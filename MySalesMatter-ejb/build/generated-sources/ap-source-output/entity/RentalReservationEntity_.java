package entity;

import entity.ListingEntity;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import util.enumeration.ReservationStatus;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-04-12T18:23:58")
@StaticMetamodel(RentalReservationEntity.class)
public class RentalReservationEntity_ { 

    public static volatile SingularAttribute<RentalReservationEntity, Date> endDate;
    public static volatile SingularAttribute<RentalReservationEntity, BigDecimal> totalPrice;
    public static volatile SingularAttribute<RentalReservationEntity, Long> rentalReservationId;
    public static volatile SingularAttribute<RentalReservationEntity, ListingEntity> listing;
    public static volatile SingularAttribute<RentalReservationEntity, String> issues;
    public static volatile SingularAttribute<RentalReservationEntity, Date> startDate;
    public static volatile SingularAttribute<RentalReservationEntity, ReservationStatus> reservationStatus;

}