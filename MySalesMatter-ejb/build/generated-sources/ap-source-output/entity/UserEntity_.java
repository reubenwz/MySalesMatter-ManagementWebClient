package entity;

import entity.LikedItemEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import entity.ReviewEntity;
import entity.SalesTransactionEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-04-12T14:05:43")
@StaticMetamodel(UserEntity.class)
public class UserEntity_ { 

    public static volatile SingularAttribute<UserEntity, String> picturePath;
    public static volatile ListAttribute<UserEntity, OfferEntity> offers;
    public static volatile SingularAttribute<UserEntity, String> bio;
    public static volatile ListAttribute<UserEntity, SalesTransactionEntity> transactions;
    public static volatile ListAttribute<UserEntity, LikedItemEntity> likedItems;
    public static volatile SingularAttribute<UserEntity, Long> userId;
    public static volatile ListAttribute<UserEntity, ListingEntity> listings;
    public static volatile SingularAttribute<UserEntity, String> password;
    public static volatile SingularAttribute<UserEntity, String> phoneNumber;
    public static volatile ListAttribute<UserEntity, ReviewEntity> reviews;
    public static volatile SingularAttribute<UserEntity, String> name;
    public static volatile SingularAttribute<UserEntity, String> bankAccountNumber;
    public static volatile SingularAttribute<UserEntity, String> email;
    public static volatile SingularAttribute<UserEntity, String> username;

}