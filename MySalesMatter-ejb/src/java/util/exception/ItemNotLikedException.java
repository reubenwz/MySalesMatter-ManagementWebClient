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
public class ItemNotLikedException extends Exception {

    /**
     * Creates a new instance of <code>ItemNotLikedException</code> without
     * detail message.
     */
    public ItemNotLikedException() {
    }

    /**
     * Constructs an instance of <code>ItemNotLikedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ItemNotLikedException(String msg) {
        super(msg);
    }
}
