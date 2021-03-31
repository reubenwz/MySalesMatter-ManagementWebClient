/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author rtan3
 */
public class CreateNewOfferException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewOfferException</code> without
     * detail message.
     */
    public CreateNewOfferException() {
    }

    /**
     * Constructs an instance of <code>CreateNewOfferException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewOfferException(String msg) {
        super(msg);
    }
}
