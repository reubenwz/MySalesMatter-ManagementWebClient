/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SalesTransactionEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewTransactionException;
import util.exception.InputDataValidationException;
import util.exception.OfferNotFoundException;
import util.exception.SalesTransactionExistException;
import util.exception.SalesTransactionNotFoundException;
import util.exception.TransactionDeletionException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;

/**
 *
 * @author sylvia
 */
@Local
public interface SalesTransactionEntitySessionBeanLocal {

    public List<SalesTransactionEntity> retrieveAllTransactions();

    public SalesTransactionEntity retrieveTransactionById(Long id) throws SalesTransactionNotFoundException;

    public void deleteTransaction(Long transactionId) throws SalesTransactionNotFoundException, TransactionDeletionException;

    public void updateStatus(Long transactionId, String status) throws SalesTransactionNotFoundException;

    public SalesTransactionEntity createSalesTransaction(Long offerId, Long userId, String status, Date transactionDate, BigDecimal totalAmt) throws UnknownPersistenceException, InputDataValidationException, UserNotFoundException, CreateNewTransactionException, SalesTransactionExistException, OfferNotFoundException;

    public List<SalesTransactionEntity> getSalesTransactionByUserId(Long userId) throws UserNotFoundException;
    
}
