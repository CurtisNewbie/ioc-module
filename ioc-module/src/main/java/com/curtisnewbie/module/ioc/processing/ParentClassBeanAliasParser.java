package com.curtisnewbie.module.ioc.processing;

import com.curtisnewbie.module.ioc.annotations.MBean;
import com.curtisnewbie.module.ioc.exceptions.TypeNotSupportedForInjectionException;

import java.util.*;

import static java.lang.String.format;

/**
 * Implementation of {@link BeanAliasParser}
 * <p>
 * The parent classes (including interfaces) are treated 'aliases'
 * </p>
 *
 * @author yongjie.zhuang
 */
public class ParentClassBeanAliasParser implements BeanAliasParser {

    private final BeanNameGenerator beanNameGenerator;

    public ParentClassBeanAliasParser(BeanNameGenerator beanNameGenerator) {
        this.beanNameGenerator = beanNameGenerator;
    }

    @Override
    public Set<String> parseBeanAliases(Class<?> clazz) {
        Set<String> beanAlias = new HashSet<>();

        Set<Class<?>> clzSeen = new HashSet<>();
        Queue<Class<?>> clzToBeAdded = new LinkedList<>();

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            clzToBeAdded.add(superClass);
        }
        clzToBeAdded.addAll(Arrays.asList(clazz.getInterfaces()));

        while (!clzToBeAdded.isEmpty()) {
            Class<?> c = clzToBeAdded.poll();

            // have seen this class already
            if (clzSeen.contains(c))
                continue;
            clzSeen.add(c);

            // actual class instead of interface
            if (!c.isInterface() && (superClass = c.getSuperclass()) != null) {
                clzToBeAdded.add(superClass);
            }

            Class<?>[] interfaces = c.getInterfaces();
            if (interfaces != null && interfaces.length > 0) {
                clzToBeAdded.addAll(Arrays.asList(interfaces));
            }
        }

        for (Class<?> ic : clzSeen) {
            String interfaceName = beanNameGenerator.generateBeanName(ic);
            beanAlias.add(interfaceName);
        }
        return beanAlias;
    }
}
