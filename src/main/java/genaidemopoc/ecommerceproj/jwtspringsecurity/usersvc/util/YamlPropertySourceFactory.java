package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.util;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.List;

/**
 * Factory for creating a PropertySource from a YAML file.
 * This allows @PropertySource annotation to work with YAML files.
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) throws IOException {
        String resourceName = name != null ? name : encodedResource.getResource().getFilename();
        List<PropertySource<?>> propertySourceList = new YamlPropertySourceLoader()
                .load(resourceName, encodedResource.getResource());
        
        if (propertySourceList.isEmpty()) {
            return null;
        }
        
        return propertySourceList.get(0);
    }
} 