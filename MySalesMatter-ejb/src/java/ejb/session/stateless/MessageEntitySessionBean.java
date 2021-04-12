/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MessageEntity;
import entity.OfferEntity;
import entity.UserEntity;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.MessageNotFoundException;
import util.exception.OfferNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;

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
    UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    @EJB
    OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;

    public MessageEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public List<MessageEntity> retrieveAllMessages() {
        Query query = em.createQuery("SELECT m FROM MessageEntity m ORDER BY m.sentDate ASC");

        return query.getResultList();
    }

    @Override
    public MessageEntity addMessage(String message, Long offerId, Long senderId, Date date) throws UnknownPersistenceException, OfferNotFoundException, UserNotFoundException, InputDataValidationException {
        try {
            UserEntity send = userEntitySessionBeanLocal.retrieveUserById(senderId);
            OfferEntity o = offerEntitySessionBeanLocal.retrieveOfferById(offerId);
            MessageEntity m = new MessageEntity();
            m.setMessage(message);
            m.setSender(send);
            m.setSentDate(date);
            m.setRecipient(o.getListing().getUser());
            m.setOffer(o);
            Set<ConstraintViolation<MessageEntity>> constraintViolations = validator.validate(m);
            if (constraintViolations.isEmpty()) {
                try {
                    em.persist(m);
                    o.getMessage().add(m);
                    em.merge(o);
                    em.flush();
                    return m;
                } catch (PersistenceException ex) {
                    throw new UnknownPersistenceException(ex.getMessage());

                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }

        } catch (OfferNotFoundException ex) {
            throw new OfferNotFoundException("Offer with this id, " + offerId + ", does not exist!");
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("Sender with this id, " + senderId + ", does not exist!");
        }

    }
    
    @Override
    public MessageEntity addMessageV2(String message, Long offerId, Long senderId, Long recipientId, Date date) throws UnknownPersistenceException, OfferNotFoundException, UserNotFoundException, InputDataValidationException {
        try {
            UserEntity recipient = userEntitySessionBeanLocal.retrieveUserById(recipientId);
            UserEntity sender = userEntitySessionBeanLocal.retrieveUserById(senderId);
            OfferEntity o = offerEntitySessionBeanLocal.retrieveOfferById(offerId);
            MessageEntity m = new MessageEntity();
            m.setMessage(message);
            m.setSender(sender);
            m.setSentDate(date);
            m.setRecipient(recipient);
            m.setOffer(o);
            Set<ConstraintViolation<MessageEntity>> constraintViolations = validator.validate(m);
            if (constraintViolations.isEmpty()) {
                try {
                    em.persist(m);
                    o.getMessage().add(m);
                    em.merge(o);
                    em.flush();
                    return m;
                } catch (PersistenceException ex) {
                    throw new UnknownPersistenceException(ex.getMessage());

                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }

        } catch (OfferNotFoundException ex) {
            throw new OfferNotFoundException("Offer with this id, " + offerId + ", does not exist!");
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("Sender with this id, " + senderId + ", does not exist!");
        }

    }

    @Override
    public List<MessageEntity> retrieveReceivedMessagesByUserId(Long userId) throws MessageNotFoundException {
        Query query = em.createQuery("SELECT m FROM MessageEntity m WHERE m.recipient.userId =:inUserId");
        query.setParameter("inUserId", userId);
        return query.getResultList();
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<MessageEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
