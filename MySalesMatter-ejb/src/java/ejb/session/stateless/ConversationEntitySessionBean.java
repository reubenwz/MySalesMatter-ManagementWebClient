/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ConversationEntity;
import entity.UserEntity;
import entity.MessageEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.ConversationNotFoundException;
import util.exception.DeleteConversationException;
import util.exception.InputDataValidationException;
import util.exception.UserNotFoundException;

/**
 *
 * @author Yuki
 */
@Stateless
public class ConversationEntitySessionBean implements ConversationEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MySalesMatter-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @EJB
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    @EJB
    private MessageEntitySessionBeanLocal messageEntitySessionBeanLocal;

    public ConversationEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public ConversationEntity createNewConversation(ConversationEntity newConversationEntity, Long offererUserId, Long offereeUserId, List<MessageEntity> messageList) throws UserNotFoundException, InputDataValidationException {
        newConversationEntity.setMessages(messageList);
        try {
            UserEntity offerer = userEntitySessionBeanLocal.retrieveUserById(offererUserId);
            UserEntity offeree = userEntitySessionBeanLocal.retrieveUserById(offereeUserId);

            newConversationEntity.setOfferee(offeree);
            newConversationEntity.setOfferer(offerer);
            //offeree.getConversationsAsOfferee().add(newConversationEntity);
            //offerer.getConversationsAsOfferer().add(newConversationEntity);

            Set<ConstraintViolation<ConversationEntity>> constraintViolations = validator.validate(newConversationEntity);

            if (constraintViolations.isEmpty()) {
                em.persist(newConversationEntity);
                em.merge(offerer);
                em.merge(offeree);
                em.flush();
                return newConversationEntity;
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("User does not exists!");
        }
    }
    
    @Override
    public ConversationEntity createNewEmptyConversation(Long offererId, Long offereeId) throws UserNotFoundException {
        ConversationEntity newConversationEntity = new ConversationEntity();
        UserEntity offeree = userEntitySessionBeanLocal.retrieveUserById(offereeId);
        UserEntity offerer = userEntitySessionBeanLocal.retrieveUserById(offererId);
        newConversationEntity.setOfferee(offeree);
        newConversationEntity.setOfferer(offerer);
        //offeree.getConversationsAsOfferee().add(newConversationEntity);
        //offerer.getConversationsAsOfferer().add(newConversationEntity);
        
        em.persist(newConversationEntity);
        return newConversationEntity;
    }
    
    @Override
    public List<ConversationEntity> retrieveConverationsByUser(Long userId) throws UserNotFoundException {
        List<ConversationEntity> conversations = new ArrayList<>();
        UserEntity user = userEntitySessionBeanLocal.retrieveUserById(userId);
        //conversations.addAll(user.getConversationsAsOfferee());
        //conversations.addAll(user.getConversationsAsOfferer());

        try {
            for (ConversationEntity c : conversations) {
                c.getMessages().size();
            }
            return conversations;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new UserNotFoundException("Conversation with user :" + userId + " does not exist");
        }
    }

    @Override
    public ConversationEntity retrieveConverationByOffereeName(UserEntity offeree) throws UserNotFoundException {
        Query query = em.createQuery("SELECT c FROM ConversationEntity c WHERE c.offeree:inOfferee");
        query.setParameter("inOfferee", offeree);

        try {
            ConversationEntity result = (ConversationEntity) query.getSingleResult();
            result.getMessages().size();
            return result;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new UserNotFoundException("Conversation with :" + offeree + " does not exist");
        }
    }

    @Override
    public ConversationEntity retrieveConverationByOffererName(UserEntity offerer) throws UserNotFoundException {
        Query query = em.createQuery("SELECT c FROM ConversationEntity c WHERE c.offerer:inOfferer");
        query.setParameter("inOfferer", offerer);

        try {
            ConversationEntity result = (ConversationEntity) query.getSingleResult();
            result.getMessages().size();
            return result;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new UserNotFoundException("Conversation with :" + offerer + " does not exist");
        }
    }

    @Override
    public ConversationEntity retrieveConversationById(Long id) throws ConversationNotFoundException {
        ConversationEntity conversationEntity = em.find(ConversationEntity.class, id);

        if (conversationEntity != null) {
            conversationEntity.getMessages().size();
            return conversationEntity;
        } else {
            throw new ConversationNotFoundException("Conversation with this id, " + id + ", does not exist!");
        }
    }

    @Override
    public void deleteConversation(Long conversationId) throws ConversationNotFoundException, DeleteConversationException {
        ConversationEntity conversationEntityToRemove = retrieveConversationById(conversationId);

        if (conversationEntityToRemove.getMessages().isEmpty() || conversationEntityToRemove.getOfferee() == null || conversationEntityToRemove.getOfferer() == null) {
            em.remove(conversationEntityToRemove);
        } else {
            throw new DeleteConversationException("Conversation ID " + conversationId + " is associatied with existing user(s) and cannot be deleted!");
        }
    }

    @Override
    public ConversationEntity searchConversationByOffereeName(UserEntity offeree) {
        Query query = em.createQuery("SELECT c FROM ConversationEntity c WHERE LOWER(c.offeree) LIKE :inOfferee ORDER BY c.conversationId ASC");
        query.setParameter("inOfferee", offeree);

        ConversationEntity conversationEntities = (ConversationEntity) query.getSingleResult();
        conversationEntities.getMessages().size();
      
        return conversationEntities;
    }

    @Override
    public ConversationEntity searchConversationByOffererName(UserEntity offerer) {
        Query query = em.createQuery("SELECT c FROM ConversationEntity c WHERE LOWER(c.offerer) LIKE :inOfferer ORDER BY c.conversationId ASC");
        query.setParameter("inOfferer", offerer);

        ConversationEntity conversationEntities = (ConversationEntity) query.getSingleResult();
        conversationEntities.getMessages().size();
      
        return conversationEntities;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ConversationEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
