package entity;

import entity.ListingEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-04-11T10:42:47")
@StaticMetamodel(TagEntity.class)
public class TagEntity_ { 

    public static volatile ListAttribute<TagEntity, ListingEntity> listings;
    public static volatile SingularAttribute<TagEntity, Long> tagId;
    public static volatile SingularAttribute<TagEntity, String> name;

}