/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author sylvia
 */
public class TransactionDeletionException extends Exception {

    /**
     * Creates a new instance of <code>TransactionDeletionException</code>
     * without detail message.
     */
    public TransactionDeletionException() {
    }

    /**
     * Constructs an instance of <code>TransactionDeletionException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public TransactionDeletionException(String msg) {
        super(msg);
    }
}
