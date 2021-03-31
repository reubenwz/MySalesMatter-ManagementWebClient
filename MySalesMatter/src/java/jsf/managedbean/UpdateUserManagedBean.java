/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.UserEntity;
import java.io.Serializable;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.InputDataValidationException;
import util.exception.UpdateUserException;
import util.exception.UserNotFoundException;

/**
 *
 * @author Yuki
 */
@Named(value = "updateUserManagedBean")
@ViewScoped
public class UpdateUserManagedBean implements Serializable {

    @EJB(name = "UserEntitySessionBeanLocal")
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    private UserEntity currentUser;
    private UserEntity userToUpdate;
    private String updatedPassword;

    public UpdateUserManagedBean() {
        userToUpdate = new UserEntity();
    }

    public void postConstruct() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        currentUser = (UserEntity) sessionMap.get("currentUser");
    }

    public void updateUser(ActionEvent event) throws UpdateUserException, InputDataValidationException {
        try {
            userToUpdate.setPassword(updatedPassword);

            setCurrentUser(userToUpdate);
            userEntitySessionBeanLocal.updateUser(getUserToUpdate());
            setUserToUpdate(new UserEntity());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User updated successfully", null));
        } catch (UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not found: " + ex.getMessage(), null));
        }
    }

    /**
     * @return the updatedPassword
     */
    public String getUpdatedPassword() {
        return updatedPassword;
    }

    /**
     * @param updatedPassword the updatedPassword to set
     */
    public void setUpdatedPassword(String updatedPassword) {
        this.updatedPassword = updatedPassword;
    }

    /**
     * @return the userEntitySessionBeanLocal
     */
    public UserEntitySessionBeanLocal getUserEntitySessionBeanLocal() {
        return userEntitySessionBeanLocal;
    }

    /**
     * @param userEntitySessionBeanLocal the userEntitySessionBeanLocal to set
     */
    public void setUserEntitySessionBeanLocal(UserEntitySessionBeanLocal userEntitySessionBeanLocal) {
        this.userEntitySessionBeanLocal = userEntitySessionBeanLocal;
    }

    /**
     * @return the currentUser
     */
    public UserEntity getCurrentUser() {
        return currentUser;
    }

    /**
     * @param currentUser the currentUser to set
     */
    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * @return the userToUpdate
     */
    public UserEntity getUserToUpdate() {
        return userToUpdate;
    }

    /**
     * @param userToUpdate the userToUpdate to set
     */
    public void setUserToUpdate(UserEntity userToUpdate) {
        this.userToUpdate = userToUpdate;
    }

}
