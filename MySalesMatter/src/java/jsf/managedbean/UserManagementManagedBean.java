/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.SalesTransactionEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.UserEntity;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FlowEvent;
import static org.primefaces.shaded.commons.io.IOUtils.skip;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UserEmailExistsException;

/**
 *
 * @author Yuki
 */
@Named(value = "userManagementManagedBean")
@ViewScoped
public class UserManagementManagedBean implements Serializable {

    @EJB(name = "UserEntitySessionBeanLocal")
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    @EJB(name = "SalesTransactionEntitySessionBeanLocal")
    private SalesTransactionEntitySessionBeanLocal salesTransactionEntitySessionBeanLocal;

    private UserEntity user;
    private boolean skip;

    public UserManagementManagedBean() {
        user = new UserEntity();
    }

    @PostConstruct
    public void postConstruct() {

    }

    public void registerNewUser(ActionEvent event) {
        try {
            setUser(userEntitySessionBeanLocal.registerUser(getUser()));

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New user created successfully (User ID: " + getUser().getUserId() + ")", null));

            setUser(new UserEntity());

        } catch (InputDataValidationException | UnknownPersistenceException | UserEmailExistsException ex) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new user: " + ex.getMessage(), null));
        }
    }

    public String onFlowProcess(FlowEvent event) {
        if (skip) {
            skip = false;	//reset in case user goes back
            return "confirm";
        } else {
            return event.getNewStep();
        }
    }

    /**
     * @return the user
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

}
