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
public class DeleteOfferException extends Exception {

    /**
     * Creates a new instance of <code>DeleteOfferException</code> without
     * detail message.
     */
    public DeleteOfferException() {
    }

    /**
     * Constructs an instance of <code>DeleteOfferException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteOfferException(String msg) {
        super(msg);
    }
}
