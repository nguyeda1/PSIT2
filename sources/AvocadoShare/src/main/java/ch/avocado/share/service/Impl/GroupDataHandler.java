package ch.avocado.share.service.Impl;

import ch.avocado.share.common.SearchResultComparater;
import ch.avocado.share.model.data.Category;
import ch.avocado.share.model.data.Group;
import ch.avocado.share.model.data.Rating;
import ch.avocado.share.service.IDatabaseConnectionHandler;
import ch.avocado.share.service.IGroupDataHandler;
import ch.avocado.share.service.exceptions.DataHandlerException;
import ch.avocado.share.service.exceptions.ObjectNotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ch.avocado.share.common.constants.sql.GroupConstants.*;

/**
 * Implementation of the group data handler which accesses
 * the database by using {@link IDatabaseConnectionHandler}
 */
public class GroupDataHandler extends DataHandlerBase implements IGroupDataHandler {

    private PreparedStatement getGetStatement(String id) throws DataHandlerException {
        if (id == null) throw new NullPointerException("id is null");
        PreparedStatement statement;

        try {
            statement = getConnectionHandler().getPreparedStatement(SELECT_BY_ID_QUERY);
            statement.setInt(GET_BY_ID_ID_INDEX, Integer.parseInt(id));
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
        return statement;
    }

    /**
     * @param statement The statement to fetch the group
     * @return The result set (not null)
     * @throws DataHandlerException
     */
    private ResultSet executeGetStatement(PreparedStatement statement) throws DataHandlerException {
        if (statement == null) throw new NullPointerException("statement is null");
        try {
            return getConnectionHandler().executeQuery(statement);
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
    }

    private Group getGroupFromResultSet(ResultSet resultSet) throws DataHandlerException {
        if (resultSet == null) throw new NullPointerException("resultSet is null");
        String id, name, description, ownerId;
        Date creationDate;
        // TODO: @muellcy1 fetch categories and rating
        try {
            id = resultSet.getString(RESULT_ID_INDEX);
            name = resultSet.getString(RESULT_NAME_INDEX);
            description = resultSet.getString(RESULT_DESCRIPTION_INDEX);
            creationDate = resultSet.getTimestamp(RESULT_CREATION_DATE);
            ownerId = resultSet.getString(RESULT_OWNER_ID);
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
        if (ownerId == null) {
            ownerId = id;
        }
        return new Group(id, new ArrayList<Category>(), creationDate, new Rating(), ownerId, description, name);
    }

    @Override
    public Group getGroup(String id) throws DataHandlerException, ObjectNotFoundException {
        if (id == null) throw new NullPointerException("id is null");
        PreparedStatement statement = getGetStatement(id);
        ResultSet resultSet = executeGetStatement(statement);
        try {
            if (!resultSet.next()) throw new ObjectNotFoundException(Group.class, id);
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
        return getGroupFromResultSet(resultSet);
    }

    @Override
    public List<Group> getGroups(Collection<String> ids) throws DataHandlerException {
        if (ids == null) throw new NullPointerException("ids is null");
        if (ids.size() == 0) return new ArrayList<>();
        ArrayList<Group> groups = new ArrayList<>(ids.size());
        String query = SELECT_BY_ID_LIST + getIdList(ids);
        IDatabaseConnectionHandler connectionHandler = getConnectionHandler();
        try {
            PreparedStatement statement = connectionHandler.getPreparedStatement(query);
            ResultSet result = connectionHandler.executeQuery(statement);
            while (result.next()) {
                groups.add(getGroupFromResultSet(result));
            }
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
        groups.trimToSize();
        Collections.sort(groups, new SearchResultComparater());
        return groups;
    }

    private PreparedStatement getInsertStatement(String id, String name) throws DataHandlerException {
        if (name == null) throw new NullPointerException("name is null");
        if (id == null) throw new NullPointerException("id is null");
        PreparedStatement statement;
        try {
            statement = getConnectionHandler().getPreparedStatement(INSERT_QUERY);
            statement.setInt(INSERT_QUERY_ID_INDEX, Integer.parseInt(id));
            statement.setString(INSERT_QUERY_NAME_INDEX, name);
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
        return statement;
    }

    private void executeInsertStatement(PreparedStatement statement) throws DataHandlerException {
        if (statement == null) throw new NullPointerException("statement is null");
        try {
            getConnectionHandler().insertDataSet(statement);
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
    }

    @Override
    public String addGroup(Group group) throws DataHandlerException {
        if (group == null) throw new NullPointerException("group is null");
        if (group.getId() != null) throw new IllegalArgumentException("group.getId() is not null");
        group.setId(addAccessControlObject(group));
        PreparedStatement statement = getInsertStatement(group.getId(), group.getName());
        executeInsertStatement(statement);
        return group.getId();
    }

    @Override
    public void updateGroup(Group group) throws DataHandlerException, ObjectNotFoundException {
        if (group == null) throw new NullPointerException("group is null");
        PreparedStatement statement;
        long id = Long.parseLong(group.getId());
        try {
            statement = getConnectionHandler().getPreparedStatement(UPDATE);
            statement.setLong(UPDATE_INDEX_ID, id);
            statement.setString(UPDATE_INDEX_NAME, group.getName());
            if(!getConnectionHandler().updateDataSet(statement)) {
                throw new ObjectNotFoundException(Group.class, id);
            }
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
        updateObject(group);
    }

    @Override
    public void deleteGroup(Group group) throws DataHandlerException, ObjectNotFoundException {
        deleteAccessControlObject(group);
    }

    private PreparedStatement getGetByNameStatement(String name) throws DataHandlerException {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = getConnectionHandler().getPreparedStatement(SELECT_BY_NAME_QUERY);
            preparedStatement.setString(GET_BY_NAME_NAME_INDEX, name);
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
        return preparedStatement;
    }

    @Override
    public Group getGroupByName(String name) throws DataHandlerException, ObjectNotFoundException {
        if (name == null) throw new NullPointerException("name is null");
        ResultSet resultSet = executeGetStatement(getGetByNameStatement(name));
        try {
            if (!resultSet.next()) throw new ObjectNotFoundException(Group.class, name);
        } catch (SQLException e) {
            throw new DataHandlerException(e);
        }
        return getGroupFromResultSet(resultSet);
    }

    @Override
    public List<Group> searchGroups(String searchString) throws DataHandlerException {
        try {
            IDatabaseConnectionHandler connectionHandler = getConnectionHandler();

            PreparedStatement ps = connectionHandler.getPreparedStatement(SEARCH_QUERY);
            ps.setString(1, "%"+searchString+"%");
            ps.setString(2, "%"+searchString+"%");

            ResultSet rs = connectionHandler.executeQuery(ps);

            List<Group> groups = new LinkedList<>();
            while (rs.next())
            {
                Group group = getGroupFromResultSet(rs);
                groups.add(group);
            }

            Collections.sort(groups, new SearchResultComparater());
            return groups;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
