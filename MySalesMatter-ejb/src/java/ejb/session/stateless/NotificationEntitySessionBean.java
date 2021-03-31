/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.NotificationEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.NotificationAlreadyExpiredException;
import util.exception.NotificationNotFoundException;

/**
 *
 * @author Yuki
 */
@Stateless
public class NotificationEntitySessionBean implements NotificationEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MySalesMatter-ejbPU")
    private EntityManager em;

    public NotificationEntitySessionBean() {
    }

    @Override
    public Long createNewNotification(NotificationEntity notif) {
        em.persist(notif);
        em.flush();

        return notif.getNotificationId();
    }

    @Override
    public NotificationEntity retrieveNotificationByNotificationId(Long notificationId) throws NotificationNotFoundException {
        NotificationEntity notif = em.find(NotificationEntity.class, notificationId);

        if (notif != null) {
            return notif;
        } else {
            throw new NotificationNotFoundException("Notification Id " + notificationId + " does not exist!");
        }
    }

    @Override
    public List<NotificationEntity> retrieveAllNotifications() {
        Query query = em.createQuery("SELECT n from NotificationEntity n");
        return query.getResultList();
    }

    @Override
    public List<NotificationEntity> retrieveAllActiveNotifications() {
        Date current = new Date();

        Query query = em.createQuery("SELECT n FROM NotificationEntity n WHERE n.expiryDate >= :inCurrent");
        query.setParameter("inCurrent", current);

        return query.getResultList();
    }

    @Override
    public List<NotificationEntity> retrieveAllExpiredNotifications() {
        Date current = new Date();

        Query query = em.createQuery("SELECT n FROM NotificationEntity n WHERE n.expiryDate <= :inCurrent");
        query.setParameter("inCurrent", current);

        return query.getResultList();
    }
    
    @Override
    public void updateNotification(NotificationEntity n) throws NotificationNotFoundException, NotificationAlreadyExpiredException {
        if (n.getNotificationId() != null) {
            NotificationEntity notificationToUpdate = retrieveNotificationByNotificationId(n.getNotificationId());

            //check if announcement has already expired
            Date current = new Date();

            if (notificationToUpdate.getExpiryDate().before(current)) {
                throw new NotificationAlreadyExpiredException("Notification cannot be updated as it expired!");
            } else {
                notificationToUpdate.setTitle(n.getTitle());
                notificationToUpdate.setContent(n.getContent());
                notificationToUpdate.setPostedDate(n.getPostedDate());
                notificationToUpdate.setExpiryDate(n.getExpiryDate());
            }
        } else {
            throw new NotificationNotFoundException("Notification Id " + n.getNotificationId() + " does not exist!");
        }
    }
    
    @Override
    public void deleteNotification(Long notificationId) throws NotificationNotFoundException
    {
        NotificationEntity notificationToDelete = retrieveNotificationByNotificationId(notificationId);
        
        em.remove(notificationToDelete);
    }
}
