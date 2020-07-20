package org.management.asset.listeners;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CascadeCallback implements ReflectionUtils.FieldCallback {

    @Autowired
    private Object source;

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);

        if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(CascadeSave.class)) {
            final Object fieldValue = field.get(this.source);

            boolean insta = !(fieldValue instanceof String);
            boolean instanull = fieldValue != null;

            if ( instanull && insta ) {
                final FieldCallback callback = new FieldCallback();
                ReflectionUtils.doWithFields(fieldValue.getClass(), callback);
                this.mongoOperations.save(fieldValue);
            }
        }
    }
}
