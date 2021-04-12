package entity;

import entity.ListingEntity;
import entity.MessageEntity;
import entity.SalesTransactionEntity;
import entity.UserEntity;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import util.enumeration.OfferType;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-04-12T10:51:58")
@StaticMetamodel(OfferEntity.class)
public abstract class OfferEntity_ { 

    public static volatile SingularAttribute<OfferEntity, OfferType> offerType;
    public static volatile SingularAttribute<OfferEntity, BigDecimal> totalPrice;
    public static volatile SingularAttribute<OfferEntity, Date> offerDate;
    public static volatile SingularAttribute<OfferEntity, Boolean> paid;
    public static volatile SingularAttribute<OfferEntity, Long> offerId;
    public static volatile SingularAttribute<OfferEntity, Boolean> accepted;
    public static volatile SingularAttribute<OfferEntity, ListingEntity> listing;
    public static volatile ListAttribute<OfferEntity, MessageEntity> message;
    public static volatile SingularAttribute<OfferEntity, UserEntity> user;
    public static volatile SingularAttribute<OfferEntity, SalesTransactionEntity> sales;

}