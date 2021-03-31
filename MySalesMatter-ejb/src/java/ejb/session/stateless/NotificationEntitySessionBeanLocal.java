/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.NotificationEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.NotificationAlreadyExpiredException;
import util.exception.NotificationNotFoundException;

/**
 *
 * @author Yuki
 */
@Local
public interface NotificationEntitySessionBeanLocal {

    public Long createNewNotification(NotificationEntity notif);

    public NotificationEntity retrieveNotificationByNotificationId(Long notificationId) throws NotificationNotFoundException;
    
    public List<NotificationEntity> retrieveAllNotifications();
    
    public List<NotificationEntity> retrieveAllActiveNotifications();
    
    public List<NotificationEntity> retrieveAllExpiredNotifications();
    
    void updateNotification(NotificationEntity n) throws NotificationNotFoundException, NotificationAlreadyExpiredException;
    
    void deleteNotification(Long notificationId) throws NotificationNotFoundException;
    
}
