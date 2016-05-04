package ch.avocado.share.service.Impl;

import ch.avocado.share.common.ServiceLocator;
import ch.avocado.share.model.data.AccessControlObjectBase;
import ch.avocado.share.model.data.Category;
import ch.avocado.share.model.data.CategoryList;
import ch.avocado.share.service.exceptions.ObjectNotFoundException;
import ch.avocado.share.service.exceptions.ServiceNotFoundException;
import ch.avocado.share.service.ICategoryDataHandler;
import ch.avocado.share.service.IDatabaseConnectionHandler;
import ch.avocado.share.service.exceptions.DataHandlerException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static ch.avocado.share.common.constants.sql.CategoryConstants.*;

/**
 * Handler for categories
 */
public class CategoryDataHandler implements ICategoryDataHandler {

    @Override
    public void addAccessObjectCategories(AccessControlObjectBase accessObject) throws DataHandlerException {
        for (Category category : accessObject.getCategoryList().getNewCategories()) {
            addCategory(category.getName(), accessObject.getId());
        }
    }

    @Override
    public void updateAccessObjectCategories(AccessControlObjectBase changedAccessObject) throws DataHandlerException {
        CategoryList categories = changedAccessObject.getCategoryList();
        for (Category newCategory : categories.getNewCategories()) {
            addCategory(newCategory.getName(), changedAccessObject.getId());
        }
        for (Category delCategory : categories.getRemovedCategories()) {
            deleteCategoryAssignedObject(delCategory.getName(), changedAccessObject.getId());
        }
    }

    /**
     * return the Category by passing the Category name
     * @param name the Category name
     * @return the Category object
     */
    @Override
    public Category getCategoryByName(String name) throws DataHandlerException {
        IDatabaseConnectionHandler connectionHandler = getDatabaseHandler();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connectionHandler.getPreparedStatement(SELECT_BY_NAME);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            return createCategoryFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
    }

    /**
     * checks if a Category is already added to a AccessControlObject
     * @param name                      the name of the Category
     * @param accessObjectReferenceId   the id of the AccessControlObject
     * @return true if the Category is already added to the AccessControlObject
     */
    @Override
    public boolean hasCategoryAssignedObject(String name, String accessObjectReferenceId) throws DataHandlerException {
        IDatabaseConnectionHandler connectionHandler = getDatabaseHandler();
        if(connectionHandler == null) return false;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connectionHandler.getPreparedStatement(SELECT_BY_NAME_AND_OBJECT_ID);
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, Long.parseLong(accessObjectReferenceId));
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
    }

    /**
     * Returns all categories, which are assigned to AccessControlObject.
     * @param accessControlObjectId the accessObjectId, for which the categories should be returned.
     * @return the accessObject assigned categories.
     */
    @Override
    public List<Category> getAccessObjectAssignedCategories(String accessControlObjectId) throws DataHandlerException {
        IDatabaseConnectionHandler connectionHandler = getDatabaseHandler();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connectionHandler.getPreparedStatement(SQL_SELECT_CATEGORIES_BY_OBJECT_ID);
            preparedStatement.setLong(1, Long.parseLong(accessControlObjectId));
            resultSet = preparedStatement.executeQuery();
            return createAccessObjectAssignedCategories(resultSet);
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
    }

    private Category createCategoryFromResultSet(ResultSet resultSet) throws DataHandlerException {
        String name;
        Category category = null;
        try {
            if(resultSet.next()) {
                List<String> objectIds = new LinkedList<>();
                name = resultSet.getString(2);
                do {
                    String id = resultSet.getString(1);
                    assert id != null;
                    objectIds.add(id);
                }while (resultSet.next());
                category = new Category(name, objectIds);
            }
        }catch (SQLException e) {
            throw new DataHandlerException(e);
        }
        return category;
    }

    private List<Category> createAccessObjectAssignedCategories(ResultSet resultSet) throws DataHandlerException {
        List<Category> categories = new ArrayList<>();
        try {
            while(resultSet.next()) {
                Category category = getCategoryByName(resultSet.getString(1));
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
        return categories;
    }

    private void addCategory(String name, String accessObjectReferenceId) throws DataHandlerException {
        IDatabaseConnectionHandler connectionHandler = getDatabaseHandler();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connectionHandler.getPreparedStatement(SQL_ADD_CATEGORY);
            preparedStatement.setLong(1, Long.parseLong(accessObjectReferenceId));
            preparedStatement.setString(2, name);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
    }

    private void deleteCategoryAssignedObject(String name, String accessObjectReferenceId) throws DataHandlerException {
        IDatabaseConnectionHandler connectionHandler = getDatabaseHandler();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connectionHandler.getPreparedStatement(SQL_DELETE_CATEGORY_FROM_OBJECT);
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, Long.parseLong(accessObjectReferenceId));
            connectionHandler.deleteDataSet(preparedStatement);
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
    }

    private IDatabaseConnectionHandler getDatabaseHandler() throws DataHandlerException {
        try {
            return ServiceLocator.getService(IDatabaseConnectionHandler.class);
        } catch (ServiceNotFoundException e) {
            throw new DataHandlerException(e);
        }
    }
}
