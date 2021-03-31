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
public class CreateNewRentalReservationException extends Exception {

    /**
     * Creates a new instance of
     * <code>CreateNewRentalReservationException</code> without detail message.
     */
    public CreateNewRentalReservationException() {
    }

    /**
     * Constructs an instance of
     * <code>CreateNewRentalReservationException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewRentalReservationException(String msg) {
        super(msg);
    }
}
