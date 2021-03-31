package entity;

import entity.ListingEntity;
import entity.SalesTransactionEntity;
import entity.UserEntity;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import util.enumeration.OfferType;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-03-28T23:01:26")
@StaticMetamodel(OfferEntity.class)
public abstract class OfferEntity_ { 

    public static volatile SingularAttribute<OfferEntity, OfferType> offerType;
    public static volatile SingularAttribute<OfferEntity, BigDecimal> totalPrice;
    public static volatile SingularAttribute<OfferEntity, Date> offerDate;
    public static volatile SingularAttribute<OfferEntity, Long> offerId;
    public static volatile SingularAttribute<OfferEntity, Boolean> accepted;
    public static volatile SingularAttribute<OfferEntity, ListingEntity> listing;
    public static volatile SingularAttribute<OfferEntity, UserEntity> user;
    public static volatile SingularAttribute<OfferEntity, SalesTransactionEntity> sales;

}