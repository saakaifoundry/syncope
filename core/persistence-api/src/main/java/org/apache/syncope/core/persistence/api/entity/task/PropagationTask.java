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
package org.apache.syncope.core.persistence.api.entity.task;

import java.util.Set;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.common.lib.types.ResourceOperation;
import org.apache.syncope.core.persistence.api.entity.resource.ExternalResource;
import org.identityconnectors.framework.common.objects.Attribute;

public interface PropagationTask extends Task {

    String getConnObjectKey();

    void setConnObjectKey(String connObjectKey);

    String getOldConnObjectKey();

    void setOldConnObjectKey(String oldConnObjectKey);

    String getSerializedAttributes();

    Set<Attribute> getAttributes();

    void setAttributes(Set<Attribute> attributes);

    String getObjectClassName();

    void setObjectClassName(String objectClassName);

    ResourceOperation getOperation();

    void setOperation(ResourceOperation operation);

    String getAnyKey();

    void setAnyKey(String anyKey);

    AnyTypeKind getAnyTypeKind();

    void setAnyTypeKind(AnyTypeKind anyTypeKind);

    String getAnyType();

    void setAnyType(String anyType);

    ExternalResource getResource();

    void setResource(ExternalResource resource);
}
