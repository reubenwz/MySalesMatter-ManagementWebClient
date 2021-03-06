/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OfferEntitySessionBeanLocal;
import ejb.session.stateless.SalesTransactionEntitySessionBeanLocal;
import entity.OfferEntity;
import entity.SalesTransactionEntity;
import entity.UserEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import util.enumeration.OfferType;
import util.exception.SalesTransactionNotFoundException;
import util.exception.UserNotFoundException;

/**
 *
 * @author sylvia
 */
@Named(value = "viewAllSalesTransactionManagedBean")
@ViewScoped
public class ViewAllSalesTransactionManagedBean implements Serializable {

    @EJB
    private OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;

    private List<SalesTransactionEntity> salesTransactionNotCompleted;
    private List<SalesTransactionEntity> salesTransactionCompleted;
    private UserEntity currentUser;

    @EJB
    private SalesTransactionEntitySessionBeanLocal salesTransactionEntitySessionBeanLocal;

    public ViewAllSalesTransactionManagedBean() {
        currentUser = new UserEntity();
        salesTransactionNotCompleted = new ArrayList<>();
        salesTransactionCompleted = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        try {
            currentUser = (UserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
            List<SalesTransactionEntity> transactions = salesTransactionEntitySessionBeanLocal.getSalesTransactionByUserId(currentUser.getUserId());
            List<SalesTransactionEntity> newSalesTransactionCompleted = new ArrayList<>();
            List<SalesTransactionEntity> newSalesTransactionNotCompleted = new ArrayList<>();

            for (SalesTransactionEntity s : newSalesTransactionCompleted) {
                salesTransactionCompleted.add(salesTransactionEntitySessionBeanLocal.retrieveTransactionById(s.getSalesTransactionId()));
            }
            for (SalesTransactionEntity s : newSalesTransactionNotCompleted) {
                salesTransactionNotCompleted.add(salesTransactionEntitySessionBeanLocal.retrieveTransactionById(s.getSalesTransactionId()));
            }
        } catch (SalesTransactionNotFoundException | UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while loading conversations: " + ex.getMessage(), null));
        }

    }

    public List<SalesTransactionEntity> retrieveSalesTransactionCompleted() {
        List<SalesTransactionEntity> transactions = new ArrayList<>();
        try {
            transactions = salesTransactionEntitySessionBeanLocal.getSalesTransactionByUserId(currentUser.getUserId());

        } catch (UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while loading conversations: " + ex.getMessage(), null));
        }
        return transactions;
    }

    public List<OfferEntity> retrievePurchases() {
        List<OfferEntity> purchase = offerEntitySessionBeanLocal.retrieveOffersByUserId(currentUser.getUserId());
        List<OfferEntity> allPurchases = new ArrayList<>();
        for (OfferEntity o : purchase) {
            if (o.getOfferType() == OfferType.BUY && o.isPaid()) {
                allPurchases.add(o);
            }
        }
        return allPurchases;
    }

    public List<SalesTransactionEntity> getSalesTransactionNotCompleted() {
        return salesTransactionNotCompleted;
    }

    public void setSalesTransactionNotCompleted(List<SalesTransactionEntity> salesTransactionNotCompleted) {
        this.salesTransactionNotCompleted = salesTransactionNotCompleted;
    }

    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    public List<SalesTransactionEntity> getSalesTransactionCompleted() {
        return salesTransactionCompleted;
    }

    public void setSalesTransactionCompleted(List<SalesTransactionEntity> salesTransactionCompleted) {
        this.salesTransactionCompleted = salesTransactionCompleted;
    }

}
