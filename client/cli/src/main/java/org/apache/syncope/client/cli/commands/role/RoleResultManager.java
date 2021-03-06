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
package org.apache.syncope.client.cli.commands.role;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.syncope.client.cli.commands.CommonsResultManager;
import org.apache.syncope.common.lib.to.RoleTO;

public class RoleResultManager extends CommonsResultManager {

    public void printRoles(final List<RoleTO> roleTOs) {
        System.out.println("");
        for (final RoleTO roleTO : roleTOs) {
            printRole(roleTO);
        }
    }

    private void printRole(final RoleTO roleTO) {
        System.out.println(" > ROLE ID: " + roleTO.getKey());
        System.out.println("    REALMS: ");
        printRealms(roleTO.getRealms());
        System.out.println("    ENTITLEMENTS:");
        printEntitlements(roleTO.getEntitlements());
        System.out.println("    dynamic membership condition: " + roleTO.getDynMembershipCond());
        System.out.println("");
    }
    
    private void printRealms(final List<String> realms) {
        for (final String realm : realms) {
            System.out.println("       - " + realm);
        }
    }

    private void printEntitlements(final Set<String> entitlements) {
        for (final String entitlement : entitlements) {
            System.out.println("       - " + entitlement);
        }
    }

    public void printDetails(final Map<String, String> details) {
        printDetails("Roles details", details);
    }
}
