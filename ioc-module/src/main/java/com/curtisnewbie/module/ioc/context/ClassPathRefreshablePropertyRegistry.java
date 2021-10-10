package com.curtisnewbie.module.ioc.context;

import com.curtisnewbie.module.ioc.util.ClassLoaderHolder;
import com.curtisnewbie.module.ioc.util.ReflectionsScanUtil;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Default implementation of {@link PropertyRegistry}
 * <p>
 * This class implements {@link #refresh()}} by using Reflections library to scan the class path for files that match
 * the pattern: {@code .*.properties}, and load all the key-value pairs to the registry.
 * </p>
 *
 * @author yongjie.zhuang
 * @see com.curtisnewbie.module.ioc.processing.PropertyValueBeanPostProcessor
 * @see com.curtisnewbie.module.ioc.convert.Converters
 */
public class ClassPathRefreshablePropertyRegistry extends AbstractPropertyRegistry implements PropertyRegistry, RefreshableRegistry {

    private final String ROOT_PATH = "";
    private final Pattern propertiesFilePattern = Pattern.compile(".*\\.properties");
    private final ClassLoader cl = ClassLoaderHolder.getClassLoader();

    @Override
    public void refresh() {
        synchronized (this.propertyValues) {
            Reflections reflections = ReflectionsScanUtil.getReflectionsForResourcesScanning(ROOT_PATH, cl);
            Set<String> relPaths = reflections.getResources(propertiesFilePattern);
            for (String p : relPaths) {
                // load each properties file
                Properties properties = new Properties();
                try {
                    properties.load(cl.getResourceAsStream(p));
                } catch (IOException e) {
                    throw new IllegalStateException(p, e);
                }
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    this.propertyValues.put((String) entry.getKey(), (String) entry.getValue());
                }
            }
        }
    }
}
