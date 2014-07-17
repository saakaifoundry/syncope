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
package org.apache.syncope.installer.files;

public class OrmXml {

    public static final String PATH_DIR = "core/src/main/resources/META-INF";

    public static final String PATH_COMPLETE = PATH_DIR + "/orm.xml";

    public static final String ORACLE_ORM = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<!--\n"
            + "Licensed to the Apache Software Foundation (ASF) under one\n"
            + "or more contributor license agreements.  See the NOTICE file\n"
            + "distributed with this work for additional information\n"
            + "regarding copyright ownership.  The ASF licenses this file\n"
            + "to you under the Apache License, Version 2.0 (the\n"
            + "\"License\"); you may not use this file except in compliance\n"
            + "with the License.  You may obtain a copy of the License at\n" + "\n"
            + "  http://www.apache.org/licenses/LICENSE-2.0\n" + "\n"
            + "Unless required by applicable law or agreed to in writing,\n"
            + "software distributed under the License is distributed on an\n"
            + "\"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY\n"
            + "KIND, either express or implied.  See the License for the\n"
            + "specific language governing permissions and limitations\n" + "under the License.\n" + "\n" + "-->\n"
            + "<entity-mappings  xmlns=\"http://java.sun.com/xml/ns/persistence/orm\"\n"
            + "                  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "                  xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence/orm \n"
            + "                                      http://java.sun.com/xml/ns/persistence/orm_2_0.xsd\"\n"
            + "                  version=\"2.0\">\n" + "\n"
            + "  <table-generator name=\"SEQ_UAttrValue\" pk-column-value=\"SEQ_UAttrValue\" initial-value=\"100\"/>\n"
            + "  <table-generator name=\"SEQ_RAttrValue\" pk-column-value=\"SEQ_RAttrValue\" initial-value=\"100\"/>\n"
            + "  <table-generator name=\"SEQ_MAttrValue\" pk-column-value=\"SEQ_MAttrValue\" initial-value=\"100\"/>\n"
            + "\n" + "  <entity class=\"org.apache.syncope.core.persistence.beans.user.SyncopeUser\">\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_SyncopeUser\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_SyncopeUser\" pk-column-value=\"SEQ_SyncopeUser\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.role.SyncopeRole\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_SyncopeRole\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_SyncopeRole\" pk-column-value=\"SEQ_SyncopeRole\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.membership.Membership\">\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_Membership\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_Membership\" pk-column-value=\"SEQ_Membership\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.user.UMapping\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_UMapping\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_UMapping\" pk-column-value=\"SEQ_UMapping\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.role.RMapping\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_RMapping\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_RMapping\" pk-column-value=\"SEQ_RMapping\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.user.UMappingItem\">\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_UMappingItem\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_UMappingItem\" pk-column-value=\"SEQ_UMappingItem\" initial-value=\"1000\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.role.RMappingItem\">\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_RMappingItem\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_RMappingItem\" pk-column-value=\"SEQ_RMappingItem\" initial-value=\"1000\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.ConnInstance\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_ConnInstance\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_ConnInstance\" pk-column-value=\"SEQ_ConnInstance\" initial-value=\"1000\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.user.UAttr\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_UAttr\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_UAttr\" pk-column-value=\"SEQ_UAttr\" initial-value=\"1000\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.role.RAttr\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_RAttr\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_RAttr\" pk-column-value=\"SEQ_RAttr\" initial-value=\"1000\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.membership.MAttr\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_MAttr\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_MAttr\" pk-column-value=\"SEQ_MAttr\" initial-value=\"1000\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "    \n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.user.UAttrValue\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_UAttrValue\" strategy=\"TABLE\"/>\n" + "      </id>\n"
            + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.user.UAttrUniqueValue\">\n" + "    <table>\n"
            + "      <unique-constraint>\n" + "        <column-name>booleanValue</column-name>\n"
            + "        <column-name>dateValue</column-name>\n" + "        <column-name>stringValue</column-name>\n"
            + "        <column-name>doubleValue</column-name>\n" + "        <column-name>longValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n" + "    </table>\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_UAttrValue\" strategy=\"TABLE\"/>\n" + "      </id>\n"
            + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.role.RAttrValue\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_RAttrValue\" strategy=\"TABLE\"/>\n" + "      </id>\n"
            + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.role.RAttrUniqueValue\">\n" + "    <table>\n"
            + "      <unique-constraint>\n" + "        <column-name>booleanValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>dateValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>stringValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>doubleValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>longValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n" + "    </table>\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_RAttrValue\" strategy=\"TABLE\"/>\n" + "      </id>\n"
            + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.membership.MAttrValue\">\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_MAttrValue\" strategy=\"TABLE\"/>\n" + "      </id>\n"
            + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.membership.MAttrUniqueValue\">\n"
            + "    <table>\n" + "      <unique-constraint>\n" + "        <column-name>booleanValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>dateValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>stringValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>doubleValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>longValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n" + "    </table>\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_MAttrValue\" strategy=\"TABLE\"/>\n" + "      </id>\n"
            + "    </attributes>\n" + "  </entity>\n" + "\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.Task\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_Task\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_Task\" pk-column-value=\"SEQ_Task\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.TaskExec\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_TaskExec\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_TaskExec\" pk-column-value=\"SEQ_TaskExec\" initial-value=\"10\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "    \n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.Policy\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_Policy\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_Policy\" pk-column-value=\"SEQ_Policy\" initial-value=\"1000\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.Report\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_Report\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_Report\" pk-column-value=\"SEQ_Report\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.ReportExec\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_ReportExec\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_ReportExec\" pk-column-value=\"SEQ_ReportExec\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.ReportletConfInstance\">\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_ReportletConfInstance\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_ReportletConfInstance\" pk-column-value=\"SEQ_ReportletConfInstance\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.Notification\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_Notification\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_Notification\" pk-column-value=\"SEQ_Notification\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "</entity-mappings>";

    public static final String SQLSERVER_ORM = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<!--\n"
            + "Licensed to the Apache Software Foundation (ASF) under one\n"
            + "or more contributor license agreements.  See the NOTICE file\n"
            + "distributed with this work for additional information\n"
            + "regarding copyright ownership.  The ASF licenses this file\n"
            + "to you under the Apache License, Version 2.0 (the\n"
            + "\"License\"); you may not use this file except in compliance\n"
            + "with the License.  You may obtain a copy of the License at\n" + "\n"
            + "  http://www.apache.org/licenses/LICENSE-2.0\n" + "\n"
            + "Unless required by applicable law or agreed to in writing,\n"
            + "software distributed under the License is distributed on an\n"
            + "\"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY\n"
            + "KIND, either express or implied.  See the License for the\n"
            + "specific language governing permissions and limitations\n" + "under the License.\n" + "\n" + "-->\n"
            + "<entity-mappings  xmlns=\"http://java.sun.com/xml/ns/persistence/orm\"\n"
            + "                  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "                  xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence/orm \n"
            + "                                      http://java.sun.com/xml/ns/persistence/orm_2_0.xsd\"\n"
            + "                  version=\"2.0\">\n" + "\n"
            + "  <table-generator name=\"SEQ_UAttrValue\" pk-column-value=\"SEQ_UAttrValue\" initial-value=\"100\"/>\n"
            + "  <table-generator name=\"SEQ_RAttrValue\" pk-column-value=\"SEQ_RAttrValue\" initial-value=\"100\"/>\n"
            + "  <table-generator name=\"SEQ_MAttrValue\" pk-column-value=\"SEQ_MAttrValue\" initial-value=\"100\"/>\n"
            + "\n" + "  <entity class=\"org.apache.syncope.core.persistence.beans.user.SyncopeUser\">\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_SyncopeUser\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_SyncopeUser\" pk-column-value=\"SEQ_SyncopeUser\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.role.SyncopeRole\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_SyncopeRole\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_SyncopeRole\" pk-column-value=\"SEQ_SyncopeRole\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.membership.Membership\">\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_Membership\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_Membership\" pk-column-value=\"SEQ_Membership\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.user.UMapping\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_UMapping\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_UMapping\" pk-column-value=\"SEQ_UMapping\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.role.RMapping\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_RMapping\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_RMapping\" pk-column-value=\"SEQ_RMapping\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.user.UMappingItem\">\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_UMappingItem\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_UMappingItem\" pk-column-value=\"SEQ_UMappingItem\" initial-value=\"1000\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.role.RMappingItem\">\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_RMappingItem\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_RMappingItem\" pk-column-value=\"SEQ_RMappingItem\" initial-value=\"1000\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.ConnInstance\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_ConnInstance\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_ConnInstance\" pk-column-value=\"SEQ_ConnInstance\" initial-value=\"1000\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.user.UAttr\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_UAttr\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_UAttr\" pk-column-value=\"SEQ_UAttr\" initial-value=\"1000\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.role.RAttr\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_RAttr\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_RAttr\" pk-column-value=\"SEQ_RAttr\" initial-value=\"1000\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.membership.MAttr\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_MAttr\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_MAttr\" pk-column-value=\"SEQ_MAttr\" initial-value=\"1000\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "    \n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.user.UAttrValue\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_UAttrValue\" strategy=\"TABLE\"/>\n" + "      </id>\n"
            + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.user.UAttrUniqueValue\">\n" + "    <table>\n"
            + "      <unique-constraint>\n" + "        <column-name>booleanValue</column-name>\n"
            + "        <column-name>dateValue</column-name>\n" + "        <column-name>stringValue</column-name>\n"
            + "        <column-name>doubleValue</column-name>\n" + "        <column-name>longValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n" + "    </table>\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_UAttrValue\" strategy=\"TABLE\"/>\n" + "      </id>\n"
            + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.role.RAttrValue\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_RAttrValue\" strategy=\"TABLE\"/>\n" + "      </id>\n"
            + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.role.RAttrUniqueValue\">\n" + "    <table>\n"
            + "      <unique-constraint>\n" + "        <column-name>booleanValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>dateValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>stringValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>doubleValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>longValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n" + "    </table>\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_RAttrValue\" strategy=\"TABLE\"/>\n" + "      </id>\n"
            + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.membership.MAttrValue\">\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_MAttrValue\" strategy=\"TABLE\"/>\n" + "      </id>\n"
            + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.membership.MAttrUniqueValue\">\n"
            + "    <table>\n" + "      <unique-constraint>\n" + "        <column-name>booleanValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>dateValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>stringValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>doubleValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n"
            + "      <unique-constraint>\n" + "        <column-name>longValue</column-name>\n"
            + "        <column-name>schema_name</column-name>\n" + "      </unique-constraint>\n" + "    </table>\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_MAttrValue\" strategy=\"TABLE\"/>\n" + "      </id>\n"
            + "    </attributes>\n" + "  </entity>\n" + "\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.Task\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_Task\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_Task\" pk-column-value=\"SEQ_Task\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.TaskExec\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_TaskExec\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_TaskExec\" pk-column-value=\"SEQ_TaskExec\" initial-value=\"10\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "    \n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.Policy\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_Policy\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_Policy\" pk-column-value=\"SEQ_Policy\" initial-value=\"1000\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.Report\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n" + "        <generated-value generator=\"SEQ_Report\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_Report\" pk-column-value=\"SEQ_Report\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.ReportExec\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_ReportExec\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_ReportExec\" pk-column-value=\"SEQ_ReportExec\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.ReportletConfInstance\">\n"
            + "    <attributes>\n" + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_ReportletConfInstance\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_ReportletConfInstance\" pk-column-value=\"SEQ_ReportletConfInstance\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n"
            + "  <entity class=\"org.apache.syncope.core.persistence.beans.Notification\">\n" + "    <attributes>\n"
            + "      <id name=\"id\">\n"
            + "        <generated-value generator=\"SEQ_Notification\" strategy=\"TABLE\"/>\n"
            + "        <table-generator name=\"SEQ_Notification\" pk-column-value=\"SEQ_Notification\" initial-value=\"100\"/>\n"
            + "      </id>\n" + "    </attributes>\n" + "  </entity>\n" + "</entity-mappings>";

}