package ejb.session.singleton;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.ListingEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import ejb.session.stateless.TagEntitySessionBeanLocal;
import entity.CategoryEntity;
import entity.ListingEntity;
import entity.UserEntity;
import entity.TagEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CreateNewCategoryException;
import util.exception.CreateNewListingException;
import util.exception.CreateNewTagException;
import util.exception.InputDataValidationException;
import util.exception.UserNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UserEmailExistsException;



@Singleton
@LocalBean
@Startup

public class DataInitializationSessionBean
{    
    @PersistenceContext(unitName = "MySalesMatter-ejbPU")
    private EntityManager entityManager;
    
    @EJB
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;
    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    @EJB
    private TagEntitySessionBeanLocal tagEntitySessionBeanLocal;
    
    
    
    public DataInitializationSessionBean()
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        try
        {
            userEntitySessionBeanLocal.retrieveUserByEmail("user@gmail.com");
        }
        catch(UserNotFoundException ex)
        {
            initializeData();
        }
    }
    
    
    
    // Updated in v4.2 to include reorderQuantity and bean validation
    
    private void initializeData()
    {
        try
        {
            userEntitySessionBeanLocal.registerUser(new UserEntity("user@gmail.com", "82882095", "0123456789", "Hi, my name is user", "user", "User", "password"));
            
            // Added in v5.0
            // Updated in v5.1
            CategoryEntity categoryEntityElectronics = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Electronics", "Electronics"), null);
            CategoryEntity categoryEntityFashions = categoryEntitySessionBeanLocal.createNewCategoryEntity(new CategoryEntity("Fashions", "Fashions"), null);

            // Newly added in v5.1
            TagEntity tagEntityPopular = tagEntitySessionBeanLocal.createNewTagEntity(new TagEntity("popular"));
            TagEntity tagEntityDiscount = tagEntitySessionBeanLocal.createNewTagEntity(new TagEntity("discount"));
            TagEntity tagEntityNew = tagEntitySessionBeanLocal.createNewTagEntity(new TagEntity("new"));

            // Newly added in v5.1
            List<Long> tagIdsPopular = new ArrayList<>();
            tagIdsPopular.add(tagEntityPopular.getTagId());
            
            List<Long> tagIdsDiscount = new ArrayList<>();
            tagIdsDiscount.add(tagEntityDiscount.getTagId());
            
            List<Long> tagIdsPopularDiscount = new ArrayList<>();
            tagIdsPopularDiscount.add(tagEntityPopular.getTagId());
            tagIdsPopularDiscount.add(tagEntityDiscount.getTagId());
            
            List<Long> tagIdsPopularNew = new ArrayList<>();
            tagIdsPopularNew.add(tagEntityPopular.getTagId());
            tagIdsPopularNew.add(tagEntityNew.getTagId());
            
            List<Long> tagIdsPopularDiscountNew = new ArrayList<>();
            tagIdsPopularDiscountNew.add(tagEntityPopular.getTagId());
            tagIdsPopularDiscountNew.add(tagEntityDiscount.getTagId());
            tagIdsPopularDiscountNew.add(tagEntityNew.getTagId());
            
            List<Long> tagIdsEmpty = new ArrayList<>();

        }
        catch(InputDataValidationException | UnknownPersistenceException | UserEmailExistsException | CreateNewTagException | CreateNewCategoryException ex)
        {
            ex.printStackTrace();
        }
    }
}