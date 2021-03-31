/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCategoryException;

/**
 *
 * @author reuben
 */
@Local
public interface CategoryEntitySessionBeanLocal {
    CategoryEntity createNewCategoryEntity(CategoryEntity newCategoryEntity, Long parentCategoryId) throws InputDataValidationException, CreateNewCategoryException;
    
    List<CategoryEntity> retrieveAllCategories();
    
    List<CategoryEntity> retrieveAllRootCategories();
    
    List<CategoryEntity> retrieveAllLeafCategories();
    
    List<CategoryEntity> retrieveAllCategoriesWithoutListing();
    
    CategoryEntity retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException;
    
    void updateCategory(CategoryEntity categoryEntity, Long parentCategoryId) throws InputDataValidationException, CategoryNotFoundException, UpdateCategoryException;
    
    void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException;
}
