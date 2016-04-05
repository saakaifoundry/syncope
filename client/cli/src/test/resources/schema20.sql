-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements.  See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership.  The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License.  You may obtain a copy of the License at
--
--   http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied.  See the License for the
-- specific language governing permissions and limitations
-- under the License.

CREATE TABLE AccountPolicy (id BIGINT NOT NULL, description VARCHAR(255), maxAuthenticationAttempts INTEGER, propagateSuspension INTEGER, PRIMARY KEY (id));
CREATE TABLE AccountPolicy_ExternalResource (accountPolicy_id BIGINT, resource_name VARCHAR(255));
CREATE TABLE AccountRuleConfInstance (id BIGINT NOT NULL, serializedInstance CLOB, ACCOUNTPOLICY_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE ADynGroupMembership (id BIGINT NOT NULL, fiql VARCHAR(255), GROUP_ID BIGINT, ANYTYPE_NAME VARCHAR(255), PRIMARY KEY (id));
CREATE TABLE ADynGroupMembership_AnyObject (aDynGroupMembership_id BIGINT, anyObject_id BIGINT);
CREATE TABLE AMembership (id BIGINT NOT NULL, anyObject_id BIGINT, group_id BIGINT, PRIMARY KEY (id));
CREATE TABLE AnyAbout (id BIGINT NOT NULL, filter CLOB, ANYTYPE_NAME VARCHAR(255), NOTIFICATION_ID BIGINT, PRIMARY KEY (id), CONSTRAINT U_NYABOUT_NOTIFICATION_ID UNIQUE (NOTIFICATION_ID, ANYTYPE_NAME));
CREATE TABLE AnyObject (id BIGINT NOT NULL, creationDate TIMESTAMP NOT NULL, creator VARCHAR(255) NOT NULL, lastChangeDate TIMESTAMP NOT NULL, lastModifier VARCHAR(255) NOT NULL, status VARCHAR(255), workflowId VARCHAR(255), REALM_ID BIGINT, TYPE_NAME VARCHAR(255), PRIMARY KEY (id));
CREATE TABLE AnyObject_AnyTypeClass (anyObject_id BIGINT, anyTypeClass_name VARCHAR(255));
CREATE TABLE AnyObject_ExternalResource (anyObject_id BIGINT, resource_name VARCHAR(255));
CREATE TABLE AnyTemplatePullTask (id BIGINT NOT NULL, template CLOB, ANYTYPE_NAME VARCHAR(255), PULLTASK_ID BIGINT, PRIMARY KEY (id), CONSTRAINT U_NYTMTSK_PULLTASK_ID UNIQUE (PULLTASK_ID, ANYTYPE_NAME));
CREATE TABLE AnyTemplateRealm (id BIGINT NOT NULL, template CLOB, REALM_ID BIGINT, ANYTYPE_NAME VARCHAR(255), PRIMARY KEY (id), CONSTRAINT U_NYTMRLM_REALM_ID UNIQUE (REALM_ID, ANYTYPE_NAME));
CREATE TABLE AnyType (name VARCHAR(255) NOT NULL, kind VARCHAR(20), PRIMARY KEY (name));
CREATE TABLE AnyTypeClass (name VARCHAR(255) NOT NULL, PRIMARY KEY (name));
CREATE TABLE AnyType_AnyTypeClass (anyType_name VARCHAR(255), anyTypeClass_name VARCHAR(255));
CREATE TABLE APlainAttr (id BIGINT NOT NULL, OWNER_ID BIGINT, schema_name VARCHAR(255), PRIMARY KEY (id));
CREATE TABLE APlainAttrUniqueValue (id BIGINT NOT NULL, binaryValue BLOB, booleanValue INTEGER, dateValue TIMESTAMP, doubleValue DOUBLE, longValue BIGINT, stringValue VARCHAR(255), ATTRIBUTE_ID BIGINT, schema_name VARCHAR(255), PRIMARY KEY (id), CONSTRAINT U_PLNTQVL_BOOLEANVALUE1 UNIQUE (booleanValue, schema_name), CONSTRAINT U_PLNTQVL_DATEVALUE1 UNIQUE (dateValue, schema_name), CONSTRAINT U_PLNTQVL_STRINGVALUE1 UNIQUE (stringValue, schema_name), CONSTRAINT U_PLNTQVL_DOUBLEVALUE1 UNIQUE (doubleValue, schema_name), CONSTRAINT U_PLNTQVL_LONGVALUE1 UNIQUE (longValue, schema_name));
CREATE TABLE APlainAttrValue (id BIGINT NOT NULL, binaryValue BLOB, booleanValue INTEGER, dateValue TIMESTAMP, doubleValue DOUBLE, longValue BIGINT, stringValue VARCHAR(255), ATTRIBUTE_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE ARelationship (id BIGINT NOT NULL, left_anyObject_id BIGINT, right_anyObject_id BIGINT, TYPE_NAME VARCHAR(255), PRIMARY KEY (id), CONSTRAINT U_RLTNSHP_TYPE_NAME1 UNIQUE (TYPE_NAME, left_anyObject_id, right_anyObject_id));
CREATE TABLE ConnInstance (id BIGINT NOT NULL, bundleName VARCHAR(255), connRequestTimeout INTEGER, connectorName VARCHAR(255), displayName VARCHAR(255), jsonConf CLOB, location VARCHAR(255), version VARCHAR(255), maxIdle INTEGER, maxObjects INTEGER, maxWait BIGINT, minEvictableIdleTimeMillis BIGINT, minIdle INTEGER, PRIMARY KEY (id), CONSTRAINT U_CNNNTNC_DISPLAYNAME UNIQUE (displayName));
CREATE TABLE ConnInstance_capabilities (connInstance_id BIGINT, capability VARCHAR(20));
CREATE TABLE CPlainAttr (id BIGINT NOT NULL, schema_name VARCHAR(255), OWNER_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE CPlainAttrUniqueValue (id BIGINT NOT NULL, binaryValue BLOB, booleanValue INTEGER, dateValue TIMESTAMP, doubleValue DOUBLE, longValue BIGINT, stringValue VARCHAR(255), ATTRIBUTE_ID BIGINT, schema_name VARCHAR(255), PRIMARY KEY (id), CONSTRAINT U_CPLNQVL_BOOLEANVALUE UNIQUE (booleanValue, schema_name), CONSTRAINT U_CPLNQVL_DATEVALUE UNIQUE (dateValue, schema_name), CONSTRAINT U_CPLNQVL_STRINGVALUE UNIQUE (stringValue, schema_name), CONSTRAINT U_CPLNQVL_DOUBLEVALUE UNIQUE (doubleValue, schema_name), CONSTRAINT U_CPLNQVL_LONGVALUE UNIQUE (longValue, schema_name));
CREATE TABLE CPlainAttrValue (id BIGINT NOT NULL, binaryValue BLOB, booleanValue INTEGER, dateValue TIMESTAMP, doubleValue DOUBLE, longValue BIGINT, stringValue VARCHAR(255), ATTRIBUTE_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE DerSchema (name VARCHAR(255) NOT NULL, expression VARCHAR(255), ANYTYPECLASS_NAME VARCHAR(255), PRIMARY KEY (name));
CREATE TABLE DynRoleMembership (id BIGINT NOT NULL, fiql VARCHAR(255), ROLE_NAME VARCHAR(255), PRIMARY KEY (id));
CREATE TABLE DynRoleMembership_User (dynRoleMembership_id BIGINT, user_id BIGINT);
CREATE TABLE ExternalResource (name VARCHAR(255) NOT NULL, creationDate TIMESTAMP NOT NULL, creator VARCHAR(255) NOT NULL, lastChangeDate TIMESTAMP NOT NULL, lastModifier VARCHAR(255) NOT NULL, createTraceLevel VARCHAR(20), deleteTraceLevel VARCHAR(20), enforceMandatoryCondition INTEGER, jsonConf CLOB, overrideCapabilities INTEGER, propagationPriority INTEGER, pullTraceLevel VARCHAR(20), randomPwdIfNotProvided INTEGER, updateTraceLevel VARCHAR(20), ACCOUNTPOLICY_ID BIGINT, CONNECTOR_ID BIGINT, PASSWORDPOLICY_ID BIGINT, PULLPOLICY_ID BIGINT, PRIMARY KEY (name));
CREATE TABLE ExternalResource_capOverride (resource_name VARCHAR(255), capabilityOverride VARCHAR(20));
CREATE TABLE ExternalResource_PropActions (resource_name VARCHAR(255), actionClassName VARCHAR(255));
CREATE TABLE GPlainAttr (id BIGINT NOT NULL, schema_name VARCHAR(255), OWNER_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE GPlainAttrUniqueValue (id BIGINT NOT NULL, binaryValue BLOB, booleanValue INTEGER, dateValue TIMESTAMP, doubleValue DOUBLE, longValue BIGINT, stringValue VARCHAR(255), ATTRIBUTE_ID BIGINT, schema_name VARCHAR(255), PRIMARY KEY (id), CONSTRAINT U_GPLNQVL_BOOLEANVALUE UNIQUE (booleanValue, schema_name), CONSTRAINT U_GPLNQVL_DATEVALUE UNIQUE (dateValue, schema_name), CONSTRAINT U_GPLNQVL_STRINGVALUE UNIQUE (stringValue, schema_name), CONSTRAINT U_GPLNQVL_DOUBLEVALUE UNIQUE (doubleValue, schema_name), CONSTRAINT U_GPLNQVL_LONGVALUE UNIQUE (longValue, schema_name));
CREATE TABLE GPlainAttrValue (id BIGINT NOT NULL, binaryValue BLOB, booleanValue INTEGER, dateValue TIMESTAMP, doubleValue DOUBLE, longValue BIGINT, stringValue VARCHAR(255), ATTRIBUTE_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE MailTemplate (name VARCHAR(255) NOT NULL, htmlTemplate CLOB, textTemplate CLOB, PRIMARY KEY (name));
CREATE TABLE Mapping (id BIGINT NOT NULL, connObjectLink VARCHAR(255), PROVISION_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE MappingItem (id BIGINT NOT NULL, connObjectKey INTEGER, extAttrName VARCHAR(255), intAttrName VARCHAR(255), intMappingType VARCHAR(22), mandatoryCondition VARCHAR(255), password INTEGER, purpose VARCHAR(20), MAPPING_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE MappingItem_Transformer (mappingItem_id BIGINT, transformerClassName VARCHAR(255));
CREATE TABLE Notification (id BIGINT NOT NULL, active INTEGER, recipientAttrName VARCHAR(255), recipientAttrType VARCHAR(22), recipientsFIQL VARCHAR(255), recipientsProviderClassName VARCHAR(255), selfAsRecipient INTEGER, sender VARCHAR(255), subject VARCHAR(255), traceLevel VARCHAR(20), template_name VARCHAR(255), PRIMARY KEY (id));
CREATE TABLE NotificationTask_recipients (notificationTask_id BIGINT, address VARCHAR(255));
CREATE TABLE Notification_events (notification_id BIGINT, event VARCHAR(255));
CREATE TABLE Notification_staticRecipients (notification_id BIGINT, staticRecipients VARCHAR(255));
CREATE TABLE OPENJPA_SEQUENCES_TABLE (ID VARCHAR(255) NOT NULL, SEQUENCE_VALUE BIGINT, PRIMARY KEY (ID));
CREATE TABLE PasswordPolicy (id BIGINT NOT NULL, description VARCHAR(255), allowNullPassword INTEGER, historyLength INTEGER, PRIMARY KEY (id));
CREATE TABLE PasswordRuleConfInstance (id BIGINT NOT NULL, serializedInstance CLOB, PASSWORDPOLICY_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE PlainSchema (name VARCHAR(255) NOT NULL, cipherAlgorithm VARCHAR(20), conversionPattern VARCHAR(255), enumerationKeys CLOB, enumerationValues CLOB, mandatoryCondition VARCHAR(255), mimeType VARCHAR(255), multivalue INTEGER, readonly INTEGER, secretKey VARCHAR(255), type VARCHAR(20), uniqueConstraint INTEGER, validatorClass VARCHAR(255), ANYTYPECLASS_NAME VARCHAR(255), PRIMARY KEY (name));
CREATE TABLE Provision (id BIGINT NOT NULL, objectClass VARCHAR(255), serializedSyncToken CLOB, ANYTYPE_NAME VARCHAR(255), RESOURCE_NAME VARCHAR(255), PRIMARY KEY (id), CONSTRAINT U_PROVSON_RESOURCE_NAME UNIQUE (RESOURCE_NAME, ANYTYPE_NAME));
CREATE TABLE PullPolicy (id BIGINT NOT NULL, description VARCHAR(255), specification CLOB, PRIMARY KEY (id));
CREATE TABLE PullTask_actionsClassNames (pullTask_id BIGINT, actionClassName VARCHAR(255));
CREATE TABLE PushPolicy (id BIGINT NOT NULL, description VARCHAR(255), specification CLOB, PRIMARY KEY (id));
CREATE TABLE PushTaskAnyFilter (id BIGINT NOT NULL, fiql VARCHAR(255), PUSHTASK_ID BIGINT, ANYTYPE_NAME VARCHAR(255), PRIMARY KEY (id), CONSTRAINT U_PSHTLTR_PUSHTASK_ID UNIQUE (PUSHTASK_ID, ANYTYPE_NAME));
CREATE TABLE PushTask_actionsClassNames (pushTask_id BIGINT, actionClassName VARCHAR(255));
CREATE TABLE Realm (id BIGINT NOT NULL, name VARCHAR(255), ACCOUNTPOLICY_ID BIGINT, PARENT_ID BIGINT, PASSWORDPOLICY_ID BIGINT, PRIMARY KEY (id), CONSTRAINT U_REALM_NAME UNIQUE (name, PARENT_ID));
CREATE TABLE Realm_actionsClassNames (realm_id BIGINT, actionClassName VARCHAR(255));
CREATE TABLE RelationshipType (name VARCHAR(255) NOT NULL, description VARCHAR(255), PRIMARY KEY (name));
CREATE TABLE Report (id BIGINT NOT NULL, active INTEGER, cronExpression VARCHAR(255), name VARCHAR(255) NOT NULL, template_name VARCHAR(255), PRIMARY KEY (id), CONSTRAINT U_REPORT_NAME UNIQUE (name));
CREATE TABLE ReportExec (id BIGINT NOT NULL, endDate TIMESTAMP, message CLOB, startDate TIMESTAMP, status VARCHAR(255), execResult BLOB, REPORT_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE ReportletConfInstance (id BIGINT NOT NULL, serializedInstance CLOB, REPORT_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE ReportTemplate (name VARCHAR(255) NOT NULL, csvTemplate CLOB, foTemplate CLOB, htmlTemplate CLOB, PRIMARY KEY (name));
CREATE TABLE SecurityQuestion (id BIGINT NOT NULL, content VARCHAR(255), PRIMARY KEY (id), CONSTRAINT U_SCRTSTN_CONTENT UNIQUE (content));
CREATE TABLE SyncopeConf (id BIGINT NOT NULL, creationDate TIMESTAMP NOT NULL, creator VARCHAR(255) NOT NULL, lastChangeDate TIMESTAMP NOT NULL, lastModifier VARCHAR(255) NOT NULL, PRIMARY KEY (id));
CREATE TABLE SyncopeDomain (name VARCHAR(255) NOT NULL, adminCipherAlgorithm VARCHAR(20), adminPwd VARCHAR(255), PRIMARY KEY (name));
CREATE TABLE SyncopeGroup (id BIGINT NOT NULL, creationDate TIMESTAMP NOT NULL, creator VARCHAR(255) NOT NULL, lastChangeDate TIMESTAMP NOT NULL, lastModifier VARCHAR(255) NOT NULL, status VARCHAR(255), workflowId VARCHAR(255), name VARCHAR(255), REALM_ID BIGINT, GROUPOWNER_ID BIGINT, USEROWNER_ID BIGINT, PRIMARY KEY (id), CONSTRAINT U_SYNCGRP_NAME UNIQUE (name));
CREATE TABLE SyncopeGroup_AnyTypeClass (group_id BIGINT, anyTypeClass_name VARCHAR(255));
CREATE TABLE SyncopeGroup_ExternalResource (group_id BIGINT, resource_name VARCHAR(255));
CREATE TABLE SyncopeLogger (logName VARCHAR(255) NOT NULL, logLevel VARCHAR(20) NOT NULL, logType VARCHAR(20) NOT NULL, PRIMARY KEY (logName));
CREATE TABLE SyncopeRole (name VARCHAR(255) NOT NULL, PRIMARY KEY (name));
CREATE TABLE SyncopeRole_entitlements (role_name VARCHAR(255), entitlement VARCHAR(255));
CREATE TABLE SyncopeRole_Realm (role_name VARCHAR(255), realm_id BIGINT);
CREATE TABLE SyncopeUser (id BIGINT NOT NULL, creationDate TIMESTAMP NOT NULL, creator VARCHAR(255) NOT NULL, lastChangeDate TIMESTAMP NOT NULL, lastModifier VARCHAR(255) NOT NULL, status VARCHAR(255), workflowId VARCHAR(255), changePwdDate TIMESTAMP, cipherAlgorithm VARCHAR(20), failedLogins INTEGER, lastLoginDate TIMESTAMP, mustChangePassword INTEGER, password VARCHAR(255), securityAnswer VARCHAR(255), suspended INTEGER, token CLOB, tokenExpireTime TIMESTAMP, username VARCHAR(255), REALM_ID BIGINT, SECURITYQUESTION_ID BIGINT, PRIMARY KEY (id), CONSTRAINT U_SYNCPSR_USERNAME UNIQUE (username));
CREATE TABLE SyncopeUser_AnyTypeClass (user_id BIGINT, anyTypeClass_name VARCHAR(255));
CREATE TABLE SyncopeUser_ExternalResource (user_id BIGINT, resource_name VARCHAR(255));
CREATE TABLE SyncopeUser_passwordHistory (user_id BIGINT, passwordHistoryValue VARCHAR(255));
CREATE TABLE SyncopeUser_SyncopeRole (user_id BIGINT, role_name VARCHAR(255));
CREATE TABLE Task (id BIGINT NOT NULL, DTYPE VARCHAR(31), active INTEGER, cronExpression VARCHAR(255), description VARCHAR(255), jobDelegateClassName VARCHAR(255), name VARCHAR(255), startAt TIMESTAMP, matchingRule VARCHAR(20), performCreate INTEGER, performDelete INTEGER, performUpdate INTEGER, pullStatus INTEGER, unmatchingRule VARCHAR(20), pullMode VARCHAR(23), reconciliationFilterBuilderClassName VARCHAR(255), RESOURCE_NAME VARCHAR(255), DESTINATIONREALM_ID BIGINT, executed INTEGER, htmlBody CLOB, sender VARCHAR(255), subject VARCHAR(255), textBody CLOB, traceLevel VARCHAR(20), anyKey BIGINT, anyType VARCHAR(255), anyTypeKind VARCHAR(20), attributes CLOB, connObjectKey VARCHAR(255), objectClassName VARCHAR(255), oldConnObjectKey VARCHAR(255), operation VARCHAR(20), PRIMARY KEY (id));
CREATE TABLE TaskExec (id BIGINT NOT NULL, endDate TIMESTAMP, message CLOB, startDate TIMESTAMP, status VARCHAR(255), TASK_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE TypeExtension (id BIGINT NOT NULL, GROUP_ID BIGINT, ANYTYPE_NAME VARCHAR(255), PRIMARY KEY (id), CONSTRAINT U_TYPXNSN_GROUP_ID UNIQUE (GROUP_ID, ANYTYPE_NAME));
CREATE TABLE TypeExtension_AnyTypeClass (typeExtension_id BIGINT, anyTypeClass_name VARCHAR(255));
CREATE TABLE UDynGroupMembership (id BIGINT NOT NULL, fiql VARCHAR(255), GROUP_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE UDynGroupMembership_User (uDynGroupMembership_id BIGINT, user_id BIGINT);
CREATE TABLE UMembership (id BIGINT NOT NULL, user_id BIGINT, group_id BIGINT, PRIMARY KEY (id));
CREATE TABLE UPlainAttr (id BIGINT NOT NULL, OWNER_ID BIGINT, schema_name VARCHAR(255), PRIMARY KEY (id));
CREATE TABLE UPlainAttrUniqueValue (id BIGINT NOT NULL, binaryValue BLOB, booleanValue INTEGER, dateValue TIMESTAMP, doubleValue DOUBLE, longValue BIGINT, stringValue VARCHAR(255), ATTRIBUTE_ID BIGINT, schema_name VARCHAR(255), PRIMARY KEY (id), CONSTRAINT U_PLNTQVL_BOOLEANVALUE UNIQUE (booleanValue, schema_name), CONSTRAINT U_PLNTQVL_DATEVALUE UNIQUE (dateValue, schema_name), CONSTRAINT U_PLNTQVL_STRINGVALUE UNIQUE (stringValue, schema_name), CONSTRAINT U_PLNTQVL_DOUBLEVALUE UNIQUE (doubleValue, schema_name), CONSTRAINT U_PLNTQVL_LONGVALUE UNIQUE (longValue, schema_name));
CREATE TABLE UPlainAttrValue (id BIGINT NOT NULL, binaryValue BLOB, booleanValue INTEGER, dateValue TIMESTAMP, doubleValue DOUBLE, longValue BIGINT, stringValue VARCHAR(255), ATTRIBUTE_ID BIGINT, PRIMARY KEY (id));
CREATE TABLE URelationship (id BIGINT NOT NULL, user_id BIGINT, anyObject_id BIGINT, TYPE_NAME VARCHAR(255), PRIMARY KEY (id), CONSTRAINT U_RLTNSHP_TYPE_NAME UNIQUE (TYPE_NAME, user_id, anyObject_id));
CREATE TABLE VirSchema (name VARCHAR(255) NOT NULL, extAttrName VARCHAR(255), readonly INTEGER, ANYTYPECLASS_NAME VARCHAR(255), PROVISION_ID BIGINT, PRIMARY KEY (name));
CREATE INDEX I_CCNTSRC_ACCOUNTPOLICY_ID ON AccountPolicy_ExternalResource (accountPolicy_id);
CREATE INDEX I_CCNTSRC_ELEMENT ON AccountPolicy_ExternalResource (resource_name);
CREATE INDEX I_CCNTTNC_ACCOUNTPOLICY ON AccountRuleConfInstance (ACCOUNTPOLICY_ID);
CREATE INDEX I_DYNGSHP_ANYTYPE ON ADynGroupMembership (ANYTYPE_NAME);
CREATE INDEX I_DYNGSHP_GROUP ON ADynGroupMembership (GROUP_ID);
CREATE INDEX I_DYNGJCT_ADYNGROUPMEMBERSHIP_ID ON ADynGroupMembership_AnyObject (aDynGroupMembership_id);
CREATE INDEX I_DYNGJCT_ELEMENT ON ADynGroupMembership_AnyObject (anyObject_id);
CREATE INDEX I_MMBRSHP_LEFTEND1 ON AMembership (anyObject_id);
CREATE INDEX I_MMBRSHP_RIGHTEND1 ON AMembership (group_id);
CREATE INDEX I_NYABOUT_ANYTYPE ON AnyAbout (ANYTYPE_NAME);
CREATE INDEX I_NYABOUT_NOTIFICATION ON AnyAbout (NOTIFICATION_ID);
CREATE INDEX I_NYOBJCT_REALM ON AnyObject (REALM_ID);
CREATE INDEX I_NYOBJCT_TYPE ON AnyObject (TYPE_NAME);
CREATE INDEX I_NYBJLSS_ANYOBJECT_ID ON AnyObject_AnyTypeClass (anyObject_id);
CREATE INDEX I_NYBJLSS_ELEMENT ON AnyObject_AnyTypeClass (anyTypeClass_name);
CREATE INDEX I_NYBJSRC_ANYOBJECT_ID ON AnyObject_ExternalResource (anyObject_id);
CREATE INDEX I_NYBJSRC_ELEMENT ON AnyObject_ExternalResource (resource_name);
CREATE INDEX I_NYTMTSK_ANYTYPE ON AnyTemplatePullTask (ANYTYPE_NAME);
CREATE INDEX I_NYTMTSK_PULLTASK ON AnyTemplatePullTask (PULLTASK_ID);
CREATE INDEX I_NYTMRLM_ANYTYPE ON AnyTemplateRealm (ANYTYPE_NAME);
CREATE INDEX I_NYTMRLM_REALM ON AnyTemplateRealm (REALM_ID);
CREATE INDEX I_NYTYLSS_ANYTYPE_NAME ON AnyType_AnyTypeClass (anyType_name);
CREATE INDEX I_NYTYLSS_ELEMENT ON AnyType_AnyTypeClass (anyTypeClass_name);
CREATE INDEX I_PLINTTR_OWNER ON APlainAttr (OWNER_ID);
CREATE INDEX I_PLINTTR_SCHEMA ON APlainAttr (schema_name);
CREATE INDEX I_PLNTQVL_ATTRIBUTE1 ON APlainAttrUniqueValue (ATTRIBUTE_ID);
CREATE INDEX I_PLNTQVL_SCHEMA1 ON APlainAttrUniqueValue (schema_name);
CREATE INDEX I_PLNTRVL_ATTRIBUTE1 ON APlainAttrValue (ATTRIBUTE_ID);
CREATE INDEX I_RLTNSHP_LEFTEND1 ON ARelationship (left_anyObject_id);
CREATE INDEX I_RLTNSHP_RIGHTEND1 ON ARelationship (right_anyObject_id);
CREATE INDEX I_RLTNSHP_TYPE1 ON ARelationship (TYPE_NAME);
CREATE INDEX I_CNNNLTS_CONNINSTANCE_ID ON ConnInstance_capabilities (connInstance_id);
CREATE INDEX I_CPLNTTR_OWNER ON CPlainAttr (OWNER_ID);
CREATE INDEX I_CPLNTTR_SCHEMA ON CPlainAttr (schema_name);
CREATE INDEX I_CPLNQVL_ATTRIBUTE ON CPlainAttrUniqueValue (ATTRIBUTE_ID);
CREATE INDEX I_CPLNQVL_SCHEMA ON CPlainAttrUniqueValue (schema_name);
CREATE INDEX I_CPLNRVL_ATTRIBUTE ON CPlainAttrValue (ATTRIBUTE_ID);
CREATE INDEX I_DRSCHEM_ANYTYPECLASS ON DerSchema (ANYTYPECLASS_NAME);
CREATE INDEX I_DYNRSHP_ROLE ON DynRoleMembership (ROLE_NAME);
CREATE INDEX I_DYNR_SR_DYNROLEMEMBERSHIP_ID ON DynRoleMembership_User (dynRoleMembership_id);
CREATE INDEX I_DYNR_SR_ELEMENT ON DynRoleMembership_User (user_id);
CREATE INDEX I_XTRNSRC_ACCOUNTPOLICY ON ExternalResource (ACCOUNTPOLICY_ID);
CREATE INDEX I_XTRNSRC_CONNECTOR ON ExternalResource (CONNECTOR_ID);
CREATE INDEX I_XTRNSRC_PASSWORDPOLICY ON ExternalResource (PASSWORDPOLICY_ID);
CREATE INDEX I_XTRNSRC_PULLPOLICY ON ExternalResource (PULLPOLICY_ID);
CREATE INDEX I_XTRNRRD_RESOURCE_NAME ON ExternalResource_capOverride (resource_name);
CREATE INDEX I_XTRNTNS_RESOURCE_NAME ON ExternalResource_PropActions (resource_name);
CREATE INDEX I_GPLNTTR_OWNER ON GPlainAttr (OWNER_ID);
CREATE INDEX I_GPLNTTR_SCHEMA ON GPlainAttr (schema_name);
CREATE INDEX I_GPLNQVL_ATTRIBUTE ON GPlainAttrUniqueValue (ATTRIBUTE_ID);
CREATE INDEX I_GPLNQVL_SCHEMA ON GPlainAttrUniqueValue (schema_name);
CREATE INDEX I_GPLNRVL_ATTRIBUTE ON GPlainAttrValue (ATTRIBUTE_ID);
CREATE INDEX I_MAPPING_PROVISION ON Mapping (PROVISION_ID);
CREATE INDEX I_MPPNGTM_MAPPING ON MappingItem (MAPPING_ID);
CREATE INDEX I_MPPNRMR_MAPPINGITEM_ID ON MappingItem_Transformer (mappingItem_id);
CREATE INDEX I_NTFCTON_TEMPLATE ON Notification (template_name);
CREATE INDEX I_NTFCNTS_NOTIFICATIONTASK_ID ON NotificationTask_recipients (notificationTask_id);
CREATE INDEX I_NTFCNTS_NOTIFICATION_ID ON Notification_events (notification_id);
CREATE INDEX I_NTFCNTS_NOTIFICATION_ID1 ON Notification_staticRecipients (notification_id);
CREATE INDEX I_PSSWTNC_PASSWORDPOLICY ON PasswordRuleConfInstance (PASSWORDPOLICY_ID);
CREATE INDEX I_PLNSCHM_ANYTYPECLASS ON PlainSchema (ANYTYPECLASS_NAME);
CREATE INDEX I_PROVSON_ANYTYPE ON Provision (ANYTYPE_NAME);
CREATE INDEX I_PROVSON_RESOURCE ON Provision (RESOURCE_NAME);
CREATE INDEX I_PLLTNMS_PULLTASK_ID ON PullTask_actionsClassNames (pullTask_id);
CREATE INDEX I_PSHTLTR_ANYTYPE ON PushTaskAnyFilter (ANYTYPE_NAME);
CREATE INDEX I_PSHTLTR_PUSHTASK ON PushTaskAnyFilter (PUSHTASK_ID);
CREATE INDEX I_PSHTNMS_PUSHTASK_ID ON PushTask_actionsClassNames (pushTask_id);
CREATE INDEX I_REALM_ACCOUNTPOLICY ON Realm (ACCOUNTPOLICY_ID);
CREATE INDEX I_REALM_PARENT ON Realm (PARENT_ID);
CREATE INDEX I_REALM_PASSWORDPOLICY ON Realm (PASSWORDPOLICY_ID);
CREATE INDEX I_RLM_NMS_REALM_ID ON Realm_actionsClassNames (realm_id);
CREATE INDEX I_REPORT_TEMPLATE ON Report (template_name);
CREATE INDEX I_RPORTXC_REPORT ON ReportExec (REPORT_ID);
CREATE INDEX I_RPRTTNC_REPORT ON ReportletConfInstance (REPORT_ID);
CREATE INDEX I_SYNCGRP_GROUPOWNER ON SyncopeGroup (GROUPOWNER_ID);
CREATE INDEX I_SYNCGRP_REALM ON SyncopeGroup (REALM_ID);
CREATE INDEX I_SYNCGRP_USEROWNER ON SyncopeGroup (USEROWNER_ID);
CREATE INDEX I_SYNCLSS_ELEMENT ON SyncopeGroup_AnyTypeClass (anyTypeClass_name);
CREATE INDEX I_SYNCLSS_GROUP_ID ON SyncopeGroup_AnyTypeClass (group_id);
CREATE INDEX I_SYNCSRC_ELEMENT ON SyncopeGroup_ExternalResource (resource_name);
CREATE INDEX I_SYNCSRC_GROUP_ID ON SyncopeGroup_ExternalResource (group_id);
CREATE INDEX I_SYNCNTS_ROLE_NAME ON SyncopeRole_entitlements (role_name);
CREATE INDEX I_SYNCRLM_ELEMENT ON SyncopeRole_Realm (realm_id);
CREATE INDEX I_SYNCRLM_ROLE_NAME ON SyncopeRole_Realm (role_name);
CREATE INDEX I_SYNCPSR_REALM ON SyncopeUser (REALM_ID);
CREATE INDEX I_SYNCPSR_SECURITYQUESTION ON SyncopeUser (SECURITYQUESTION_ID);
CREATE INDEX I_SYNCLSS_ELEMENT1 ON SyncopeUser_AnyTypeClass (anyTypeClass_name);
CREATE INDEX I_SYNCLSS_USER_ID ON SyncopeUser_AnyTypeClass (user_id);
CREATE INDEX I_SYNCSRC_ELEMENT1 ON SyncopeUser_ExternalResource (resource_name);
CREATE INDEX I_SYNCSRC_USER_ID ON SyncopeUser_ExternalResource (user_id);
CREATE INDEX I_SYNCTRY_USER_ID ON SyncopeUser_passwordHistory (user_id);
CREATE INDEX I_SYNCPRL_ELEMENT ON SyncopeUser_SyncopeRole (role_name);
CREATE INDEX I_SYNCPRL_USER_ID ON SyncopeUser_SyncopeRole (user_id);
CREATE INDEX I_TASK_DESTINATIONREALM ON Task (DESTINATIONREALM_ID);
CREATE INDEX I_TASK_DTYPE ON Task (DTYPE);
CREATE INDEX I_TASK_RESOURCE ON Task (RESOURCE_NAME);
CREATE INDEX I_TSKEXEC_TASK ON TaskExec (TASK_ID);
CREATE INDEX I_TYPXNSN_ANYTYPE ON TypeExtension (ANYTYPE_NAME);
CREATE INDEX I_TYPXNSN_GROUP ON TypeExtension (GROUP_ID);
CREATE INDEX I_TYPXLSS_ELEMENT ON TypeExtension_AnyTypeClass (anyTypeClass_name);
CREATE INDEX I_TYPXLSS_TYPEEXTENSION_ID ON TypeExtension_AnyTypeClass (typeExtension_id);
CREATE INDEX I_DYNGSHP_GROUP1 ON UDynGroupMembership (GROUP_ID);
CREATE INDEX I_DYNG_SR_ELEMENT ON UDynGroupMembership_User (user_id);
CREATE INDEX I_DYNG_SR_UDYNGROUPMEMBERSHIP_ID ON UDynGroupMembership_User (uDynGroupMembership_id);
CREATE INDEX I_MMBRSHP_LEFTEND ON UMembership (user_id);
CREATE INDEX I_MMBRSHP_RIGHTEND ON UMembership (group_id);
CREATE INDEX I_UPLNTTR_OWNER ON UPlainAttr (OWNER_ID);
CREATE INDEX I_UPLNTTR_SCHEMA ON UPlainAttr (schema_name);
CREATE INDEX I_PLNTQVL_ATTRIBUTE ON UPlainAttrUniqueValue (ATTRIBUTE_ID);
CREATE INDEX I_PLNTQVL_SCHEMA ON UPlainAttrUniqueValue (schema_name);
CREATE INDEX I_PLNTRVL_ATTRIBUTE ON UPlainAttrValue (ATTRIBUTE_ID);
CREATE INDEX I_RLTNSHP_LEFTEND ON URelationship (user_id);
CREATE INDEX I_RLTNSHP_RIGHTEND ON URelationship (anyObject_id);
CREATE INDEX I_RLTNSHP_TYPE ON URelationship (TYPE_NAME);
CREATE INDEX I_VIRSCHM_ANYTYPECLASS ON VirSchema (ANYTYPECLASS_NAME);
CREATE INDEX I_VIRSCHM_PROVISION ON VirSchema (PROVISION_ID);