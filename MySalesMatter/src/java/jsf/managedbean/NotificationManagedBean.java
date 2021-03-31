/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.NotificationEntitySessionBeanLocal;
import entity.NotificationEntity;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.NotificationAlreadyExpiredException;
import util.exception.NotificationNotFoundException;

/**
 *
 * @author Yuki
 */
@Named(value = "notificationManagedBean")
@ViewScoped
public class NotificationManagedBean implements Serializable{

    @EJB(name = "NotificationEntitySessionBeanLocal")
    private NotificationEntitySessionBeanLocal notificationEntitySessionBeanLocal;

    private List<NotificationEntity> ongoingNotifications;
    private List<NotificationEntity> expiredNotifications;
    private List<NotificationEntity> filteredNotifications;
    private List<NotificationEntity> notifications;
    
    private NotificationEntity notificationToView;
    private NotificationEntity ongoingToUpdate;
    
    private NotificationEntity newNotification;
    private String selectedFilter;
    
    public NotificationManagedBean() {
        newNotification = new NotificationEntity();
        selectedFilter = "Ongoing";
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setOngoingNotifications(notificationEntitySessionBeanLocal.retrieveAllActiveNotifications());
        setExpiredNotifications(notificationEntitySessionBeanLocal.retrieveAllExpiredNotifications());
        setNotifications(notificationEntitySessionBeanLocal.retrieveAllNotifications());
    }
    
    public void createNewNotification(ActionEvent event)
    {
        Long newNotificationId = notificationEntitySessionBeanLocal.createNewNotification(getNewNotification());
        notifications.add(newNotification);
        ongoingNotifications.add(newNotification);
        setNewNotification(new NotificationEntity());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New notification created successfully (Notification ID: " + newNotificationId + ")", null));

    }
    
    public void updateNotifications(ActionEvent event)
    {
        try
        {
            notificationEntitySessionBeanLocal.updateNotification(getOngoingToUpdate());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Notification created successfully", null));
        } catch (NotificationNotFoundException ex) {
            Logger.getLogger(NotificationManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotificationAlreadyExpiredException ex) {
            Logger.getLogger(NotificationManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    /**
     * @return the ongoingNotifications
     */
    public List<NotificationEntity> getOngoingNotifications() {
        return ongoingNotifications;
    }

    /**
     * @param ongoingNotifications the ongoingNotifications to set
     */
    public void setOngoingNotifications(List<NotificationEntity> ongoingNotifications) {
        this.ongoingNotifications = ongoingNotifications;
    }

    /**
     * @return the expiredNotifications
     */
    public List<NotificationEntity> getExpiredNotifications() {
        return expiredNotifications;
    }

    /**
     * @param expiredNotifications the expiredNotifications to set
     */
    public void setExpiredNotifications(List<NotificationEntity> expiredNotifications) {
        this.expiredNotifications = expiredNotifications;
    }

    /**
     * @return the filteredNotifications
     */
    public List<NotificationEntity> getFilteredNotifications() {
        return filteredNotifications;
    }

    /**
     * @param filteredNotifications the filteredNotifications to set
     */
    public void setFilteredNotifications(List<NotificationEntity> filteredNotifications) {
        this.filteredNotifications = filteredNotifications;
    }

    /**
     * @return the notifications
     */
    public List<NotificationEntity> getNotifications() {
        return notifications;
    }

    /**
     * @param notifications the notifications to set
     */
    public void setNotifications(List<NotificationEntity> notifications) {
        this.notifications = notifications;
    }

    /**
     * @return the notificationToView
     */
    public NotificationEntity getNotificationToView() {
        return notificationToView;
    }

    /**
     * @param notificationToView the notificationToView to set
     */
    public void setNotificationToView(NotificationEntity notificationToView) {
        this.notificationToView = notificationToView;
    }

    /**
     * @return the ongoingToUpdate
     */
    public NotificationEntity getOngoingToUpdate() {
        return ongoingToUpdate;
    }

    /**
     * @param ongoingToUpdate the ongoingToUpdate to set
     */
    public void setOngoingToUpdate(NotificationEntity ongoingToUpdate) {
        this.ongoingToUpdate = ongoingToUpdate;
    }

    /**
     * @return the newNotification
     */
    public NotificationEntity getNewNotification() {
        return newNotification;
    }

    /**
     * @param newNotification the newNotification to set
     */
    public void setNewNotification(NotificationEntity newNotification) {
        this.newNotification = newNotification;
    }
        
            
}
