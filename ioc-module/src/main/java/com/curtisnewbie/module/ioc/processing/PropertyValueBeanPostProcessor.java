package com.curtisnewbie.module.ioc.processing;

import com.curtisnewbie.module.ioc.annotations.PropertyValue;
import com.curtisnewbie.module.ioc.context.PropertyRegistry;
import com.curtisnewbie.module.ioc.exceptions.UnableToInjectDependencyException;
import com.curtisnewbie.module.ioc.util.BeansUtil;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Implementation of {@link BeanPostProcessor} that injects property's value
 *
 * @author yongjie.zhuang
 */
public class PropertyValueBeanPostProcessor implements BeanPostProcessor {

    private final Class<PropertyValue> propertyValueAnnotation = PropertyValue.class;
    private final PropertyRegistry propertyRegistry;


    public PropertyValueBeanPostProcessor(PropertyRegistry propertyRegistry) {
        this.propertyRegistry = propertyRegistry;
    }

    @Override
    public Object postProcessBeanAfterInstantiation(Object bean, String beanName) {
        Objects.requireNonNull(bean, "Unable to post process null bean");
        Class<?> beanClz = bean.getClass();
        Map<String, PropertyDescriptor> pdMap = BeansUtil.introspectPropertyDescriptorMap(beanClz);
        Field[] fields = BeansUtil.introspectBeanFields(beanClz);
        for (Field f : fields) {
            // check if it has supported annotations, such as @PropertyValue
            if (!hasSupportedAnnotation(f.getDeclaredAnnotations()))
                continue;

            Annotation annotation = f.getDeclaredAnnotation(propertyValueAnnotation);
            Objects.requireNonNull(annotation);

            PropertyValue anno = propertyValueAnnotation.cast(annotation);
            String propertyKey = anno.value();
            if (propertyKey == null || propertyKey.trim().isEmpty()) {
                throw new UnableToInjectDependencyException("Property key is null or empty, unable to inject property value");
            }

            // if the property is required, but it's not found
            if (!propertyRegistry.containsProperty(propertyKey) && anno.required()) {
                throw new UnableToInjectDependencyException(
                        String.format("Property value for '%s' is not found and it's required", propertyKey)
                );
            } else {
                PropertyDescriptor pd = pdMap.get(f.getName());
                // if pd == null, means there is not getter and setter for this field at all
                // as long as the writerMethod is missing, this field is not injectable
                Method writeMethod;
                if (pd == null
                        || (writeMethod = pd.getWriteMethod()) == null
                        || Modifier.isPrivate(writeMethod.getModifiers())) {
                    throw new UnableToInjectDependencyException(
                            String.format("Unable to inject property value, field: '%s' is not accessible, setter method is not found or private",
                                    f.getName())
                    );
                }
                // try to set the value
                try {
                    writeMethod.invoke(bean, propertyRegistry.getProperty(propertyKey));
                } catch (ReflectiveOperationException e) {
                    throw new UnableToInjectDependencyException(
                            String.format("Unable to inject property value in field: '%s' for property: '%s'", f.getName(), propertyKey)
                    );
                }
            }
        }
        return bean;
    }

    private boolean hasSupportedAnnotation(Annotation[] annotations) {
        for (Annotation a : annotations) {
            if (propertyValueAnnotation.equals(a.annotationType()))
                return true;
        }
        return false;
    }

}
