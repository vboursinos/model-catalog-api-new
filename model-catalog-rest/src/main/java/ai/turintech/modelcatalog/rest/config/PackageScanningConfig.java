package ai.turintech.modelcatalog.rest.config;

import ai.turintech.modelcatalog.callable.ModelCatalogCallablePackage;
import ai.turintech.modelcatalog.dtoentitymapper.ModelCatalogDtoEntityMapperPackage;
import ai.turintech.modelcatalog.entity.ModelCatalogEntityPackage;
import ai.turintech.modelcatalog.facade.ModelPackageFacadePackage;
import ai.turintech.modelcatalog.repository.ModelCatalogRepositoryPackage;
import ai.turintech.modelcatalog.rest.ModelCatalogRestPackage;
import ai.turintech.modelcatalog.service.ModelCatalogServicePackage;
import ai.turintech.modelcatalog.todtomapper.ModelCatalogToDtoMapperPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackageClasses = {ModelCatalogRestPackage.class,
        ModelCatalogToDtoMapperPackage.class,
        ModelPackageFacadePackage.class,
        ModelCatalogServicePackage.class,
        ModelCatalogDtoEntityMapperPackage.class,
        ModelCatalogRepositoryPackage.class,
        ModelCatalogEntityPackage.class,
        ModelCatalogCallablePackage.class})
@EnableJpaRepositories(basePackageClasses = {ModelCatalogRepositoryPackage.class})
@EntityScan(basePackageClasses = {ModelCatalogEntityPackage.class})
public class PackageScanningConfig {

}
