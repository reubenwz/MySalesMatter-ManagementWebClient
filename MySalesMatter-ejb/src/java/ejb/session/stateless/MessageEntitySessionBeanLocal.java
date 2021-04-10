/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MessageEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.MessageNotFoundException;
import util.exception.OfferNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;

/**
 *
 * @author Yuki
 */
@Local
public interface MessageEntitySessionBeanLocal {

    List<MessageEntity> retrieveAllMessages();

    public List<MessageEntity> retrieveReceivedMessageSByUserId(Long userId) throws MessageNotFoundException;

    public Long addMessage(String message, Long offerId, Long senderId, Date date) throws UnknownPersistenceException, OfferNotFoundException, UserNotFoundException, InputDataValidationException;
    public Long addMessageV2(String message, Long offerId, Long senderId, Long recipientId, Date date) throws UnknownPersistenceException, OfferNotFoundException, UserNotFoundException, InputDataValidationException;
}
