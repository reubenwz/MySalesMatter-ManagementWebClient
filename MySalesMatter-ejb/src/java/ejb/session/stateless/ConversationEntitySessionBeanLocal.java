/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ConversationEntity;
import entity.UserEntity;
import entity.MessageEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ConversationNotFoundException;
import util.exception.DeleteConversationException;
import util.exception.InputDataValidationException;
import util.exception.UserNotFoundException;

/**
 *
 * @author Yuki
 */
@Local
public interface ConversationEntitySessionBeanLocal {
    public ConversationEntity createNewConversation(ConversationEntity newConversationEntity, Long offererUserId, Long offereeUserId, List<MessageEntity> messageList) throws UserNotFoundException, InputDataValidationException;
    ConversationEntity retrieveConverationByOffereeName(UserEntity offeree) throws UserNotFoundException;
    ConversationEntity retrieveConverationByOffererName(UserEntity offerer) throws UserNotFoundException;
    ConversationEntity retrieveConversationById(Long id) throws ConversationNotFoundException;
    void deleteConversation(Long conversationId) throws ConversationNotFoundException, DeleteConversationException;
    ConversationEntity searchConversationByOffereeName(UserEntity offeree);
    ConversationEntity searchConversationByOffererName(UserEntity offerer);
    List<ConversationEntity> retrieveConverationsByUser(Long userId) throws UserNotFoundException;
    ConversationEntity createNewEmptyConversation(Long offererId, Long offereeId) throws UserNotFoundException;
}
