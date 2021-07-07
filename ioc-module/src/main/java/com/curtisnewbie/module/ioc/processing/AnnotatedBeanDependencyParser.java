package com.curtisnewbie.module.ioc.processing;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.beans.BeanPropertyInfo;
import com.curtisnewbie.module.ioc.beans.DefaultBeanPropertyInfo;
import com.curtisnewbie.module.ioc.beans.DefaultDependentBeanInfo;
import com.curtisnewbie.module.ioc.beans.DependentBeanInfo;
import com.curtisnewbie.module.ioc.exceptions.TypeNotSupportedForInjectionException;
import com.curtisnewbie.module.ioc.exceptions.UnableToInjectDependencyException;
import com.curtisnewbie.module.ioc.util.BeansUtil;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Parser of annotated beans' dependencies
 *
 * @author yongjie.zhuang
 */
public class AnnotatedBeanDependencyParser implements BeanDependencyParser {

    private Set<Class<? extends Annotation>> injectableAnnotations = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Dependency.class
    )));
    private final BeanNameGenerator beanNameGenerator;

    public AnnotatedBeanDependencyParser(BeanNameGenerator beanNameGenerator) {
        this.beanNameGenerator = beanNameGenerator;
    }

    private Map<String, List<BeanPropertyInfo>> parseDependenciesOfClass(Class<?> clz) {
        Objects.requireNonNull(clz, "class is null, unable to parse dependencies");
        Map<String, List<BeanPropertyInfo>> dependencies = new HashMap<>();
        Map<String, PropertyDescriptor> pdMap = BeansUtil.introspectPropertyDescriptorMap(clz);

        Field[] fields = clz.getDeclaredFields();
        for (Field f : fields) {
            // the field has a @Dependency annotation & it contains a writer method
            if (isInjectableAnnotationPresent(f.getDeclaredAnnotations())) {
                PropertyDescriptor pd = pdMap.get(f.getName());
                // if pd == null, means there is not getter and setter for this field at all
                // as long as the writerMethod is missing, this field is not injectable
                Method writeMethod;
                if (pd == null
                        || (writeMethod = pd.getWriteMethod()) == null
                        || Modifier.isPrivate(writeMethod.getModifiers())) {
                    throw new UnableToInjectDependencyException(
                            String.format("Unable to inject dependency, field: '%s' not accessible, setter method is missing or private",
                                    f.getName())
                    );
                }
                Class<?> propType = pd.getPropertyType();
                if (isPrimitiveTypes(propType)) {
                    throw new TypeNotSupportedForInjectionException(
                            String.format(
                                    "Primitive types are not supported for dependency injection, field: %s, type: %s",
                                    f.getName(),
                                    propType.getSimpleName()
                            ));
                }
                if (isCollections(propType)) {
                    throw new TypeNotSupportedForInjectionException(
                            "Collections are not supported for dependency injection, " +
                                    "field: " + f.getName());
                }
                String dependentBeanName = beanNameGenerator.generateBeanName(propType);
                // key: dependent's type, value: list of propertyInfo of this dependency type
                dependencies.computeIfAbsent(dependentBeanName, k -> new ArrayList<>());
                dependencies.get(dependentBeanName).add(new DefaultBeanPropertyInfo(f.getName(), pd));
            }
        }
        return dependencies;
    }

    @Override
    public List<DependentBeanInfo> parseDependencies(Class<?> beanClazz) {
        Map<String, List<BeanPropertyInfo>> dependencyInfo = parseDependenciesOfClass(beanClazz);
        List<DependentBeanInfo> dependentBeanInfos = new ArrayList<>();
        for (Map.Entry<String, List<BeanPropertyInfo>> entry : dependencyInfo.entrySet()) {
            dependentBeanInfos.add(new DefaultDependentBeanInfo(entry.getKey(), entry.getValue()));
        }
        return dependentBeanInfos;
    }

    /**
     * Check if the class is primitive types, such as int, Integer, etc.
     *
     * @param clz class
     */
    private boolean isPrimitiveTypes(Class<?> clz) {
        if (clz.isPrimitive()) {
            return true;
        }
        List<Class<?>> boxedTypes = Arrays.asList(
                Integer.class,
                String.class,
                Double.class,
                Float.class,
                Long.class,
                Short.class,
                Byte.class,
                Character.class
        );
        return isTypes(clz, boxedTypes);
    }

    /**
     * Check if the class is a Collection or Array
     *
     * @param clz class
     */
    private boolean isCollections(Class<?> clz) {
        if (clz.isArray()) {
            return true;
        }
        List<Class<?>> boxedTypes = Arrays.asList(Collection.class);
        return isTypes(clz, boxedTypes);
    }

    private boolean isTypes(Class<?> compared, List<Class<?>> toBeMatched) {
        if (toBeMatched.isEmpty()) {
            throw new IllegalArgumentException("These must be at least one type to be compared");
        }
        for (Class<?> target : toBeMatched) {
            if (compared.equals(target)) {
                return true;
            }
        }
        return false;
    }

    /** check if an injectable annotation is present */
    private boolean isInjectableAnnotationPresent(Annotation[] annotations) {
        for (Annotation annt : annotations) {
            // the annotation itself is a supported annotation
            if (injectableAnnotations.contains(annt.annotationType())) {
                return true;
            }

            // for composed annotation, see if this annotation has any other annotation that is supported
            for (Annotation composed : annt.annotationType().getDeclaredAnnotations()) {
                if (injectableAnnotations.contains(composed.annotationType())) {
                    return true;
                }
            }
        }
        return false;
    }
}
