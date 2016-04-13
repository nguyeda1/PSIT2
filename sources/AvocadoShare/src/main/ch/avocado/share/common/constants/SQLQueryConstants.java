package ch.avocado.share.common.constants;

import java.sql.PreparedStatement;

/**
 * Created by bergm on 22/03/2016.
 */
public class SQLQueryConstants {
    //ACCESS CONTROL DATA OBJECT
    public static final String INSERT_ACCESS_CONTROL_QUERY = "INSERT INTO avocado_share.access_control(id, creation_date, description) VALUES (DEFAULT, DEFAULT, ?) ";
    public static final int INSERT_ACCESS_CONTROL_QUERY_DESCRIPTION_INDEX = 1;
    public static final String DELETE_ACCESS_CONTROL_QUERY = "DELETE FROM access_control WHERE id = ?";
    public static final String SELECT_ACCESS_CONTROL_QUERY = "SELECT id, creation_date FROM avocado_share.access_control WHERE id=?";

    //USER DATA QUERIES
    public static final String INSERT_USER_QUERY = "INSERT INTO avocado_share.identity(id, prename, surname, avatar, password) VALUES (?, ?, ?, ?, ?)";
    public static final String INSERT_PASSWORD_VERIFICATION_QUERY = "INSERT INTO avocado_share.password_reset(id, expiry, code) VALUES (?, ?, ?)";

    private static final String SELECT_USER_SELECTED_COLUMNS = "I.id, prename, surname, avatar, description, password, address, verified, creation_date";
    public static final String SELECT_USER_QUERY = "" +
            "SELECT " + SELECT_USER_SELECTED_COLUMNS + " FROM  " +
            "  avocado_share.identity AS I " +
            "  JOIN avocado_share.email AS E " +
            "    ON I.id=E.identity_id " +
            "  JOIN avocado_share.access_control AS O " +
            "    ON O.id=I.id " +
            "  WHERE I.id=?";
    public static final String SELECT_USER_BY_MAIL_QUERY = "" +
            "SELECT " + SELECT_USER_SELECTED_COLUMNS + " " +
            "  FROM avocado_share.identity AS I " +
            "  JOIN avocado_share.email AS E " +
            "    ON I.id=E.identity_id " +
            "  JOIN avocado_share.access_control AS O " +
            "    ON O.id=I.id " +
            "  WHERE E.address=?";

    public static final String DELETE_USER_QUERY = "DELETE FROM access_control WHERE id = ?";

    public static final int USER_RESULT_ID_INDEX = 1;
    public static final int USER_RESULT_PRENAME_INDEX = 2;
    public static final int USER_RESULT_SURNAME_INDEX = 3;
    public static final int USER_RESULT_AVATAR_INDEX = 4;
    public static final int USER_RESULT_DESCRIPTION_INDEX = 5;
    public static final int USER_RESULT_PASSWORD_INDEX = 6;
    public static final int USER_RESULT_ADDRESS_INDEX = 7;
    public static final int USER_RESULT_VERIFIED_INDEX = 8;
    public static final int USER_RESULT_CREATION_DATE_INDEX = 9;

    public static final String SELECT_PASSWORD_VERIFICATION_QUERY = "SELECT id, expiry, code FROM avocado_share.password_reset WHERE id=?";
    public static final String UPDATE_USER_QUERY = "UPDATE avocado_share.identity SET prename=?, surname=?, avatar=?, password=? WHERE id=?";
    public static final String INSERT_MAIL_QUERY = "INSERT INTO avocado_share.email(identity_id, address, verified)VALUES (?, ?, FALSE)";
    public static final String SELECT_MAIL_QUERY = "SELECT identity_id, address, verified FROM avocado_share.email WHERE identity_id=?";
    public static final String INSERT_MAIL_VERIFICATION_QUERY = "INSERT INTO avocado_share.email_verification(identity_id, address, expiry, verification_code) VALUES (?, ?, ?, ?)";
    public static final String SELECT_EMAIL_VERIFICATION  = "SELECT expiry, verification_code FROM avocado_share.email_verification WHERE identity_id=? AND address=?";
    public static final int SELECT_EMAIL_VERIFICATION_USER_ID_INDEX = 1;
    public static final int SELECT_EMAIL_VERIFICATION_ADDRESS_INDEX = 2;
    public static final int EMAIL_VERIFICATION_RESULT_CODE_INDEX = 2;
    public static final int EMAIL_VERIFICATION_RESULT_EXPIRY_INDEX = 1;

    public static final String SET_MAIL_TO_VERIFIED = "UPDATE avocado_share.email SET verified=TRUE WHERE identity_id=?";
    public static final String UPDATE_ACCESS_CONTROL_DESCRIPTION = "UPDATE avocado_share.access_control SET description = ? WHERE id = ?";
    public static final int UPDATE_ACCESS_CONTROL_DESCRIPTION_DESCRIPTION_INDEX = 1;
    public static final int UPDATE_ACCESS_CONTROL_DESCRIPTION_ID_INDEX = 2;


    public final static class Module {
        private static final String table = "avocado_share.module";
        public static final String INSERT_QUERY = "INSERT INTO " + table + "(id, name) VALUES (?, ?)";
        public static final int INSERT_QUERY_ID_INDEX = 1;
        public static final int INSERT_QUERY_NAME_INDEX = 2;

        private static final String SELECT_COLUMNS = "m.id, description, name, creation_date";
        public static final int RESULT_INDEX_ID = 1;
        public static final int RESULT_INDEX_DESCRIPTION = 2;
        public static final int RESULT_INDEX_NAME = 3;
        public static final int RESULT_INDEX_CREATION_DATE = 4;

        public static final String SELECT_QUERY = "" +
                "SELECT " + SELECT_COLUMNS + " FROM " + table + " AS m " +
                " JOIN avocado_share.access_control as a ON m.id = a.id " +
                "   WHERE a.id = ?";

        public static final int SELECT_QUERY_INDEX_ID = 1;

        public static final String UPDATE_QUERY = "UPDATE " + table + " SET name=? WHERE id=?";
        public static final int UPDATE_QUERY_INDEX_ID = 2;
        public static final int UPDATE_QUERY_INDEX_NAME = 1;
    }

    /**
     * Group related queries
     */
    public final static class Group {
        public static final String TABLE = "avocado_share.access_group";
        public static final String INSERT_QUERY = "INSERT INTO "+ TABLE + "(id, name) VALUES (?, ?)";
        public static final int INSERT_QUERY_NAME_INDEX = 2;
        public static final int INSERT_QUERY_ID_INDEX = 1;
        /**
         * Index of the id in get group by id query
         */
        public static final int GET_BY_ID_ID_INDEX = 1;

        /**
         * Index of the id in get group by name query
         */
        public static final int GET_BY_NAME_NAME_INDEX = 1;
        /**
         * Index of the id in a get group result
         */
        public static final int RESULT_ID_INDEX = 1;
        /**
         * Index of the name in a get group result
         */
        public static final int RESULT_NAME_INDEX = 2;
        /**
         * Index of the description in a get group result
         */
        public static final int RESULT_DESCRIPTION_INDEX = 3;
        /**
         * Index of the creation date in a get group result
         */
        public static final int RESULT_CREATION_DATE = 4;
        /**
         * Index of the ownerId in the result
         */
        public static final int RESULT_OWNER_ID = 5;
        private static final String SELECT_COLUMNS = "g.id, name, description, creation_date, W.owner_id";
        private static final String SELECT_WITHOUT_WHERE = "" +
                "SELECT " + SELECT_COLUMNS +
                " FROM " + TABLE + " AS g " +
                "JOIN access_control AS o " +
                " ON g.id = o.id " +
                "JOIN avocado_share.ownership as W " +
                " ON o.id = W.object_id ";
        public static final String SELECT_BY_NAME_QUERY = SELECT_WITHOUT_WHERE + "WHERE g.name = ?";
        public static final String SELECT_BY_ID_QUERY = SELECT_WITHOUT_WHERE +  "WHERE g.id = ?";

        public static final String UPDATE = "UPDATE " + TABLE + " SET name = ? WHERE id = ?";
        public static final int UPDATE_INDEX_ID = 2;
        public static final int UPDATE_INDEX_NAME = 1;
    }

    //CATEGORY
    public static final String SQL_SELECT_CATEGORY_BY_NAME = "SELECT object_id, name FROM category WHERE name = '?'";

    public static final String SQL_SELECT_CATEGORY_BY_NAME_AND_OBJECT_ID = "SELECT object_id, name FROM category WHERE name = '?' AND object_id = ?";

    public static final String SQL_ADD_CATEGORY = "INSERT INTO category (object_id, name) (?, '?')";

    public static final String SQL_DELETE_CATEGORY_FROM_OBJECT = "DELETE FROM category WHERE name = '?' AND object_id = ?";


    public static final class File {
        public static final String SELECT_BY_ID_QUERY = "SELECT o.id, title, description, last_changed, creation_date, path FROM file AS f JOIN access_control AS o ON f.id = o.id WHERE o.id = ?";
        public static final String SELECT_BY_TITLE_QUERY = "SELECT o.id, title, description, last_changed, creation_date, path FROM file AS f JOIN access_control AS o ON f.id = o.id WHERE title = ?";
        public static final String INSERT_QUERY = "INSERT INTO avocado_share.file (id, title, last_changed, path) VALUES (?, ?, ?, ?)";
        public static final String UPDATE_QUERY = "UPDATE file SET title=?, last_changed=?, path=? WHERE id = ?";
    }

    //PERMISSION
    public static final String INSERT_OWNERSHIP = "INSERT INTO avocado_share.ownership(owner_id, object_id) VALUES (?, ?)";
    public static final int INSERT_OWNERSHIP_INDEX_OWNER = 1;
    public static final int INSERT_OWNERSHIP_INDEX_OBJECT = 2;

    public static final String INSERT_RIGHTS_QUERY = "INSERT INTO avocado_share.rights(object_id, owner_id, level) VALUES (?, ?, ?)";
    public static final String SELECT_OWNER_OF_OBJECT = "SELECT owner_id, object_id FROM avocado_share.ownership WHERE object_id=?";
    public static final String SELECT_READING_ACCESS_LEVEL = "SELECT level FROM avocado_share.access_level WHERE readable=TRUE AND writable=FALSE AND manageable=FALSE";

    public static final String SELECT_ACCESS_LEVEL = "" +
            "SELECT " +
            "  readable, writable, manageable, " +
            "  CASE " +
            "    WHEN o.owner_id IS NULL THEN false " +
            "    ELSE true " +
            "  END AS owner " +
            "FROM avocado_share.access_level AS l " +
            "JOIN avocado_share.rights AS r " +
            "  ON r.level = l.level " +
            "LEFT OUTER JOIN avocado_share.ownership AS o" +
            "  ON " +
            "      o.owner_id = r.owner_id " +
            "    AND " +
            "      o.object_id = r.object_id " +
            "WHERE o.owner_id = ? AND o.object_id = ?";


    public static final String SELECT_INHERITED_GROUP_RIGHTS = "" +
            "  SELECT" +
            "    glevel.readable, glevel.writable, glevel.manageable, false" +
            "  FROM avocado_share.access_level AS glevel " +
            "  JOIN avocado_share.rights AS grights " +
            "    ON grights.level = glevel.level " +
            "  JOIN avocado_share.access_group AS gro " +
            "    ON grights.owner_id = gro.id " +
            "    WHERE " +
            "      EXISTS( " +
            "        SELECT 1 " +
            "          FROM avocado_share.rights x " +
            "          NATURAL JOIN avocado_share.access_level y " +
            "          WHERE x.owner_id = ? AND x.object_id = gro.id AND y.readable = TRUE" +
            "       )" +
            "       AND grights.object_id = ?";

    public static final String SELECT_ANONYMOUS_ACCESS_LEVEL = "" +
            "SELECT alevel.readable, alevel.writable, alevel.manageable, false" +
            "  FROM avocado_share.access_level AS alevel" +
            "  NATURAL JOIN avocado_share.default_access AS arights " +
            "  WHERE arights.object_id = ?";


    public static final String SELECT_ACCESS_LEVEL_INCLUDING_INHERITED = "" +
            "SELECT " +
            "  readable, writable, manageable, " +
            "  CASE " +
            "    WHEN o.owner_id IS NULL THEN false " +
            "    ELSE true " +
            "  END AS owner " +
            "FROM avocado_share.access_level AS l " +
            "JOIN avocado_share.rights AS r " +
            "  ON r.level = l.level " +
            "LEFT OUTER JOIN avocado_share.ownership AS o " +
            "  ON " +
            "      o.owner_id = r.owner_id " +
            "    AND " +
            "      o.object_id = r.object_id " +
            " WHERE r.owner_id = ? AND r.object_id = ?" +
            "UNION " + SELECT_INHERITED_GROUP_RIGHTS +
            "UNION " + SELECT_ANONYMOUS_ACCESS_LEVEL;

    public static final String ADD_ANONYMOUS_ACCESS_LEVEL = "" +
            "INSERT INTO avocado_share.default_access (object_id, level) " +
            " VALUES (?, ?)";

    public static final int ADD_ANONYMOUS_ACCESS_LEVEL_OBJECT_ID_INDEX = 1;
    public static final int ADD_ANONYMOUS_ACCESS_LEVEL_LEVEL_INDEX = 2;


    public static final String UPDATE_ANONYMOUS_ACCESS_LEVEL = "" +
            "UPDATE avocado_share.default_access SET level = ? " +
            " WHERE object_id = ?";
    public static final int UPDATE_ANONYMOUS_ACCESS_LEVEL_LEVEL_INDEX = 1;
    public static final int UPDATE_ANONYMOUS_ACCESS_LEVEL_OBJECT_ID_INDEX = 2;


    public static final int SELECT_ACCESS_LEVEL_READ_INDEX = 1;
    public static final int SELECT_ACCESS_LEVEL_WRITE_INDEX = 2;
    public static final int SELECT_ACCESS_LEVEL_MANAGE_INDEX = 3;
    public static final int SELECT_ACCESS_LEVEL_OWNER_INDEX = 4;

    public static final String SELECT_ACCESS_LEVEL_QUERY = "" +
            "SELECT level FROM avocado_share.access_level " +
            "    WHERE readable=? AND writable=? AND manageable=?";

    public static final String SET_ACCESS_LEVEL_QUERY =
            "UPDATE avocado_share.rights SET LEVEL= ? " +
            "    WHERE object_id = ? AND owner_id = ?";

    public static final String ADD_ACCESS_LEVEL_QUERY = "" +
            "INSERT INTO avocado_share.rights (object_id, owner_id, level) " +
            "    VALUES (?, ?, ?)";

    public static final String DELETE_ACCESS_LEVEL_QUERY = "" +
            "DELETE FROM avocado_share.rights " +
            "    WHERE object_id = ? AND owner_id = ? ";

    public static final String SELECT_DEFAULT_ACCESS_LEVEL_QUERY = "" +
            "SELECT " +
            "  readable, writable, manageable " +
            "FROM avocado_share.access_level AS l " +
            "JOIN avocado_share.default_access AS d " +
            "  ON l.level = d.level";

    public static final String SELECT_GROUPS_WITH_ACCESS_ON_OBJECT = "" +
            "SELECT  id, readable, writable, manageable " +
            "FROM " + Group.TABLE + " AS g " +
            "JOIN avocado_share.rights AS r  " +
            "  ON r.owner_id = g.id " +
            "NATURAL JOIN avocado_share.access_level as l " +
            "WHERE " +
            "  r.object_id = ? " +
            "  AND (l.readable <> false OR l.writable <> false OR l.manageable <> false ) ";

    public static final String SELECT_USER_WITH_ACCESS_ON_OBJECT = "" +
            "SELECT  id, readable, writable, manageable " +
            "FROM avocado_share.identity AS g " +
            "JOIN avocado_share.rights AS r  " +
            "  ON r.owner_id = g.id " +
            "NATURAL JOIN avocado_share.access_level as l " +
            "WHERE " +
            "  r.object_id = ? " +
            "  AND ( l.readable <> false OR l.writable <> false OR l.manageable <> false ) ";

    public static final String SELECT_TARGETS_WITH_ACCESS = "" +
            "SELECT object_id " +
            "   FROM avocado_share.rights AS r " +
            "       WHERE r.owner_id = ? " +
            "       AND r.level >= ? " +
            "UNION SELECT object_id  FROM avocado_share.ownership AS o" +
            "   WHERE o.owner_id = ?";

    public static final int SELECT_TARGETS_WITH_ACCESS_OWNER_ID_INDEX = 1;
    public static final int SELECT_TARGETS_WITH_ACCESS_OWNER_ID_INDEX_2 = 3;
    public static final int SELECT_TARGETS_WITH_ACCESS_LEVEL_INDEX = 2;
}
