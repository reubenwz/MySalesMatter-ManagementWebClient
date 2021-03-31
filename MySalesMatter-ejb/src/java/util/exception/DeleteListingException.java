/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author reuben
 */
public class DeleteListingException extends Exception {

    /**
     * Creates a new instance of <code>DeleteListingException</code> without
     * detail message.
     */
    public DeleteListingException() {
    }

    /**
     * Constructs an instance of <code>DeleteListingException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteListingException(String msg) {
        super(msg);
    }
}
