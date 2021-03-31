/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ConversationEntity;
import entity.MessageEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.ConversationNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.MessageNotFoundException;

/**
 *
 * @author Yuki
 */
@Stateless
public class MessageEntitySessionBean implements MessageEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MySalesMatter-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    @EJB
    ConversationEntitySessionBeanLocal conversationEntitySessionBeanLocal;

    public MessageEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public MessageEntity createNewMessage(MessageEntity newMessageEntity, Long conversationId) throws InputDataValidationException, ConversationNotFoundException {
        try {
            ConversationEntity conversation = conversationEntitySessionBeanLocal.retrieveConversationById(conversationId);
            conversation.getMessages().add(newMessageEntity);
        
        Set<ConstraintViolation<MessageEntity>> constraintViolations = validator.validate(newMessageEntity);
        
        if (constraintViolations.isEmpty()) {
            em.persist(newMessageEntity);
            em.merge(conversation);
            em.flush();
            
            return newMessageEntity;
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
        } catch (ConversationNotFoundException ex) {
            throw new ConversationNotFoundException("Conversation does not exist");
        }
    }
    
    @Override
    public List<MessageEntity> retrieveAllMessages() {
        Query query = em.createQuery("SELECT m FROM MessageEntity m ORDER BY m.sentDate ASC");
        
        return query.getResultList();
    }
    
    public MessageEntity retrieveMessageById(Long messageId) throws MessageNotFoundException {
        MessageEntity message = em.find(MessageEntity.class, messageId);
        if (message != null) {
            return message;
        } else {
            throw new MessageNotFoundException("Message does not exist!");
        }
    }
   

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<MessageEntity>> constraintViolations) {
        String msg = "Input data validation error!:";
        
        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    

    
}
