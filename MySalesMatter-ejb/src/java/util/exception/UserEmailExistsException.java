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
public class UserEmailExistsException extends Exception {

    public UserEmailExistsException() {
    }

    public UserEmailExistsException(String msg) {
        super(msg);
    }
}
