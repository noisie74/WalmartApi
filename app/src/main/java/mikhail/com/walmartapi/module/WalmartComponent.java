package mikhail.com.walmartapi.module;

import javax.inject.Singleton;

import dagger.Component;
import mikhail.com.walmartapi.api.WalmartAPI;

/**
 * api component
 */
@Singleton
@Component(modules = {ApiModule.class})
public interface WalmartComponent {
    WalmartAPI provideWalmartService();

}
