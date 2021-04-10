package entity;

import entity.ListingEntity;
import entity.UserEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-04-10T12:42:47")
@StaticMetamodel(LikedItemEntity.class)
public class LikedItemEntity_ { 

    public static volatile SingularAttribute<LikedItemEntity, Long> likedItemId;
    public static volatile SingularAttribute<LikedItemEntity, ListingEntity> listing;
    public static volatile SingularAttribute<LikedItemEntity, UserEntity> user;

}