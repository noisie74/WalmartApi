package mikhail.com.walmartapi.module;

import javax.inject.Singleton;

import dagger.Component;
import mikhail.com.walmartapi.api.WalmartAPI;

/**
 * Created by Mikhail on 6/25/16.
 */
@Singleton
@Component(modules = {ApiModule.class})
public interface WalmartComponent {
    WalmartAPI.WalmartApiRx provideWalmartService();

}