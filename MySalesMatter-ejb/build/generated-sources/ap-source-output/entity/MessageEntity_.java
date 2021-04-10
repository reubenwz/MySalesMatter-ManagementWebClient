package entity;

import entity.OfferEntity;
import entity.UserEntity;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-04-10T11:52:17")
@StaticMetamodel(MessageEntity.class)
public class MessageEntity_ { 

    public static volatile SingularAttribute<MessageEntity, OfferEntity> offer;
    public static volatile SingularAttribute<MessageEntity, Date> sentDate;
    public static volatile SingularAttribute<MessageEntity, UserEntity> sender;
    public static volatile SingularAttribute<MessageEntity, UserEntity> recipient;
    public static volatile SingularAttribute<MessageEntity, Long> messageId;
    public static volatile SingularAttribute<MessageEntity, String> message;

}