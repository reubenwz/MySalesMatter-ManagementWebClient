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
public class SalesTransactionNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>TransactionNotFoundException</code>
     * without detail message.
     */
    public SalesTransactionNotFoundException() {
    }

    /**
     * Constructs an instance of <code>TransactionNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public SalesTransactionNotFoundException(String msg) {
        super(msg);
    }
}
