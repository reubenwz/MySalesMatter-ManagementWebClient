package entity;

import entity.MessageEntity;
import entity.UserEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-04-09T11:53:30")
@StaticMetamodel(ConversationEntity.class)
public class ConversationEntity_ { 

    public static volatile SingularAttribute<ConversationEntity, UserEntity> offeree;
    public static volatile SingularAttribute<ConversationEntity, Long> conversationId;
    public static volatile SingularAttribute<ConversationEntity, UserEntity> offerer;
    public static volatile ListAttribute<ConversationEntity, MessageEntity> messages;

}