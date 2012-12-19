/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.syncope.core.rest;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.syncope.client.http.PreemptiveAuthHttpRequestFactory;
import org.apache.syncope.client.mod.AttributeMod;
import org.apache.syncope.client.mod.MembershipMod;
import org.apache.syncope.client.mod.UserMod;
import org.apache.syncope.client.to.AttributeTO;
import org.apache.syncope.client.to.ConfigurationTO;
import org.apache.syncope.client.to.ConnObjectTO;
import org.apache.syncope.client.to.MembershipTO;
import org.apache.syncope.client.to.PasswordPolicyTO;
import org.apache.syncope.client.to.PolicyTO;
import org.apache.syncope.client.to.PropagationTO;
import org.apache.syncope.client.to.PropagationTaskTO;
import org.apache.syncope.client.to.ResourceTO;
import org.apache.syncope.client.to.UserTO;
import org.apache.syncope.client.to.WorkflowFormPropertyTO;
import org.apache.syncope.client.to.WorkflowFormTO;
import org.apache.syncope.client.util.AttributableOperations;
import org.apache.syncope.client.validation.SyncopeClientCompositeErrorException;
import org.apache.syncope.client.validation.SyncopeClientException;
import org.apache.syncope.core.init.SpringContextInitializer;
import org.apache.syncope.core.persistence.beans.user.SyncopeUser;
import org.apache.syncope.types.CipherAlgorithm;
import org.apache.syncope.types.PropagationTaskExecStatus;
import org.apache.syncope.types.SyncopeClientExceptionType;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.junit.Assume;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

@FixMethodOrder(MethodSorters.JVM)
public class UserTestITCase extends AbstractTest {

    public static UserTO getSampleTO(final String email) {
        UserTO userTO = new UserTO();
        userTO.setPassword("password123");
        userTO.setUsername(email);

        userTO.addAttribute(attributeTO("fullname", email));
        userTO.addAttribute(attributeTO("firstname", email));
        userTO.addAttribute(attributeTO("surname", "surname"));
        userTO.addAttribute(attributeTO("type", "a type"));
        userTO.addAttribute(attributeTO("userId", email));
        userTO.addAttribute(attributeTO("email", email));
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        userTO.addAttribute(attributeTO("loginDate", sdf.format(new Date())));
        userTO.addDerivedAttribute(attributeTO("cn", null));
        userTO.addVirtualAttribute(attributeTO("virtualdata", "virtualvalue"));
        return userTO;
    }

    @Test
    public void selfRead() {
        PreemptiveAuthHttpRequestFactory requestFactory = ((PreemptiveAuthHttpRequestFactory) restTemplate.
                getRequestFactory());
        ((DefaultHttpClient) requestFactory.getHttpClient()).getCredentialsProvider().setCredentials(
                requestFactory.getAuthScope(), new UsernamePasswordCredentials("user1", "password"));

        try {
            restTemplate.getForObject(BASE_URL + "user/read/{userId}.json", UserTO.class, 1);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.FORBIDDEN, e.getStatusCode());
        }

        UserTO userTO = restTemplate.getForObject(BASE_URL + "user/read/self", UserTO.class);
        assertEquals("user1", userTO.getUsername());
    }

    @Test
    public void createUserWithNoPropagation() {
        // get task list
        List<PropagationTaskTO> tasks = Arrays.asList(restTemplate.getForObject(BASE_URL + "task/propagation/list",
                PropagationTaskTO[].class));

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());

        // get max task id
        long maxId = Long.MIN_VALUE;
        for (PropagationTaskTO task : tasks) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }

        // create a new user
        UserTO userTO = new UserTO();
        userTO.setUsername("xxx@xxx.xxx");

        userTO.addAttribute(attributeTO("firstname", "xxx"));
        userTO.addAttribute(attributeTO("surname", "xxx"));
        userTO.addAttribute(attributeTO("userId", "xxx@xxx.xxx"));
        userTO.addAttribute(attributeTO("fullname", "xxx"));

        userTO.setPassword("password123");
        userTO.addResource("ws-target-resource-nopropagation");

        restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        // get the new task list
        tasks = Arrays.asList(restTemplate.getForObject(BASE_URL + "task/propagation/list", PropagationTaskTO[].class));
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());

        // get max task id
        long newMaxId = Long.MIN_VALUE;
        for (PropagationTaskTO task : tasks) {
            if (task.getId() > newMaxId) {
                newMaxId = task.getId();
            }
        }

        assertTrue(newMaxId > maxId);

        // get last task
        PropagationTaskTO taskTO = restTemplate.getForObject(BASE_URL + "task/read/{taskId}", PropagationTaskTO.class,
                newMaxId);

        assertNotNull(taskTO);
        assertTrue(taskTO.getExecutions().isEmpty());
    }

    @Test
    /*
     * This test has been introduced to verify and solve the following issue:
     * http://code.google.com/p/syncope/issues/detail?id=172. Creations of a new user without having a global password
     * policy stored into the local repository used to fail with a null pointer exception. This bug has been fixed
     * introducing a simple control.
     */
    public void issue172() {
        PolicyTO policyTO = restTemplate.getForObject(BASE_URL + "policy/read/{id}", PasswordPolicyTO.class, 2L);

        assertNotNull(policyTO);

        restTemplate.getForObject(BASE_URL + "policy/delete/{id}", PasswordPolicyTO.class, 2L);

        UserTO userTO = new UserTO();
        userTO.setUsername("issue172@syncope.apache.org");
        userTO.setPassword("password");

        userTO.addAttribute(attributeTO("firstname", "issue172"));
        userTO.addAttribute(attributeTO("surname", "issue172"));
        userTO.addAttribute(attributeTO("userId", "issue172@syncope.apache.org"));
        userTO.addAttribute(attributeTO("fullname", "issue172"));

        restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        policyTO = restTemplate.postForObject(BASE_URL + "policy/password/create", policyTO, PasswordPolicyTO.class);

        assertNotNull(policyTO);
    }

    @Test
    public void issue186() {
        // 1. create an user with strict mandatory attributes only
        UserTO userTO = new UserTO();
        userTO.setUsername("issue186@syncope.apache.org");
        userTO.setPassword("password");

        userTO.addAttribute(attributeTO("userId", "issue186@syncope.apache.org"));
        userTO.addAttribute(attributeTO("fullname", "issue186"));
        userTO.addAttribute(attributeTO("surname", "issue186"));

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);
        assertTrue(userTO.getResources().isEmpty());

        // 2. update assigning a resource forcing mandatory constraints: must
        // fail with RequiredValuesMissing
        UserMod userMod = new UserMod();
        userMod.setId(userTO.getId());
        userMod.setPassword("newPassword");
        userMod.addResourceToBeAdded("ws-target-resource-2");

        SyncopeClientException sce = null;
        try {
            userTO = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
        } catch (SyncopeClientCompositeErrorException scce) {
            sce = scce.getException(SyncopeClientExceptionType.RequiredValuesMissing);
        }
        assertNotNull(sce);

        // 3. update assigning a resource NOT forcing mandatory constraints
        // AND primary: must fail with PropagationException
        userMod = new UserMod();
        userMod.setId(userTO.getId());
        userMod.setPassword("newPassword");
        userMod.addResourceToBeAdded("ws-target-resource-1");

        sce = null;
        try {
            userTO = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
        } catch (SyncopeClientCompositeErrorException scce) {
            sce = scce.getException(SyncopeClientExceptionType.Propagation);
        }
        assertNotNull(sce);

        // 4. update assigning a resource NOT forcing mandatory constraints
        // BUT not primary: must succeed
        userMod = new UserMod();
        userMod.setId(userTO.getId());
        userMod.setPassword("newPassword");
        userMod.addResourceToBeAdded("resource-db");

        sce = null;
        try {
            userTO = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
        } catch (SyncopeClientCompositeErrorException scce) {
            sce = scce.getException(SyncopeClientExceptionType.Propagation);
        }
        assertNull(sce);
    }

    @Test
    public void testEnforceMandatoryCondition() {
        UserTO userTO = getSampleTO("issue183@apache.org");
        userTO.addResource("ws-target-resource-2");
        userTO.setPassword("newPassword");

        AttributeTO type = null;
        for (AttributeTO attr : userTO.getAttributes()) {
            if ("type".equals(attr.getSchema())) {
                type = attr;
            }
        }
        assertNotNull(type);
        userTO.removeAttribute(type);

        SyncopeClientException sce = null;
        try {
            userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        } catch (SyncopeClientCompositeErrorException scce) {
            sce = scce.getException(SyncopeClientExceptionType.RequiredValuesMissing);
        }
        assertNotNull(sce);

        userTO.addAttribute(type);

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);
    }

    @Test
    public void testEnforceMandatoryConditionOnDerived() {
        ResourceTO resourceTO = restTemplate.getForObject(BASE_URL + "/resource/read/{resourceName}.json",
                ResourceTO.class, "resource-csv");
        assertNotNull(resourceTO);
        resourceTO.setName("resource-csv-enforcing");
        resourceTO.setEnforceMandatoryCondition(true);
        resourceTO = restTemplate.postForObject(BASE_URL + "resource/create.json", resourceTO, ResourceTO.class);
        assertNotNull(resourceTO);

        UserTO userTO = getSampleTO("syncope222@apache.org");
        userTO.addResource(resourceTO.getName());
        userTO.setPassword("newPassword");

        SyncopeClientException sce = null;
        try {
            userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        } catch (SyncopeClientCompositeErrorException scce) {
            sce = scce.getException(SyncopeClientExceptionType.RequiredValuesMissing);
        }
        assertNotNull(sce);

        userTO.addDerivedAttribute(attributeTO("csvuserid", null));

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);
        assertEquals(Collections.singleton("resource-csv-enforcing"), userTO.getResources());
    }

    @Test
    public void issue147() {
        // 1. create an user without role nor resources
        UserTO userTO = getSampleTO("issue147@tirasa.net");

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);
        assertTrue(userTO.getResources().isEmpty());

        // 2. try to update by adding a resource, but no password: must fail
        UserMod userMod = new UserMod();
        userMod.setId(userTO.getId());
        userMod.addResourceToBeAdded("ws-target-resource-2");

        SyncopeClientException sce = null;
        try {
            userTO = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
        } catch (SyncopeClientCompositeErrorException scce) {
            sce = scce.getException(SyncopeClientExceptionType.RequiredValuesMissing);
        }
        assertNotNull(sce);

        // 3. provide password: now update must work
        userMod.setPassword("newPassword");
        userTO = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
        assertNotNull(userTO);
        assertEquals(1, userTO.getResources().size());
    }

    @Test
    public void createUserWithDbPropagation() {
        UserTO userTO = new UserTO();
        userTO.setPassword("password");
        userTO.setUsername("yyy@yyy.yyy");
        userTO.addAttribute(attributeTO("firstname", "yyy"));
        userTO.addAttribute(attributeTO("surname", "yyy"));
        userTO.addAttribute(attributeTO("userId", "yyy@yyy.yyy"));
        userTO.addAttribute(attributeTO("fullname", "yyy"));
        userTO.addResource("resource-testdb");
        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);
        assertEquals(1, userTO.getPropagationTOs().size());
        assertTrue(userTO.getPropagationTOs().get(0).getStatus().isSuccessful());
    }

    @Test(expected = SyncopeClientCompositeErrorException.class)
    public void createWithInvalidPassword() {
        UserTO userTO = getSampleTO("invalidpasswd@syncope.apache.org");
        userTO.setPassword("pass");

        restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
    }

    @Test(expected = SyncopeClientCompositeErrorException.class)
    public void createWithInvalidUsername() {
        UserTO userTO = getSampleTO("invalidusername@syncope.apache.org");
        userTO.setUsername("us");

        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(7L);

        userTO.addMembership(membershipTO);

        restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
    }

    @Test(expected = SyncopeClientCompositeErrorException.class)
    public void createWithInvalidPasswordByRes() {
        UserTO userTO = getSampleTO("invalidPwdByRes@passwd.com");

        // configured to be minLength=16
        userTO.setPassword("password1");

        userTO.setResources(Collections.singleton("ws-target-resource-nopropagation"));

        restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
    }

    @Test(expected = SyncopeClientCompositeErrorException.class)
    public void createWithInvalidPasswordByRole() {
        UserTO userTO = getSampleTO("invalidPwdByRole@passwd.com");

        // configured to be minLength=16
        userTO.setPassword("password1");

        final MembershipTO membership = new MembershipTO();
        membership.setRoleId(8L);

        userTO.addMembership(membership);

        restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
    }

    @Test(expected = SyncopeClientCompositeErrorException.class)
    public void createWithException() {
        UserTO newUserTO = new UserTO();
        newUserTO.addAttribute(attributeTO("userId", "userId@nowhere.org"));
        restTemplate.postForObject(BASE_URL + "user/create", newUserTO, UserTO.class);
    }

    @Test
    public void create() {
        // get task list
        List<PropagationTaskTO> tasks =
                Arrays.asList(restTemplate.getForObject(BASE_URL + "task/propagation/list", PropagationTaskTO[].class));

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());

        // get max task id
        long maxId = Long.MIN_VALUE;
        for (PropagationTaskTO task : tasks) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        PropagationTaskTO taskTO =
                restTemplate.getForObject(BASE_URL + "task/read/{taskId}", PropagationTaskTO.class, maxId);

        assertNotNull(taskTO);
        int maxTaskExecutions = taskTO.getExecutions().size();

        UserTO userTO = getSampleTO("a.b@c.com");

        // add a membership
        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(8L);
        userTO.addMembership(membershipTO);

        // add an attribute with no values: must be ignored
        membershipTO.addAttribute(attributeTO("subscriptionDate", null));

        // add an attribute with a non-existing schema: must be ignored
        AttributeTO attrWithInvalidSchemaTO = attributeTO("invalid schema", "a value");
        userTO.addAttribute(attrWithInvalidSchemaTO);

        // add an attribute with null value: must be ignored
        userTO.addAttribute(attributeTO("activationDate", null));

        // 1. create user
        UserTO newUserTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        assertNotNull(newUserTO);
        assertFalse(newUserTO.getAttributes().contains(attrWithInvalidSchemaTO));

        // check for changePwdDate
        assertNotNull(newUserTO.getCreationDate());

        // 2. check for virtual attribute value
        newUserTO = restTemplate.getForObject(BASE_URL + "user/read/{userId}.json", UserTO.class, newUserTO.getId());
        assertNotNull(newUserTO);

        assertNotNull(newUserTO.getVirtualAttributeMap());
        assertNotNull(newUserTO.getVirtualAttributeMap().get("virtualdata").getValues());
        assertFalse(newUserTO.getVirtualAttributeMap().get("virtualdata").getValues().isEmpty());
        assertEquals("virtualvalue", newUserTO.getVirtualAttributeMap().get("virtualdata").getValues().get(0));

        // get the new task list
        tasks = Arrays.asList(restTemplate.getForObject(BASE_URL + "task/propagation/list", PropagationTaskTO[].class));

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());

        // get max task id
        long newMaxId = Long.MIN_VALUE;
        for (PropagationTaskTO task : tasks) {
            if (task.getId() > newMaxId) {
                newMaxId = task.getId();
            }
        }

        // default configuration for ws-target-resource2:
        //             only failed executions have to be registered
        // --> no more tasks/executions should be added
        assertEquals(newMaxId, maxId);

        // get last task
        taskTO = restTemplate.getForObject(BASE_URL + "task/read/{taskId}", PropagationTaskTO.class, newMaxId);

        assertNotNull(taskTO);
        assertEquals(maxTaskExecutions, taskTO.getExecutions().size());

        // 3. verify password
        Boolean verify = restTemplate.
                getForObject(BASE_URL + "user/verifyPassword/{username}.json?password=password123",
                Boolean.class, newUserTO.getUsername());
        assertTrue(verify);

        verify = restTemplate.getForObject(BASE_URL + "user/verifyPassword/{username}.json?password=passwordXX",
                Boolean.class, newUserTO.getUsername());
        assertFalse(verify);

        // 4. try (and fail) to create another user with same (unique) values
        userTO = getSampleTO("pippo@c.com");
        for (AttributeTO attr : userTO.getAttributes()) {
            if ("userId".equals(attr.getSchema())) {
                attr.getValues().clear();
                attr.addValue("a.b@c.com");
            }
        }

        SyncopeClientException sce = null;
        try {
            restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        } catch (SyncopeClientCompositeErrorException e) {
            sce = e.getException(SyncopeClientExceptionType.DataIntegrityViolation);
        }
        assertNotNull(sce);
    }

    @Test
    public void createWithRequiredValueMissing() {
        UserTO userTO = getSampleTO("a.b@c.it");

        AttributeTO type = null;
        for (AttributeTO attr : userTO.getAttributes()) {
            if ("type".equals(attr.getSchema())) {
                type = attr;
            }
        }
        assertNotNull(type);

        userTO.removeAttribute(type);

        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(8L);
        userTO.addMembership(membershipTO);

        SyncopeClientCompositeErrorException ex = null;
        try {
            // 1. create user without type (mandatory by UserSchema)
            restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        } catch (SyncopeClientCompositeErrorException e) {
            ex = e;
        }
        assertNotNull(ex);
        assertNotNull(ex.getException(SyncopeClientExceptionType.RequiredValuesMissing));

        userTO.addAttribute(attributeTO("type", "F"));

        AttributeTO surname = null;
        for (AttributeTO attributeTO : userTO.getAttributes()) {
            if ("surname".equals(attributeTO.getSchema())) {
                surname = attributeTO;
            }
        }
        userTO.removeAttribute(surname);

        // 2. create user without surname (mandatory when type == 'F')
        ex = null;
        try {
            restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        } catch (SyncopeClientCompositeErrorException e) {
            ex = e;
        }
        assertNotNull(ex);
        assertNotNull(ex.getException(SyncopeClientExceptionType.RequiredValuesMissing));
    }

    @Test
    public void createWithReject() {
        Assume.assumeTrue(SpringContextInitializer.isActivitiEnabledForUsers());

        UserTO userTO = getSampleTO("createWithReject@syncope.apache.org");

        // User with role 9 are defined in workflow as subject to approval
        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(9L);
        userTO.addMembership(membershipTO);

        // 1. create user with role 9
        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);
        assertEquals(1, userTO.getMemberships().size());
        assertEquals(9, userTO.getMemberships().get(0).getRoleId());
        assertEquals("createApproval", userTO.getStatus());

        // 2. request if there is any pending task for user just created
        WorkflowFormTO form = restTemplate.getForObject(BASE_URL + "user/workflow/form/{userId}", WorkflowFormTO.class,
                userTO.getId());
        assertNotNull(form);
        assertNotNull(form.getTaskId());
        assertNull(form.getOwner());

        // 3. claim task from user1, not in role 7 (designated for approval in workflow definition): fail
        PreemptiveAuthHttpRequestFactory requestFactory = ((PreemptiveAuthHttpRequestFactory) restTemplate.
                getRequestFactory());
        ((DefaultHttpClient) requestFactory.getHttpClient()).getCredentialsProvider().setCredentials(
                requestFactory.getAuthScope(), new UsernamePasswordCredentials("user1", "password"));

        SyncopeClientException sce = null;
        try {
            restTemplate.getForObject(BASE_URL + "user/workflow/form/claim/{taskId}", WorkflowFormTO.class, form.
                    getTaskId());
        } catch (SyncopeClientCompositeErrorException scce) {
            sce = scce.getException(SyncopeClientExceptionType.Workflow);
        }
        assertNotNull(sce);

        // 4. claim task from user4, in role 7
        ((DefaultHttpClient) requestFactory.getHttpClient()).getCredentialsProvider().setCredentials(
                requestFactory.getAuthScope(), new UsernamePasswordCredentials("user4", "password"));

        form = restTemplate.getForObject(BASE_URL + "user/workflow/form/claim/{taskId}", WorkflowFormTO.class,
                form.getTaskId());
        assertNotNull(form);
        assertNotNull(form.getTaskId());
        assertNotNull(form.getOwner());

        // 5. reject user
        Map<String, WorkflowFormPropertyTO> props = form.getPropertyMap();
        props.get("approve").setValue(Boolean.FALSE.toString());
        props.get("rejectReason").setValue("I don't like him.");
        form.setProperties(props.values());
        userTO = restTemplate.postForObject(BASE_URL + "user/workflow/form/submit", form, UserTO.class);
        assertNotNull(userTO);
        assertEquals("rejected", userTO.getStatus());

        // reset admin credentials for restTemplate
        super.resetRestTemplate();
    }

    @Test
    public void createWithApproval() {
        Assume.assumeTrue(SpringContextInitializer.isActivitiEnabledForUsers());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(testDataSource);

        UserTO userTO = getSampleTO("createWithApproval@syncope.apache.org");
        userTO.addResource("resource-testdb");

        // User with role 9 are defined in workflow as subject to approval
        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(9L);
        userTO.addMembership(membershipTO);

        // 1. create user with role 9 (and verify that no propagation occurred)
        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);
        assertEquals(1, userTO.getMemberships().size());
        assertEquals(9, userTO.getMemberships().get(0).getRoleId());
        assertEquals("createApproval", userTO.getStatus());
        assertEquals(Collections.singleton("resource-testdb"), userTO.getResources());

        assertTrue(userTO.getPropagationTOs().isEmpty());

        Exception exception = null;
        try {
            jdbcTemplate.queryForInt("SELECT id FROM test WHERE id=?", userTO.getUsername());
        } catch (EmptyResultDataAccessException e) {
            exception = e;
        }
        assertNotNull(exception);

        // 2. request if there is any pending form for user just created
        List<WorkflowFormTO> forms = Arrays.asList(restTemplate.getForObject(
                BASE_URL + "user/workflow/form/list", WorkflowFormTO[].class));
        assertNotNull(forms);
        assertEquals(1, forms.size());

        WorkflowFormTO form = restTemplate.getForObject(BASE_URL + "user/workflow/form/{userId}", WorkflowFormTO.class,
                userTO.getId());
        assertNotNull(form);
        assertNotNull(form.getTaskId());
        assertNull(form.getOwner());

        // 4. claim task (from admin)
        form = restTemplate.getForObject(BASE_URL + "user/workflow/form/claim/{taskId}", WorkflowFormTO.class, form.
                getTaskId());
        assertNotNull(form);
        assertNotNull(form.getTaskId());
        assertNotNull(form.getOwner());

        // 5. approve user (and verify that propagation occurred)
        Map<String, WorkflowFormPropertyTO> props = form.getPropertyMap();
        props.get("approve").setValue(Boolean.TRUE.toString());
        form.setProperties(props.values());
        userTO = restTemplate.postForObject(BASE_URL + "user/workflow/form/submit", form, UserTO.class);
        assertNotNull(userTO);
        assertEquals("active", userTO.getStatus());
        assertEquals(Collections.singleton("resource-testdb"), userTO.getResources());

        exception = null;
        try {
            final String username = jdbcTemplate.queryForObject("SELECT id FROM test WHERE id=?", String.class, userTO.
                    getUsername());
            assertEquals(userTO.getUsername(), username);
        } catch (EmptyResultDataAccessException e) {
            exception = e;
        }
        assertNull(exception);

        // 6. update user
        UserMod userMod = new UserMod();
        userMod.setId(userTO.getId());
        userMod.setPassword("anotherPassword123");

        userTO = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
        assertNotNull(userTO);
    }

    @Test
    public void delete() {
        try {
            restTemplate.getForObject(BASE_URL + "user/delete/{userId}", UserTO.class, 0);
        } catch (HttpStatusCodeException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }

        UserTO userTO = getSampleTO("qqgf.z@nn.com");

        // specify a propagation
        userTO.addResource("resource-testdb");

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        long id = userTO.getId();

        userTO = restTemplate.getForObject(BASE_URL + "user/delete/{userId}", UserTO.class, id);

        assertNotNull(userTO);
        assertEquals(id, userTO.getId());
        assertTrue(userTO.getAttributes().isEmpty());

        // check for propagation result
        assertFalse(userTO.getPropagationTOs().isEmpty());
        assertTrue(userTO.getPropagationTOs().get(0).getStatus().isSuccessful());

        try {
            restTemplate.getForObject(BASE_URL + "user/read/{userId}.json", UserTO.class, userTO.getId());
        } catch (HttpStatusCodeException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void deleteByUsername() {
        UserTO userTO = getSampleTO("delete.by.username@apache.org");

        // specify a propagation
        userTO.addResource("resource-testdb");

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        long id = userTO.getId();
        userTO =
                restTemplate.getForObject(BASE_URL + "user/deleteByUsername/{username}.json", UserTO.class, userTO.
                getUsername());

        assertNotNull(userTO);
        assertEquals(id, userTO.getId());
        assertTrue(userTO.getAttributes().isEmpty());

        // check for propagation result
        assertFalse(userTO.getPropagationTOs().isEmpty());
        assertTrue(userTO.getPropagationTOs().get(0).getStatus().isSuccessful());

        try {
            restTemplate.getForObject(BASE_URL + "user/read/{userId}.json", UserTO.class, userTO.getId());
        } catch (HttpStatusCodeException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void count() {
        Integer count = restTemplate.getForObject(BASE_URL + "user/count.json", Integer.class);
        assertNotNull(count);
        assertTrue(count > 0);
    }

    @Test
    public void list() {
        List<UserTO> users = Arrays.asList(restTemplate.getForObject(BASE_URL + "user/list.json", UserTO[].class));
        assertNotNull(users);
        assertFalse(users.isEmpty());
        for (UserTO user : users) {
            assertNotNull(user);
        }
    }

    @Test
    public void paginatedList() {
        List<UserTO> users = Arrays.asList(restTemplate.getForObject(BASE_URL + "user/list/{page}/{size}.json",
                UserTO[].class, 1, 2));

        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());

        for (UserTO user : users) {
            assertNotNull(user);
        }

        users = Arrays.
                asList(restTemplate.getForObject(BASE_URL + "user/list/{page}/{size}.json", UserTO[].class, 2, 2));

        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());

        users = Arrays.asList(restTemplate.getForObject(BASE_URL + "user/list/{page}/{size}.json", UserTO[].class, 100,
                2));

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    public void read() {
        UserTO userTO = restTemplate.getForObject(BASE_URL + "user/read/{userId}.json", UserTO.class, 1);

        assertNotNull(userTO);
        assertNotNull(userTO.getAttributes());
        assertFalse(userTO.getAttributes().isEmpty());
    }

    @Test
    public void updateWithouPassword() {
        UserTO userTO = getSampleTO("updatewithout@password.com");

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        assertNotNull(userTO);

        UserMod userMod = new UserMod();
        userMod.setId(userTO.getId());
        userMod.addDerivedAttributeToBeRemoved("cn");

        userTO = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);

        assertNotNull(userTO);
        assertNotNull(userTO.getDerivedAttributeMap());
        assertFalse(userTO.getDerivedAttributeMap().containsKey("cn"));
    }

    @Test(expected = SyncopeClientCompositeErrorException.class)
    public void updateInvalidPassword() {
        UserTO userTO = getSampleTO("updateinvalid@password.com");

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);

        UserMod userMod = new UserMod();
        userMod.setId(userTO.getId());
        userMod.setPassword("pass");

        restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
    }

    @Test(expected = SyncopeClientCompositeErrorException.class)
    public void updateSamePassword() {
        UserTO userTO = getSampleTO("updatesame@password.com");

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);

        UserMod userMod = new UserMod();
        userMod.setId(userTO.getId());
        userMod.setPassword("password123");

        restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
    }

    @Test
    public void update() {
        UserTO userTO = getSampleTO("g.h@t.com");

        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(8L);
        membershipTO.addAttribute(attributeTO("subscriptionDate", "2009-08-18T16:33:12.203+0200"));
        userTO.addMembership(membershipTO);

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        assertFalse(userTO.getDerivedAttributes().isEmpty());
        assertEquals(1, userTO.getMemberships().size());

        MembershipMod membershipMod = new MembershipMod();
        membershipMod.setRole(8L);
        membershipMod.addAttributeToBeUpdated(attributeMod("subscriptionDate", "2010-08-18T16:33:12.203+0200"));

        UserMod userMod = new UserMod();
        userMod.setId(userTO.getId());
        userMod.setPassword("new2Password");

        userMod.addAttributeToBeRemoved("userId");
        userMod.addAttributeToBeUpdated(attributeMod("userId", "t.w@spre.net"));

        userMod.addAttributeToBeRemoved("fullname");
        userMod.addAttributeToBeUpdated(attributeMod("fullname", "g.h@t.com"));

        userMod.addDerivedAttributeToBeAdded("cn");
        userMod.addMembershipToBeAdded(membershipMod);
        userMod.addMembershipToBeRemoved(userTO.getMemberships().iterator().next().getId());

        userTO = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
        assertNotNull(userTO);

        SyncopeUser passwordTestUser = new SyncopeUser();
        passwordTestUser.setPassword("new2Password", CipherAlgorithm.SHA1, 0);
        assertEquals(passwordTestUser.getPassword(), userTO.getPassword());

        assertEquals(1, userTO.getMemberships().size());
        assertEquals(1, userTO.getMemberships().iterator().next().getAttributes().size());
        assertFalse(userTO.getDerivedAttributes().isEmpty());

        boolean userIdFound = false;
        boolean fullnameFound = false;

        for (AttributeTO attributeTO : userTO.getAttributes()) {
            if ("userId".equals(attributeTO.getSchema())) {
                userIdFound = true;
                assertEquals(Collections.singletonList("t.w@spre.net"), attributeTO.getValues());
            }
            if ("fullname".equals(attributeTO.getSchema())) {
                fullnameFound = true;
                assertEquals(Collections.singletonList("g.h@t.com"), attributeTO.getValues());
            }
        }
        assertTrue(userIdFound);
        assertTrue(fullnameFound);
    }

    @Test
    public void updatePasswordOnly() {
        List<PropagationTaskTO> beforeTasks = Arrays.asList(restTemplate.getForObject(BASE_URL
                + "task/propagation/list", PropagationTaskTO[].class));
        assertNotNull(beforeTasks);
        assertFalse(beforeTasks.isEmpty());

        UserTO userTO = getSampleTO("pwdonly@t.com");
        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(8L);
        membershipTO.addAttribute(attributeTO("subscriptionDate", "2009-08-18T16:33:12.203+0200"));
        userTO.addMembership(membershipTO);

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        UserMod userMod = new UserMod();
        userMod.setId(userTO.getId());
        userMod.setPassword("newPassword123");

        userTO = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);

        // check for changePwdDate
        assertNotNull(userTO.getChangePwdDate());

        SyncopeUser passwordTestUser = new SyncopeUser();
        passwordTestUser.setPassword("newPassword123", CipherAlgorithm.SHA1, 0);
        assertEquals(passwordTestUser.getPassword(), userTO.getPassword());

        List<PropagationTaskTO> afterTasks = Arrays.asList(restTemplate.getForObject(
                BASE_URL + "task/propagation/list", PropagationTaskTO[].class));
        assertNotNull(afterTasks);
        assertFalse(afterTasks.isEmpty());

        assertTrue(beforeTasks.size() < afterTasks.size());
    }

    @Test
    public void verifyTaskRegistration() {
        // get task list
        List<PropagationTaskTO> tasks = Arrays.asList(restTemplate.getForObject(BASE_URL + "task/propagation/list",
                PropagationTaskTO[].class));

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());

        // get max task id
        long maxId = Long.MIN_VALUE;
        for (PropagationTaskTO task : tasks) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }

        // --------------------------------------
        // Create operation
        // --------------------------------------

        UserTO userTO = getSampleTO("task@propagation.mode");

        // add a membership
        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(8L);
        userTO.addMembership(membershipTO);

        // 1. create user
        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);

        // get the new task list
        tasks = Arrays.asList(restTemplate.getForObject(BASE_URL + "task/propagation/list", PropagationTaskTO[].class));

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());

        // get max task id
        long newMaxId = Long.MIN_VALUE;
        for (PropagationTaskTO task : tasks) {
            if (task.getId() > newMaxId) {
                newMaxId = task.getId();
            }
        }

        // default configuration for ws-target-resource2:
        //             only failed executions have to be registered
        // --> no more tasks/executions should be added
        assertEquals(newMaxId, maxId);

        // --------------------------------------
        // Update operation
        // --------------------------------------
        UserMod userMod = new UserMod();
        userMod.setId(userTO.getId());

        userMod.addAttributeToBeUpdated(attributeMod("surname", "surname"));

        userTO = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);

        assertNotNull(userTO);

        // get the new task list
        tasks = Arrays.asList(restTemplate.getForObject(BASE_URL + "task/propagation/list", PropagationTaskTO[].class));

        // get max task id
        maxId = newMaxId;
        newMaxId = Long.MIN_VALUE;
        for (PropagationTaskTO task : tasks) {
            if (task.getId() > newMaxId) {
                newMaxId = task.getId();
            }
        }

        // default configuration for ws-target-resource2:
        //             all update executions have to be registered
        assertTrue(newMaxId > maxId);

        final PropagationTaskTO taskTO =
                restTemplate.getForObject(BASE_URL + "task/read/{taskId}", PropagationTaskTO.class, newMaxId);

        assertNotNull(taskTO);
        assertEquals(1, taskTO.getExecutions().size());

        // --------------------------------------
        // Delete operation
        // --------------------------------------
        restTemplate.getForObject(BASE_URL + "user/delete/{userId}", UserTO.class, userTO.getId());

        // get the new task list
        tasks = Arrays.asList(restTemplate.getForObject(BASE_URL + "task/propagation/list", PropagationTaskTO[].class));

        // get max task id
        maxId = newMaxId;
        newMaxId = Long.MIN_VALUE;
        for (PropagationTaskTO task : tasks) {
            if (task.getId() > newMaxId) {
                newMaxId = task.getId();
            }
        }

        // default configuration for ws-target-resource2: no delete executions have to be registered
        // --> no more tasks/executions should be added
        assertEquals(newMaxId, maxId);
    }

    @Test
    public void createActivate() {
        Assume.assumeTrue(SpringContextInitializer.isActivitiEnabledForUsers());

        UserTO userTO = getSampleTO("createActivate@syncope.apache.org");

        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(11L);
        userTO.addMembership(membershipTO);

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        assertNotNull(userTO);
        assertNotNull(userTO.getToken());
        assertNotNull(userTO.getTokenExpireTime());

        assertEquals("created", userTO.getStatus());

        userTO = restTemplate.getForObject(
                BASE_URL + "user/activate/{userId}?token=" + userTO.getToken(), UserTO.class, userTO.getId());

        assertNotNull(userTO);
        assertNull(userTO.getToken());
        assertNull(userTO.getTokenExpireTime());

        assertEquals("active", userTO.getStatus());
    }

    @Test
    public void createActivateByUsername() {
        Assume.assumeTrue(SpringContextInitializer.isActivitiEnabledForUsers());

        UserTO userTO = getSampleTO("createActivateByUsername@syncope.apache.org");

        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(11L);
        userTO.addMembership(membershipTO);

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        assertNotNull(userTO);
        assertNotNull(userTO.getToken());
        assertNotNull(userTO.getTokenExpireTime());

        assertEquals("created", userTO.getStatus());

        userTO = restTemplate.getForObject(
                BASE_URL + "user/activateByUsername/{username}.json?token=" + userTO.getToken(),
                UserTO.class, userTO.getUsername());

        assertNotNull(userTO);
        assertNull(userTO.getToken());
        assertNull(userTO.getTokenExpireTime());

        assertEquals("active", userTO.getStatus());
    }

    @Test
    public void suspendReactivate() {
        UserTO userTO = getSampleTO("suspendReactivate@syncope.apache.org");

        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(7L);
        userTO.addMembership(membershipTO);

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        assertNotNull(userTO);
        assertEquals(SpringContextInitializer.isActivitiEnabledForUsers() ? "active" : "created", userTO.getStatus());

        userTO = restTemplate.getForObject(BASE_URL + "user/suspend/" + userTO.getId(), UserTO.class);

        assertNotNull(userTO);
        assertEquals("suspended", userTO.getStatus());

        userTO = restTemplate.getForObject(BASE_URL + "user/reactivate/" + userTO.getId(), UserTO.class);

        assertNotNull(userTO);
        assertEquals("active", userTO.getStatus());
    }

    @Test
    public void suspendReactivateByUsername() {
        UserTO userTO = getSampleTO("suspendReactivateByUsername@syncope.apache.org");

        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(7L);
        userTO.addMembership(membershipTO);

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        assertNotNull(userTO);
        assertEquals(SpringContextInitializer.isActivitiEnabledForUsers() ? "active" : "created", userTO.getStatus());

        userTO = restTemplate.getForObject(
                BASE_URL + "user/suspendByUsername/{username}.json", UserTO.class, userTO.getUsername());

        assertNotNull(userTO);
        assertEquals("suspended", userTO.getStatus());

        userTO = restTemplate.getForObject(
                BASE_URL + "user/reactivateByUsername/{username}.json", UserTO.class, userTO.getUsername());

        assertNotNull(userTO);
        assertEquals("active", userTO.getStatus());
    }

    @Test
    public void suspendReactivateOnResource() {
        UserTO userTO = getSampleTO("suspreactonresource@syncope.apache.org");

        userTO.getMemberships().clear();
        userTO.getResources().clear();

        ResourceTO dbTable = restTemplate.getForObject(BASE_URL + "/resource/read/{resourceName}.json",
                ResourceTO.class, "resource-testdb");

        assertNotNull(dbTable);
        userTO.addResource(dbTable.getName());

        ResourceTO ldap = restTemplate.getForObject(BASE_URL + "/resource/read/{resourceName}.json", ResourceTO.class,
                "resource-ldap");

        assertNotNull(ldap);
        userTO.addResource(ldap.getName());

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        assertNotNull(userTO);
        assertEquals(SpringContextInitializer.isActivitiEnabledForUsers() ? "active" : "created", userTO.getStatus());

        String query = "?resourceNames=" + dbTable.getName() + "&resourceNames=" + ldap.getName()
                + "&performLocally=true"; // check also performLocally

        userTO = restTemplate.getForObject(BASE_URL + "user/suspend/" + userTO.getId() + query, UserTO.class);

        assertNotNull(userTO);
        assertEquals("suspended", userTO.getStatus());

        String dbTableUID = userTO.getUsername();
        assertNotNull(dbTableUID);

        ConnObjectTO connObjectTO = restTemplate.getForObject(BASE_URL
                + "/resource/{resourceName}/read/USER/{objectId}.json", ConnObjectTO.class,
                dbTable.getName(), dbTableUID);
        assertFalse(Boolean.parseBoolean(connObjectTO.getAttributeMap().get(OperationalAttributes.ENABLE_NAME).
                getValues().
                get(0)));

        String ldapUID = userTO.getUsername();
        assertNotNull(ldapUID);

        connObjectTO = restTemplate.getForObject(BASE_URL + "/resource/{resourceName}/read/USER/{objectId}.json",
                ConnObjectTO.class, ldap.getName(), ldapUID);
        assertNotNull(connObjectTO);

        query = "?resourceNames=" + ldap.getName() + "&performLocally=false"; // check also performLocally

        userTO = restTemplate.getForObject(BASE_URL + "user/reactivate/" + userTO.getId() + query, UserTO.class);
        assertNotNull(userTO);
        assertEquals("suspended", userTO.getStatus());

        connObjectTO = restTemplate.getForObject(BASE_URL + "/resource/{resourceName}/read/USER/{objectId}.json",
                ConnObjectTO.class, dbTable.getName(), dbTableUID);
        assertFalse(Boolean.parseBoolean(connObjectTO.getAttributeMap().get(OperationalAttributes.ENABLE_NAME).
                getValues().
                get(0)));

        query = "?resourceNames=" + dbTable.getName() + "&performLocally=true"; // check also performLocally

        userTO = restTemplate.getForObject(BASE_URL + "user/reactivate/" + userTO.getId() + query, UserTO.class);
        assertNotNull(userTO);
        assertEquals("active", userTO.getStatus());

        connObjectTO = restTemplate.getForObject(BASE_URL + "/resource/{resourceName}/read/USER/{objectId}.json",
                ConnObjectTO.class, dbTable.getName(), dbTableUID);
        assertTrue(Boolean.parseBoolean(connObjectTO.getAttributeMap().get(OperationalAttributes.ENABLE_NAME).
                getValues().
                get(0)));
    }

    public void updateMultivalueAttribute() {
        UserTO userTO = getSampleTO("multivalue@syncope.apache.org");
        userTO.getResources().clear();
        userTO.getDerivedAttributes().clear();
        userTO.getVirtualAttributes().clear();

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);

        AttributeTO loginDate = userTO.getAttributeMap().get("loginDate");
        assertNotNull(loginDate);
        assertEquals(1, loginDate.getValues().size());

        UserMod userMod = new UserMod();

        AttributeMod loginDateMod = new AttributeMod();
        loginDateMod.addValueToBeAdded("2000-01-01");

        userMod.setId(userTO.getId());
        userMod.addAttributeToBeUpdated(loginDateMod);

        userTO = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
        assertNotNull(userTO);

        loginDate = userTO.getAttributeMap().get("loginDate");
        assertNotNull(loginDate);
        assertEquals(2, loginDate.getValues().size());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void issue213() {
        UserTO userTO = getSampleTO("issue213@syncope.apache.org");
        userTO.addResource("resource-testdb");

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);
        assertEquals(1, userTO.getResources().size());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(testDataSource);

        String username =
                jdbcTemplate.queryForObject("SELECT id FROM test WHERE id=?", String.class, userTO.getUsername());

        assertEquals(userTO.getUsername(), username);

        UserMod userMod = new UserMod();

        userMod.setId(userTO.getId());
        userMod.addResourceToBeRemoved("resource-testdb");

        userTO = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);

        assertTrue(userTO.getResources().isEmpty());

        jdbcTemplate.queryForObject("SELECT id FROM test WHERE id=?", String.class, userTO.getUsername());
    }

    @Test
    public void issue234() {
        UserTO userTO = getSampleTO("issue234@syncope.apache.org");
        userTO.addResource("resource-ldap");

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);

        UserMod userMod = new UserMod();

        userMod.setId(userTO.getId());
        userMod.setUsername("1" + userTO.getUsername());

        userTO = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);

        assertNotNull(userTO);

        assertEquals("1issue234@syncope.apache.org", userTO.getUsername());
    }

    @Test
    public void issue270() {
        // 1. create a new user without virtual attributes
        UserTO original = getSampleTO("issue270@syncope.apache.org");
        // be sure to remove all virtual attributes
        original.setVirtualAttributes(Collections.<AttributeTO>emptyList());

        original = restTemplate.postForObject(BASE_URL + "user/create", original, UserTO.class);

        assertNotNull(original);

        assertTrue(original.getVirtualAttributes().isEmpty());

        UserTO toBeUpdated = restTemplate.getForObject(BASE_URL + "user/read/{userId}.json", UserTO.class, original.
                getId());

        AttributeTO virtual = attributeTO("virtualdata", "virtualvalue");
        toBeUpdated.addVirtualAttribute(virtual);

        // 2. try to update by adding a resource, but no password: must fail
        UserMod userMod = AttributableOperations.diff(toBeUpdated, original);

        assertNotNull(userMod);

        toBeUpdated = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);

        assertNotNull(toBeUpdated);

        assertFalse(toBeUpdated.getVirtualAttributes().isEmpty());
        assertNotNull(toBeUpdated.getVirtualAttributes().get(0));

        assertEquals(virtual.getSchema(), toBeUpdated.getVirtualAttributes().get(0).getSchema());
    }

    @Test
    public final void issue280() {
        UserTO userTO = getSampleTO("issue280@syncope.apache.org");
        userTO.getResources().clear();
        userTO.getMemberships().clear();
        userTO.getDerivedAttributes().clear();

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);

        UserMod userMod = new UserMod();
        userMod.setId(userTO.getId());
        userMod.setPassword("123password");
        userMod.addResourceToBeAdded("resource-testdb");

        userTO = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
        assertNotNull(userTO);

        final List<PropagationTO> propagations = userTO.getPropagationTOs();

        assertNotNull(propagations);
        assertEquals(1, propagations.size());

        final PropagationTaskExecStatus status = propagations.get(0).getStatus();
        final String resource = propagations.get(0).getResourceName();

        assertNotNull(status);
        assertEquals("resource-testdb", resource);
        assertTrue(status.isSuccessful());
    }

    @Test
    public void issue281() {
        UserTO userTO = getSampleTO("issue281@syncope.apache.org");
        userTO.getResources().clear();
        userTO.getMemberships().clear();
        userTO.getDerivedAttributes().clear();
        userTO.addResource("resource-csv");

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);

        final List<PropagationTO> propagations = userTO.getPropagationTOs();

        assertNotNull(propagations);
        assertEquals(1, propagations.size());

        final PropagationTaskExecStatus status = propagations.get(0).getStatus();
        final String resource = propagations.get(0).getResourceName();

        assertNotNull(status);
        assertEquals("resource-csv", resource);
        assertFalse(status.isSuccessful());
    }

    @Test
    public void issue288() {
        UserTO userTO = getSampleTO("issue288@syncope.apache.org");
        userTO.addAttribute(attributeTO("aLong", "STRING"));

        try {
            restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
            fail();
        } catch (SyncopeClientCompositeErrorException sccee) {
            assertNotNull(sccee.getException(SyncopeClientExceptionType.InvalidValues));
        }
    }

    @Test
    public void roleAttrPropagation() {
        UserTO userTO = getSampleTO("checkRoleAttrPropagation@syncope.apache.org");
        userTO.getResources().clear();
        userTO.getMemberships().clear();
        userTO.getDerivedAttributes().clear();
        userTO.getVirtualAttributes().clear();

        userTO.addDerivedAttribute(attributeTO("csvuserid", null));

        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(1L);

        userTO.addMembership(membershipTO);

        userTO.addResource("resource-csv");

        UserTO actual = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        assertNotNull(actual);
        assertNotNull(actual.getDerivedAttributeMap().get("csvuserid"));

        ConnObjectTO connObjectTO = restTemplate.getForObject(BASE_URL
                + "/resource/{resourceName}/read/USER/{objectId}.json", ConnObjectTO.class, "resource-csv",
                actual.getDerivedAttributeMap().get("csvuserid").getValues().get(0));
        assertNotNull(connObjectTO);
        assertEquals("sx-dx", connObjectTO.getAttributeMap().get("ROLE").getValues().get(0));
    }

    @Test
    public void membershipAttrPropagation() {
        UserTO userTO = getSampleTO("checkMembAttrPropagation@syncope.apache.org");
        userTO.getResources().clear();
        userTO.getMemberships().clear();
        userTO.getDerivedAttributes().clear();
        userTO.getVirtualAttributes().clear();
        userTO.addDerivedAttribute(attributeTO("csvuserid", null));

        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(1L);
        membershipTO.addAttribute(attributeTO("mderived_sx", "sx"));
        membershipTO.addAttribute(attributeTO("mderived_dx", "dx"));
        membershipTO.addDerivedAttribute(attributeTO("mderToBePropagated", null));
        userTO.addMembership(membershipTO);

        userTO.addResource("resource-csv");

        UserTO actual = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        assertNotNull(actual);
        assertNotNull(actual.getDerivedAttributeMap().get("csvuserid"));

        ConnObjectTO connObjectTO = restTemplate.getForObject(BASE_URL
                + "/resource/{resourceName}/read/USER/{objectId}.json", ConnObjectTO.class, "resource-csv",
                actual.getDerivedAttributeMap().get("csvuserid").getValues().get(0));
        assertNotNull(connObjectTO);
        assertEquals("sx-dx", connObjectTO.getAttributeMap().get("MEMBERSHIP").getValues().get(0));
    }

    @Test
    public void issueSYNCOPE16() {
        UserTO userTO = getSampleTO("virattrupdate@apache.org");

        MembershipTO membershipTO = new MembershipTO();
        membershipTO.setRoleId(8L);
        userTO.addMembership(membershipTO);

        // 1. create user
        UserTO actual = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(actual);

        // 2. check for virtual attribute value
        actual = restTemplate.getForObject(BASE_URL + "user/read/{userId}.json", UserTO.class, actual.getId());
        assertNotNull(actual);
        assertEquals("virtualvalue", actual.getVirtualAttributeMap().get("virtualdata").getValues().get(0));

        UserMod userMod = new UserMod();
        userMod.setId(actual.getId());
        userMod.addVirtualAttributeToBeRemoved("virtualdata");
        userMod.addVirtualAttributeToBeUpdated(attributeMod("virtualdata", "virtualupdated"));

        // 3. update virtual attribute
        actual = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
        assertNotNull(actual);

        // 4. check for virtual attribute value
        actual = restTemplate.getForObject(BASE_URL + "user/read/{userId}.json", UserTO.class, actual.getId());
        assertNotNull(actual);
        assertEquals("virtualupdated", actual.getVirtualAttributeMap().get("virtualdata").getValues().get(0));
    }

    @Test
    public void issueSYNCOPE108() {
        UserTO userTO = getSampleTO("syncope108@syncope.apache.org");
        userTO.getResources().clear();
        userTO.getMemberships().clear();
        userTO.getDerivedAttributes().clear();
        userTO.getVirtualAttributes().clear();
        userTO.addDerivedAttribute(attributeTO("csvuserid", null));

        MembershipTO memb12 = new MembershipTO();
        memb12.setRoleId(12L);

        userTO.addMembership(memb12);

        MembershipTO memb13 = new MembershipTO();
        memb13.setRoleId(13L);

        userTO.addMembership(memb13);

        userTO.addResource("resource-csv");

        UserTO actual = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);

        assertNotNull(actual);
        assertEquals(2, actual.getMemberships().size());
        assertEquals(1, actual.getResources().size());

        ConnObjectTO connObjectTO = restTemplate.getForObject(BASE_URL
                + "/resource/{resourceName}/read/USER/{objectId}.json", ConnObjectTO.class, "resource-csv",
                actual.getDerivedAttributeMap().get("csvuserid").getValues().get(0));
        assertNotNull(connObjectTO);

        // -----------------------------------
        // Remove the first membership: de-provisioning shouldn't happen
        // -----------------------------------
        UserMod userMod = new UserMod();
        userMod.setId(actual.getId());

        userMod.addMembershipToBeRemoved(actual.getMemberships().get(0).getId());

        actual = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
        assertNotNull(actual);
        assertEquals(1, actual.getMemberships().size());

        connObjectTO = restTemplate.getForObject(BASE_URL
                + "/resource/{resourceName}/read/USER/{objectId}.json", ConnObjectTO.class, "resource-csv",
                actual.getDerivedAttributeMap().get("csvuserid").getValues().get(0));
        assertNotNull(connObjectTO);
        // -----------------------------------

        // -----------------------------------
        // Remove the resource assigned directly: de-provisioning shouldn't happen
        // -----------------------------------
        userMod = new UserMod();
        userMod.setId(actual.getId());

        userMod.addResourceToBeRemoved(actual.getResources().iterator().next());

        actual = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
        assertNotNull(actual);
        assertEquals(1, actual.getMemberships().size());
        assertFalse(actual.getResources().isEmpty());

        connObjectTO = restTemplate.getForObject(BASE_URL
                + "/resource/{resourceName}/read/USER/{objectId}.json", ConnObjectTO.class, "resource-csv",
                actual.getDerivedAttributeMap().get("csvuserid").getValues().get(0));
        assertNotNull(connObjectTO);
        // -----------------------------------

        // -----------------------------------
        // Remove the first membership: de-provisioning should happen
        // -----------------------------------
        userMod = new UserMod();
        userMod.setId(actual.getId());

        userMod.addMembershipToBeRemoved(actual.getMemberships().get(0).getId());

        actual = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
        assertNotNull(actual);
        assertTrue(actual.getMemberships().isEmpty());
        assertTrue(actual.getResources().isEmpty());

        Throwable t = null;

        try {
            restTemplate.getForObject(BASE_URL
                    + "/resource/{resourceName}/read/USER/{objectId}.json", ConnObjectTO.class, "resource-csv",
                    actual.getDerivedAttributeMap().get("csvuserid").getValues().get(0));
        } catch (SyncopeClientCompositeErrorException e) {
            assertNotNull(e.getException(SyncopeClientExceptionType.NotFound));
            t = e;
        }

        assertNotNull(t);
        // -----------------------------------
    }

    @Test
    public void issueSYNCOPE111() {
        UserTO userTO = getSampleTO("syncope111@syncope.apache.org");
        userTO.getResources().clear();
        userTO.getMemberships().clear();
        userTO.getDerivedAttributes().clear();
        userTO.getVirtualAttributes().clear();
        userTO.addDerivedAttribute(attributeTO("csvuserid", null));

        MembershipTO memb12 = new MembershipTO();
        memb12.setRoleId(12L);
        memb12.addAttribute(attributeTO("postalAddress", "postalAddress"));
        userTO.addMembership(memb12);

        MembershipTO memb13 = new MembershipTO();
        memb13.setRoleId(13L);
        userTO.addMembership(memb13);

        userTO.addResource("resource-ldap");

        UserTO actual = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(actual);
        assertEquals(2, actual.getMemberships().size());

        ConnObjectTO connObjectTO = restTemplate.getForObject(
                BASE_URL + "/resource/{resourceName}/read/USER/{objectId}.json",
                ConnObjectTO.class,
                "resource-ldap",
                userTO.getUsername());
        assertNotNull(connObjectTO);

        AttributeTO postalAddress = connObjectTO.getAttributeMap().get("postalAddress");
        assertNotNull(postalAddress);
        assertEquals(1, postalAddress.getValues().size());
        assertEquals("postalAddress", postalAddress.getValues().get(0));

        AttributeTO title = connObjectTO.getAttributeMap().get("title");
        assertNotNull(title);
        assertEquals(2, title.getValues().size());
        assertTrue(title.getValues().contains("r12") && title.getValues().contains("r13"));

        // -----------------------------------
        // Remove the first membership and check for membership attr propagation and role attr propagation
        // -----------------------------------
        UserMod userMod = new UserMod();
        userMod.setId(actual.getId());

        MembershipTO membershipTO = actual.getMemberships().get(0).getRoleId() == 12L
                ? actual.getMemberships().get(0) : actual.getMemberships().get(1);

        userMod.addMembershipToBeRemoved(membershipTO.getId());

        actual = restTemplate.postForObject(BASE_URL + "user/update", userMod, UserTO.class);
        assertNotNull(actual);
        assertEquals(1, actual.getMemberships().size());

        connObjectTO = restTemplate.getForObject(
                BASE_URL + "/resource/{resourceName}/read/USER/{objectId}.json",
                ConnObjectTO.class,
                "resource-ldap",
                userTO.getUsername());
        assertNotNull(connObjectTO);

        postalAddress = connObjectTO.getAttributeMap().get("postalAddress");
        assertTrue(postalAddress == null
                || postalAddress.getValues().isEmpty() || StringUtils.hasText(postalAddress.getValues().get(0)));

        title = connObjectTO.getAttributeMap().get("title");
        assertNotNull(title);
        assertEquals(1, title.getValues().size());
        assertTrue(title.getValues().contains("r13"));
        // -----------------------------------
    }

    @Test
    public void issueSYNCOPE185() {
        // 1. create user with LDAP resource, succesfully propagated
        UserTO userTO = getSampleTO("syncope185@syncope.apache.org");
        userTO.getVirtualAttributes().clear();
        userTO.addResource("resource-ldap");

        userTO = restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
        assertNotNull(userTO);
        assertFalse(userTO.getPropagationTOs().isEmpty());
        assertEquals("resource-ldap", userTO.getPropagationTOs().get(0).getResourceName());
        assertEquals(PropagationTaskExecStatus.SUCCESS, userTO.getPropagationTOs().get(0).getStatus());

        // 2. delete this user
        restTemplate.getForObject(BASE_URL + "user/delete/{userId}", UserTO.class, userTO.getId());

        // 3. try (and fail) to find this user on the external LDAP resource
        SyncopeClientException sce = null;
        try {
            restTemplate.getForObject(BASE_URL + "/resource/{resourceName}/read/USER/{objectId}.json",
                    ConnObjectTO.class, "resource-ldap", userTO.getUsername());
            fail("This entry should not be present on this resource");
        } catch (SyncopeClientCompositeErrorException sccee) {
            sce = sccee.getException(SyncopeClientExceptionType.NotFound);
        }
        assertNotNull(sce);
    }

    @Test()
    public void issueSYNCOPE51() {
        ConfigurationTO defaultConfigurationTO = restTemplate.getForObject(
                BASE_URL + "configuration/read/{key}.json", ConfigurationTO.class, "password.cipher.algorithm");

        ConfigurationTO configurationTO = new ConfigurationTO();
        configurationTO.setKey("password.cipher.algorithm");
        configurationTO.setValue("MD5");

        ConfigurationTO newConfTO =
                restTemplate.postForObject(BASE_URL + "configuration/update", configurationTO, ConfigurationTO.class);

        assertEquals(configurationTO, newConfTO);

        UserTO userTO = getSampleTO("syncope51@syncope.apache.org");
        userTO.setPassword("password");

        try {
            restTemplate.postForObject(BASE_URL + "user/create", userTO, UserTO.class);
            fail();
        } catch (SyncopeClientCompositeErrorException e) {
            assertTrue(
                    e.getException(SyncopeClientExceptionType.NotFound).getElements().iterator().next().contains("MD5"));
        }

        ConfigurationTO oldConfTO = restTemplate.postForObject(
                BASE_URL + "configuration/update", defaultConfigurationTO, ConfigurationTO.class);

        assertEquals(defaultConfigurationTO, oldConfTO);
    }
}