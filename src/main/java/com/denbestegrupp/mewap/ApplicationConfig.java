package com.denbestegrupp.mewap;




import java.util.Set;
import javax.ws.rs.core.Application;

/**
 * Technical class needed by JAX-RS
 * @author hajo
 */
@javax.ws.rs.ApplicationPath("")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.denbestegrupp.mewap.auth.AuthResource.class);
        resources.add(com.denbestegrupp.mewap.model.EventListResource.class);
    }
    
}
