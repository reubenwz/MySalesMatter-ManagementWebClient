package entity;

import entity.ListingEntity;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-04-02T12:05:23")
@StaticMetamodel(NotificationEntity.class)
public class NotificationEntity_ { 

    public static volatile SingularAttribute<NotificationEntity, Date> expiryDate;
    public static volatile ListAttribute<NotificationEntity, ListingEntity> listings;
    public static volatile SingularAttribute<NotificationEntity, Long> notificationId;
    public static volatile SingularAttribute<NotificationEntity, String> title;
    public static volatile SingularAttribute<NotificationEntity, String> content;
    public static volatile SingularAttribute<NotificationEntity, Date> postedDate;

}