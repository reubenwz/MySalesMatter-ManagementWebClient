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
public class SalesTransactionExistException extends Exception {

    /**
     * Creates a new instance of <code>SalesTransactionExistException</code>
     * without detail message.
     */
    public SalesTransactionExistException() {
    }

    /**
     * Constructs an instance of <code>SalesTransactionExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SalesTransactionExistException(String msg) {
        super(msg);
    }
}
