/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package org.syncope.core.persistence.beans.user;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import org.hibernate.annotations.Cascade;
import org.syncope.core.persistence.beans.AbstractAttributable;
import org.syncope.core.persistence.beans.AbstractAttribute;
import org.syncope.core.persistence.beans.AbstractAttributeValue;
import org.syncope.core.persistence.beans.AbstractSchema;

@Entity
public class UserAttribute extends AbstractAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
    generator = "SEQ_UserAttribute")
    @TableGenerator(name = "SEQ_UserAttribute", allocationSize = 200)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private SyncopeUser owner;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserSchema schema;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "attribute")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<UserAttributeValue> values;

    public UserAttribute() {
        values = new ArrayList<UserAttributeValue>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public <T extends AbstractAttributable> T getOwner() {
        return (T) owner;
    }

    @Override
    public <T extends AbstractAttributable> void setOwner(T owner) {
        this.owner = (SyncopeUser) owner;
    }

    @Override
    public <T extends AbstractSchema> T getSchema() {
        return (T) schema;
    }

    @Override
    public <T extends AbstractSchema> void setSchema(T schema) {
        this.schema = (UserSchema) schema;
    }

    @Override
    public <T extends AbstractAttributeValue> boolean addValue(
            T attributeValue) {

        return values.add((UserAttributeValue) attributeValue);
    }

    @Override
    public <T extends AbstractAttributeValue> boolean removeValue(
            T attributeValue) {

        return values.remove((UserAttributeValue) attributeValue);
    }

    @Override
    public <T extends AbstractAttributeValue> List<T> getValues() {
        return (List<T>) values;
    }

    @Override
    public <T extends AbstractAttributeValue> void setValues(
            List<T> attributeValues) {

        this.values = (List<UserAttributeValue>) attributeValues;

    }
}