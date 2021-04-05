package entity;

import entity.CategoryEntity;
import entity.NotificationEntity;
import entity.OfferEntity;
import entity.RentalReservationEntity;
import entity.ReviewEntity;
import entity.TagEntity;
import entity.UserEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-04-05T09:32:48")
@StaticMetamodel(ListingEntity.class)
public class ListingEntity_ { 

    public static volatile SingularAttribute<ListingEntity, BigDecimal> rentalPrice;
    public static volatile SingularAttribute<ListingEntity, String> picturePath;
    public static volatile ListAttribute<ListingEntity, OfferEntity> offers;
    public static volatile SingularAttribute<ListingEntity, Date> dateListed;
    public static volatile SingularAttribute<ListingEntity, BigDecimal> salePrice;
    public static volatile SingularAttribute<ListingEntity, String> description;
    public static volatile SingularAttribute<ListingEntity, Long> listingId;
    public static volatile ListAttribute<ListingEntity, TagEntity> tags;
    public static volatile SingularAttribute<ListingEntity, List> reservedDates;
    public static volatile ListAttribute<ListingEntity, ReviewEntity> reviews;
    public static volatile ListAttribute<ListingEntity, RentalReservationEntity> reservations;
    public static volatile SingularAttribute<ListingEntity, String> name;
    public static volatile SingularAttribute<ListingEntity, String> location;
    public static volatile SingularAttribute<ListingEntity, CategoryEntity> category;
    public static volatile SingularAttribute<ListingEntity, String> brand;
    public static volatile SingularAttribute<ListingEntity, UserEntity> user;
    public static volatile SingularAttribute<ListingEntity, Boolean> forSaleAvailability;
    public static volatile SingularAttribute<ListingEntity, Boolean> isRentOut;
    public static volatile ListAttribute<ListingEntity, NotificationEntity> notifications;
    public static volatile SingularAttribute<ListingEntity, Integer> likes;
    public static volatile SingularAttribute<ListingEntity, Boolean> rentalAvailability;

}