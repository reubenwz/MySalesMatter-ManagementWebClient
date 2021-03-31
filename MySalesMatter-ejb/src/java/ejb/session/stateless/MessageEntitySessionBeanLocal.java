/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MessageEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ConversationNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.MessageNotFoundException;

/**
 *
 * @author Yuki
 */
@Local
public interface MessageEntitySessionBeanLocal {
    public MessageEntity createNewMessage(MessageEntity newMessageEntity, Long conversationId) throws InputDataValidationException, ConversationNotFoundException;
    List<MessageEntity> retrieveAllMessages();
    MessageEntity retrieveMessageById(Long messageId) throws MessageNotFoundException;
}
