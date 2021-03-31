/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Yuki
 */
public class DeleteConversationException extends Exception {

    public DeleteConversationException() {
    }

    public DeleteConversationException(String msg) {
        super(msg);
    }
}
