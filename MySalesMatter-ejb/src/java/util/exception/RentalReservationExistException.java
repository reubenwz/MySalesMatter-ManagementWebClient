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
public class RentalReservationExistException extends Exception {

    /**
     * Creates a new instance of <code>RentalReservationExistException</code>
     * without detail message.
     */
    public RentalReservationExistException() {
    }

    /**
     * Constructs an instance of <code>RentalReservationExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RentalReservationExistException(String msg) {
        super(msg);
    }
}
