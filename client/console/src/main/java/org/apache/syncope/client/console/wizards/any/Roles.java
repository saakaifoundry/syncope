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
package org.apache.syncope.client.console.wizards.any;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.syncope.client.console.rest.RoleRestClient;
import org.apache.syncope.client.console.wicket.markup.html.form.AjaxPalettePanel;
import org.apache.syncope.common.lib.EntityTOUtils;
import org.apache.syncope.common.lib.to.AnyTO;
import org.apache.syncope.common.lib.to.RoleTO;
import org.apache.syncope.common.lib.to.UserTO;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;

public class Roles extends WizardStep {

    private static final long serialVersionUID = 552437609667518888L;

    public <T extends AnyTO> Roles(final UserTO entityTO) {
        this.setOutputMarkupId(true);

        List<String> allRoles = CollectionUtils.collect(new RoleRestClient().list(),
                EntityTOUtils.<RoleTO>keyTransformer(), new ArrayList<String>());
        Collections.sort(allRoles);

        add(new AjaxPalettePanel.Builder<String>().build("roles",
                new PropertyModel<List<String>>(entityTO, "roles"),
                new ListModel<>(allRoles)).hideLabel().setOutputMarkupId(true));

        add(new AjaxPalettePanel.Builder<String>().build("dynroles",
                new PropertyModel<List<String>>(entityTO, "dynRoles"),
                new ListModel<>(allRoles)).hideLabel().setEnabled(false).setOutputMarkupId(true));
    }
}
