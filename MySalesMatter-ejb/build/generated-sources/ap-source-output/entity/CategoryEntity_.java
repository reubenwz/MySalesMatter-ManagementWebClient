package entity;

import entity.CategoryEntity;
import entity.ListingEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-03-31T17:19:01")
@StaticMetamodel(CategoryEntity.class)
public class CategoryEntity_ { 

    public static volatile ListAttribute<CategoryEntity, ListingEntity> listings;
    public static volatile ListAttribute<CategoryEntity, CategoryEntity> subCategoryEntities;
    public static volatile SingularAttribute<CategoryEntity, CategoryEntity> parentCategoryEntity;
    public static volatile SingularAttribute<CategoryEntity, String> name;
    public static volatile SingularAttribute<CategoryEntity, String> description;
    public static volatile SingularAttribute<CategoryEntity, Long> categoryId;

}