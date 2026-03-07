package com.marotech.skillhub.components.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.marotech.skillhub.model.Parameter;
import com.marotech.skillhub.model.*;
import com.marotech.skillhub.repository.GenericRepository;
import com.marotech.skillhub.repository.ResultsType;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RepositoryService {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private GenericRepository<BaseEntity> repository;

    public List<User> fetchAllUsersWithRoles(List<String> roleNames) {
        List<UserRole> roles = findRolesByNames(roleNames);
        List<String> roleIds = new ArrayList<>();
        for (UserRole userRole : roles) {
            roleIds.add(userRole.getId());
        }
        return fetchUsersWithRoleIds(roleIds);
    }

    public List<UserRole> findRolesByNames(List<String> roleNames) {
        return entityManager.createQuery(
                        "SELECT r FROM UserRole r WHERE r.roleName IN :roleNames", UserRole.class)
                .setParameter("roleNames", roleNames)
                .getResultList();
    }

    public List<User> fetchUsersWithRoleIds(List<String> roleIds) {
        return entityManager.createQuery(
                        "SELECT DISTINCT u FROM User u JOIN u.userRoles r " +
                                "WHERE r.id IN :roleIds", User.class)
                .setParameter("roleIds", roleIds)
                .getResultList();
    }

    public List<User> fetchUsersByFilters(Category category, Skill skill, City city, Suburb suburb) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT DISTINCT u FROM User u ");

        Map<String, Object> parameters = new HashMap<>();

        // Build dynamic WHERE conditions
        List<String> conditions = new ArrayList<>();

        // Category filter (skill.category = :category)
        if (category != null) {
            jpql.append("JOIN u.skills s ");
            conditions.add("s.category = :category");
            parameters.put("category", category);
        }

        // Skill filter (direct skill match)
        if (skill != null) {
            jpql.append("JOIN u.skills s2 ");
            conditions.add("s2 = :skill");
            parameters.put("skill", skill);
        }

        // City filter
        if (city != null) {
            conditions.add("u.address.suburb.city = :city");
            parameters.put("city", city);
        }

        // Suburb filter
        if (suburb != null) {
            conditions.add("u.address.suburb = :suburb");
            parameters.put("suburb", suburb);
        }

        if (!conditions.isEmpty()) {
            jpql.append("WHERE ");
            jpql.append(String.join(" AND ", conditions));
        }

        Query query = entityManager.createQuery(jpql.toString(), User.class);

        // Set parameters
        parameters.forEach(query::setParameter);

        return query.getResultList();
    }


    public List<Notification> findNotificationsByRecipient(User recipient) {
        if (recipient == null) {
            return new ArrayList<>();
        }
        String jpql = "SELECT t FROM Notification t WHERE t.recipient = :recipient";

        TypedQuery<Notification> query = entityManager.createQuery(jpql, Notification.class);
        query.setParameter("recipient", recipient);
        return query.getResultList();
    }

    public User findUserByEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return null;
        }
        try {
            return entityManager.createQuery("SELECT u from User u WHERE u.email =?1", User.class).
                    setParameter(1, email).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Suburb fetchSuburbByName(City city, String name) {
        if (StringUtils.isBlank(name) || city == null) {
            return null;
        }
        try {
            return entityManager.createQuery("SELECT u from Suburb u WHERE u.name =?1 " +
                            "and u.city=?2", Suburb.class).
                    setParameter(1, name).
                    setParameter(2, city).
                    getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public City fetchCityByName(String name, String country) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        try {
            return entityManager.createQuery("SELECT u from City u WHERE u.name =?1 " +
                            "and u.country=?2", City.class).
                    setParameter(1, name).
                    setParameter(2, country).
                    getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Skill findSkillByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        try {
            return entityManager.createQuery("SELECT u from Skill u WHERE u.name =?1", Skill.class).
                    setParameter(1, name).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Category findCategoryByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        try {
            return entityManager.createQuery("SELECT u from Category u WHERE u.name =?1", Category.class).
                    setParameter(1, name).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public List<Job> fetchAllJobs() {
        return entityManager.createQuery("SELECT u from Job u", Job.class).
                getResultList();
    }

    public List<LanguageModel> findAllLanguageModels() {
        return entityManager.createQuery("SELECT u from LanguageModel u", LanguageModel.class).
                getResultList();
    }
    public Job findJobById(String id) {
        return entityManager.createQuery("SELECT u from Job u where u.id = ?1", Job.class).
                setParameter(1, id).getSingleResult();
    }
    public Suburb findSuburbById(String id) {
        return entityManager.createQuery("SELECT u from Suburb u where u.id = ?1", Suburb.class).
                setParameter(1, id).getSingleResult();
    }

    public City findCityById(String id) {
        return entityManager.createQuery("SELECT u from City u where u.id = ?1", City.class).
                setParameter(1, id).getSingleResult();
    }

    public Skill findSkillById(String id) {
        return entityManager.createQuery("SELECT u from Skill u where u.id = ?1", Skill.class).
                setParameter(1, id).getSingleResult();
    }

    public Comment findCommentById(String id) {
        return entityManager.createQuery("SELECT u from Comment u where u.id = ?1", Comment.class).
                setParameter(1, id).getSingleResult();
    }

    public Attachment findAttachmentById(String id) {
        return entityManager.createQuery("SELECT u from Attachment u where u.id = ?1", Attachment.class).
                setParameter(1, id).getSingleResult();
    }

    public LanguageModel findLanguageModelById(String id) {
        return entityManager.createQuery("SELECT u from LanguageModel u where u.id = ?1", LanguageModel.class).
                setParameter(1, id).getSingleResult();
    }

    public Article findPublicationById(String id) {
        return entityManager.createQuery("SELECT u from Publication u where u.id = ?1", Article.class).
                setParameter(1, id).getSingleResult();
    }

    public List<Article> findPublicationsForUser(User worker) {
        return entityManager.createQuery(
                        "SELECT p FROM Publication p JOIN p.workers a WHERE a = :worker", Article.class)
                .setParameter("worker", worker)
                .getResultList();
    }

    public User findUserById(String id) {
        return entityManager.createQuery("SELECT u from User u where u.id = ?1", User.class).
                setParameter(1, id).getSingleResult();
    }

    public JDBCDataSource findJDBCDataSourceById(String id) {
        return entityManager.createQuery("SELECT u from JDBCDataSource u where u.id = ?1", JDBCDataSource.class).
                setParameter(1, id).getSingleResult();
    }

    public List<JDBCDataSource> findAllJDBCDataSources() {
        return entityManager.createQuery("SELECT u from JDBCDataSource u ",
                JDBCDataSource.class).getResultList();
    }

    public List<City> fetchAllCities() {
        return entityManager.createQuery("SELECT u from City u ",
                City.class).getResultList();
    }

    public UserRole findUserRoleById(String id) {
        return entityManager.createQuery("SELECT u from UserRole u where u.id = ?1", UserRole.class).
                setParameter(1, id).getSingleResult();
    }

    public Category findCategoryById(String id) {
        return entityManager.createQuery("SELECT u from Category u where u.id = ?1", Category.class).
                setParameter(1, id).getSingleResult();
    }

    public Notification findNotificationById(String id) {
        return entityManager.createQuery("SELECT u from Notification u where u.id = ?1", Notification.class).
                setParameter(1, id).getSingleResult();
    }

    public Iterable<Article> findAllPublications() {
        return entityManager.createQuery("SELECT u from Publication u", Article.class).getResultList();
    }

    public List<UserRole> findAllRoles() {
        return entityManager.createQuery("SELECT u from UserRole u", UserRole.class).getResultList();
    }

    public User findUserByNationalId(String nationalId) {
        try {
            return entityManager.createQuery("SELECT u from User u WHERE u.nationalId =?1", User.class).
                    setParameter(1, nationalId).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public User findUserByNationalIdAndRole(String nationalId, String roleName) {
        try {
            User user = entityManager.createQuery("SELECT u from User u WHERE u.nationalId =?1", User.class).
                    setParameter(1, nationalId).getSingleResult();
            if (user != null && user.hasRole(roleName)) {
                return user;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public User findUserByMobilePhoneAndRole(String mobilePhone, String roleName) {
        try {
            User user = entityManager.createQuery("SELECT u from User u WHERE u.mobilePhone =?1", User.class).
                    setParameter(1, mobilePhone).getSingleResult();
            if (user != null && user.hasRole(roleName)) {
                return user;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public User findUserByMobilePhone(String mobilePhone) {
        try {
            return entityManager.createQuery("SELECT u from User u WHERE u.mobilePhone =?1", User.class).
                    setParameter(1, mobilePhone).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Long countUsers() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(u) FROM User u", Long.class);
        return query.getSingleResult();
    }

    public AuthUser findAuthUserByUserName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return null;
        }
        try {
            return entityManager.createQuery("SELECT u from AuthUser u WHERE u.userName =?1", AuthUser.class).
                    setParameter(1, userName).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public UserRole findUserRoleByRoleName(String roleName) {
        if (StringUtils.isBlank(roleName)) {
            return null;
        }
        try {
            return entityManager.createQuery("SELECT u from UserRole u WHERE u.roleName =?1", UserRole.class).
                    setParameter(1, roleName).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public String showSchemaInfo(String schema) throws Exception {
        String query = "SELECT t.TABLE_SCHEMA, t.TABLE_NAME, t.TABLE_TYPE,\n" +
                " c.COLUMN_NAME, c.DATA_TYPE, c.CHARACTER_MAXIMUM_LENGTH, c.IS_NULLABLE\n" +
                " FROM INFORMATION_SCHEMA.TABLES t\n" +
                " LEFT JOIN INFORMATION_SCHEMA.COLUMNS c\n" +
                " ON t.TABLE_NAME = c.TABLE_NAME AND t.TABLE_SCHEMA = c.TABLE_SCHEMA\n" +
                " WHERE t.TABLE_TYPE = 'BASE TABLE' and t.TABLE_SCHEMA = '" + schema + "';";

        return nativeQueryResultAsString(query);
    }

    private static final String COL_DDL = "\n\n//Column definitions for table %s in schema %s\n";
    private static final String CON_DDL = "\n\n//Constraint definitions for table %s in schema %s\n";
    private static final String INDEX_DDL = "\n\n//Constraint definitions for table %s in schema %s\n";

    public String showTableDefinitions(String platform, String schemaName) throws Exception {

        List<String> tableNames = selectTableNames(platform, schemaName);

        if (tableNames.isEmpty()) {
            LOG.error("Could not find tables for schema " + schemaName + " in platform " + platform);
            return null;
        }

        StringBuilder builder = new StringBuilder();

        for (String tableName : tableNames) {
            if (SupportedDBPlatform.MYSQL.name().equalsIgnoreCase(platform)) {
                builder.append("\n\n//DDL information for table " + tableName + " in schema " + schemaName + "\n");
                String query = "SHOW CREATE TABLE " + tableName;
                String tableDDL = nativeQueryResultAsString(query);
                builder.append(tableDDL).append("\n");
            } else if (SupportedDBPlatform.POSTGRESQL.name().equalsIgnoreCase(platform)) {

                //Retrieve column definitions
                String colQuery = "SELECT c.column_name,c.data_type " +
                        " FROM " +
                        " information_schema.columns c " +
                        " WHERE " +
                        " (c.table_schema = '" + schemaName + "' OR c.table_schema = 'public')" +
                        " AND c.table_name = '" + tableName + "';";

                String colDDL = nativeQueryResultAsString(colQuery);
                if (StringUtils.isNoneBlank(colDDL)) {
                    builder.append(String.format(COL_DDL, tableName, schemaName)).append(colDDL).append("\n");

                    //Retrieve constraint definitions
                    String conQuery = "SELECT con.conname, con.contype, pg_get_constraintdef(con.oid)" +
                            " FROM pg_catalog.pg_constraint con" +
                            " JOIN pg_catalog.pg_class rel ON rel.oid = con.conrelid" +
                            " JOIN pg_catalog.pg_namespace nsp ON nsp.oid = con.connamespace" +
                            " WHERE (nsp.nspname = '" + schemaName + "' OR nsp.nspname = 'public')" +
                            " AND rel.relname = '" + tableName + "';";

                    String conDDL = nativeQueryResultAsString(conQuery);
                    builder.append(String.format(CON_DDL, tableName, schemaName)).append(conDDL).append("\n");

                    // Retrieve index definitions
                    String indexQuery = "SELECT indexname, indexdef " +
                            " FROM pg_indexes " +
                            " WHERE (schemaname = '" + schemaName + "' OR schemaname = 'public')" +
                            " AND tablename = '" + tableName + "';";
                    String indexDDL = nativeQueryResultAsString(indexQuery);
                    builder.append(String.format(INDEX_DDL, tableName, schemaName)).append(indexDDL).append("\n");
                } else {
                    LOG.error("Could not fetch column definitions for " + schemaName + " in platform " + platform);
                    return null;
                }
            } else if (SupportedDBPlatform.HSQLDB.name().equalsIgnoreCase(platform)) {
                if (isUpperCase(tableName)) {
                    continue;
                }
                String query = "SELECT \n" +
                        "   c.column_name,\n" +
                        "   c.data_type,\n" +
                        "   tc.constraint_type,\n" +
                        "   tc.constraint_name,\n" +
                        "   cc.check_clause\n" +
                        " FROM \n" +
                        "   information_schema.columns c\n" +
                        " LEFT JOIN \n" +
                        "    information_schema.constraint_column_usage ccu \n" +
                        "    ON c.table_name = ccu.table_name \n" +
                        "    AND c.column_name = ccu.column_name\n" +
                        " LEFT JOIN \n" +
                        "    information_schema.table_constraints tc \n" +
                        "    ON ccu.constraint_name = tc.constraint_name\n" +
                        " LEFT JOIN \n" +
                        "    information_schema.check_constraints cc \n" +
                        "    ON tc.constraint_name = cc.constraint_name\n" +
                        " WHERE \n" +
                        "    c.table_name = '" + tableName + "';";
                String tableDDL = nativeQueryResultAsString(query);
                builder.append("\nTable " + tableName + ":\n").append(tableDDL).append("\n");
            } else {
                LOG.error("Error fetching database metadata for platform " + platform);
                return null;
            }
        }
        return builder.toString();
    }

    private boolean isUpperCase(String input) {
        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    public List<String> selectTableNames(String platform, String schemaName) throws Exception {
        String queryString;
        if (SupportedDBPlatform.HSQLDB.name().equalsIgnoreCase(platform)) {
            queryString = HS_SELECT_TABLES;
        } else {
            queryString = String.format(SELECT_TABLES, schemaName);
        }
        List<String> tableNames = new ArrayList<>();
        try {
            Query query = entityManager.createNativeQuery(queryString);
            List<Object> results = query.getResultList();
            for (Object result : results) {
                tableNames.add((String) result);
            }
            return tableNames;
        } catch (Exception e) {
            LOG.error("Error retrieving table names for : " + schemaName, e);
            return tableNames;
        }
    }

    public String nativeQueryResultAsString(SQLQuery sqlQuery) throws Exception {

        Query nativeQuery = entityManager.createNativeQuery(sqlQuery.getStatement());

        List<Parameter> parameters = sqlQuery.getParameters();
        int index = 1;
        if (!parameters.isEmpty()) {
            for (Parameter parameter : parameters) {
                switch (parameter.getType()) {
                    case SQLTypes.INTEGER:
                        nativeQuery.setParameter(index, Integer.parseInt(parameter.getValue()));
                        break;
                    case SQLTypes.DOUBLE:
                        nativeQuery.setParameter(index, new BigDecimal(parameter.getValue()));
                        break;
                    case SQLTypes.STRING:
                        if (parameter.isLike()) {
                            nativeQuery.setParameter(index, "%" + parameter.getValue() + "%");
                        } else {
                            nativeQuery.setParameter(index, parameter.getValue());
                        }
                        break;
                    case SQLTypes.LONG:
                        nativeQuery.setParameter(index, Long.parseLong(parameter.getValue()));
                        break;
                    case SQLTypes.FLOAT:
                        nativeQuery.setParameter(index, Float.parseFloat(parameter.getValue()));
                        break;
                    case SQLTypes.DATE:
                        Date sqlDate = getDate(parameter);
                        nativeQuery.setParameter(index, sqlDate);
                        break;
                    case SQLTypes.TIME_STAMP:
                        Timestamp timestamp = getTimestamp(parameter);
                        nativeQuery.setParameter(index, timestamp);
                        break;
                    default:
                        break;
                }
                index++;
            }
        }

        // Execute the query and get the results
        List<?> results = nativeQuery.getResultList();

        // Initialize the result string
        StringBuilder resultString = new StringBuilder();

        // Check if the result is single-column or multi-column
        if (!results.isEmpty() && results.get(0) instanceof Object[]) {
            // Multi-column result
            for (Object row : results) {
                Object[] columns = (Object[]) row; // Cast each row to Object[]
                for (Object value : columns) {
                    resultString.append(String.valueOf(value)).append(" | ");
                }
                resultString.append("\n");
            }
        } else {
            // Single-column result
            for (Object value : results) {
                resultString.append(value).append("\n");
            }
        }

        // Return the result string
        return resultString.toString();
    }

    private static java.sql.Date getDate(Parameter parameter) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = simpleDateFormat.parse(parameter.getValue());
        Date sqlDate = new Date(date.getTime());
        return sqlDate;
    }

    private static java.sql.Timestamp getTimestamp(Parameter parameter) throws ParseException {
        SimpleDateFormat timeFateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date time = timeFateFormat.parse(parameter.getValue());
        Timestamp timestamp = new Timestamp(time.getTime());
        return timestamp;
    }

    private String nativeQueryResultAsTabularString(JDBCDataSource dataSource, SQLQuery sqlQuery) throws Exception {
        // Load the JDBC driver
        Class.forName(dataSource.getDriverClassName());

        try (java.sql.Connection connection = DriverManager.getConnection(
                dataSource.getJdbcUrl(),
                dataSource.getUsername(),
                dataSource.getPassword());
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery.getStatement())) {

            // Bind parameters
            List<Parameter> parameters = sqlQuery.getParameters();
            int index = 1;
            for (Parameter parameter : parameters) {
                switch (parameter.getType()) {
                    case SQLTypes.INTEGER:
                        pstmt.setInt(index, Integer.parseInt(parameter.getValue()));
                        break;
                    case SQLTypes.DOUBLE:
                        pstmt.setDouble(index, Double.parseDouble(parameter.getValue()));
                        break;
                    case SQLTypes.BIG_DECIMAL:
                        pstmt.setBigDecimal(index, new BigDecimal(parameter.getValue()));
                        break;
                    case SQLTypes.STRING:
                        pstmt.setString(index, parameter.getValue());
                        break;
                    case SQLTypes.LONG:
                        pstmt.setLong(index, Long.parseLong(parameter.getValue()));
                        break;
                    case SQLTypes.FLOAT:
                        pstmt.setFloat(index, Float.parseFloat(parameter.getValue()));
                        break;
                    case SQLTypes.DATE:
                        pstmt.setDate(index, getDate(parameter));
                        break;
                    case SQLTypes.TIME_STAMP:
                        pstmt.setTimestamp(index, getTimestamp(parameter));
                        break;
                    default:
                        break;
                }
                index++;
            }

            // Execute the query
            try (ResultSet resultSet = pstmt.executeQuery()) {
                StringBuilder resultString = new StringBuilder();

                // Get the metadata for column names
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Append column names
                for (int i = 1; i <= columnCount; i++) {
                    resultString.append(metaData.getColumnName(i)).append(" | ");
                }
                resultString.delete(resultString.length() - 3, resultString.length());

                // Process each row in the result set
                while (resultSet.next()) {
                    // Process each column in the row
                    for (int i = 1; i <= columnCount; i++) {
                        resultString.append(resultSet.getString(i)).append(" | ");
                    }
                    // Remove the trailing " | "
                    resultString.delete(resultString.length() - 3, resultString.length());
                }

                // Return the result string
                return resultString.toString();
            }
        }
    }

    public String nativeQueryResultAstring(JDBCDataSource dataSource,
                                           SQLQuery sqlQuery, ResultsType resultsType) throws Exception {
        if (ResultsType.TABULAR == resultsType) {
            return nativeQueryResultAsTabularString(dataSource, sqlQuery);
        }
        return nativeQueryResultAsJSONString(dataSource, sqlQuery);
    }

    private String nativeQueryResultAsJSONString(JDBCDataSource dataSource, SQLQuery sqlQuery)
            throws Exception {
        // Load the JDBC driver
        Class.forName(dataSource.getDriverClassName());

        try (java.sql.Connection connection = DriverManager.getConnection(
                dataSource.getJdbcUrl(),
                dataSource.getUsername(),
                dataSource.getPassword());
             PreparedStatement pstmt = connection.prepareStatement(sqlQuery.getStatement())) {

            // Bind parameters
            List<Parameter> parameters = sqlQuery.getParameters();
            int index = 1;
            for (Parameter parameter : parameters) {
                switch (parameter.getType()) {
                    case SQLTypes.INTEGER -> pstmt.setInt(index, Integer.parseInt(parameter.getValue()));
                    case SQLTypes.DOUBLE -> pstmt.setDouble(index, Double.parseDouble(parameter.getValue()));
                    case SQLTypes.BIG_DECIMAL -> pstmt.setBigDecimal(index, new BigDecimal(parameter.getValue()));
                    case SQLTypes.STRING -> pstmt.setString(index, parameter.getValue());
                    case SQLTypes.LONG -> pstmt.setLong(index, Long.parseLong(parameter.getValue()));
                    case SQLTypes.FLOAT -> pstmt.setFloat(index, Float.parseFloat(parameter.getValue()));
                    case SQLTypes.DATE -> pstmt.setDate(index, getDate(parameter));
                    case SQLTypes.TIME_STAMP -> pstmt.setTimestamp(index, getTimestamp(parameter));
                    default -> {
                    }
                }
                index++;
            }

            // Execute query and convert to JSON
            try (ResultSet resultSet = pstmt.executeQuery()) {
                JsonArray jsonArray = new JsonArray();
                Gson gson = new Gson();
                ResultSetMetaData metaData = resultSet.getMetaData();

                while (resultSet.next()) {
                    JsonObject row = new JsonObject();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = resultSet.getObject(i);

                        // Handle null values explicitly
                        if (value == null) {
                            row.add(columnName, null);
                        } else {
                            row.add(columnName, gson.toJsonTree(value));
                        }
                    }
                    jsonArray.add(row);
                }
                return gson.toJson(jsonArray);
            }
        }
    }

    public String nativeQueryResultAsJSONString(String query) {
        try {
            // Create a query instance
            Query nativeQuery = entityManager.createNativeQuery(query, Tuple.class);

            // Execute the query and get the results
            List<Tuple> results = nativeQuery.getResultList();

            // Create a JsonArray to hold all rows
            JsonArray jsonArray = new JsonArray();

            // Check if there are results
            if (!results.isEmpty()) {
                // Get the first row to retrieve column names
                Tuple firstRow = results.get(0);
                List<TupleElement<?>> columns = firstRow.getElements();

                // Process each row
                for (Tuple row : results) {
                    JsonObject jsonObject = new JsonObject();

                    // Add each column to the JsonObject using its actual name
                    for (TupleElement<?> column : columns) {
                        String columnName = column.getAlias(); // Get the actual column name
                        Object value = row.get(columnName);

                        // Handle null values
                        if (value == null) {
                            jsonObject.addProperty(columnName, (String) null);
                        } else {
                            jsonObject.addProperty(columnName, value.toString());
                        }
                    }
                    // Add the JsonObject to the JsonArray
                    jsonArray.add(jsonObject);
                }
            }
            // Convert the JsonArray to a JSON string using Gson
            Gson gson = new Gson();
            return gson.toJson(jsonArray);
        } catch (Exception e) {
            // Log the error
            e.printStackTrace(); // Replace with your logger
            return null;
        }
    }

    public String nativeQueryResultAsString(String query) {
        try {
            LOG.debug("Running native query: {}", query);

            // Create a query instance
            Query nativeQuery = entityManager.createNativeQuery(query, Tuple.class);

            // Execute the query and get the results
            List<Tuple> results = nativeQuery.getResultList();

            // Initialize the result string
            StringBuilder resultString = new StringBuilder();

            // Check if there are results
            if (!results.isEmpty()) {
                // Get the first row to retrieve column names
                Tuple firstRow = results.get(0);
                List<TupleElement<?>> columns = firstRow.getElements();

                // Append column names
                for (TupleElement<?> column : columns) {
                    resultString.append(column.getAlias()).append(" | ");
                }
                resultString.delete(resultString.length() - 3, resultString.length());
                resultString.append("\n");

                // Append data rows
                for (Tuple row : results) {
                    for (TupleElement<?> column : columns) {
                        resultString.append(row.get(column.getAlias())).append(" | ");
                    }
                    resultString.delete(resultString.length() - 3, resultString.length());
                    resultString.append("\n");
                }
            } else {
                resultString.append("No results found.\n");
            }

            return resultString.toString();
        } catch (Exception e) {
            LOG.error("Error running native query ", e);
        }
        return null;
    }

    /*
        public List<Showcase> findShowcasesByCategory(Category category, ActiveStatus activeStatus, int start, int limit) {
            try {
                return entityManager.createQuery("SELECT u from Showcase u WHERE u.publication.category =?1 AND " +
                                " u.activeStatus =?2 AND column =?3 LIMIT ?4",
                                Showcase.class).
                        setParameter(1, category).
                        setParameter(2, activeStatus).
                        setParameter(3, start).
                        setParameter(4, limit).
                        getResultList();
            } catch (Exception e) {
                return null;
            }
        }
    */

    public List<Article> findShowcasedPublications(int start, int limit) {
        try {
            return entityManager.createQuery("SELECT u from Publication u WHERE " +
                                    " u.activeStatus =?1 AND u.showcase =?2",
                            Article.class).
                    setParameter(1, ActiveStatus.ACTIVE).
                    setParameter(2, Showcase.YES).
                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> findInnovators(int start, int limit) {
        try {
            return entityManager.createQuery("SELECT u from User u WHERE u.showcase =?1",
                            User.class).
                    setParameter(1, Showcase.YES).
                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Article> findShowcasedByCategory(Category category, int start, int limit) {
        try {
            return entityManager.createQuery("SELECT u from Publication u WHERE u.category =?1 AND " +
                                    " u.activeStatus =?2 AND u.showcase =?3",
                            Article.class).
                    setParameter(1, category).
                    setParameter(2, ActiveStatus.ACTIVE).
                    setParameter(3, Showcase.YES).
                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Article> findPublicationsByCategory(Category category, int start, int limit, ActiveStatus activeStatus) {
        try {
            return entityManager.createQuery("SELECT u from Publication u WHERE u.category =?1 AND " +
                                    " u.activeStatus =?2",
                            Article.class).
                    setParameter(1, category).
                    setParameter(2, activeStatus).
                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Article> findPublications(int start, int limit, ActiveStatus activeStatus) {
        try {
            return entityManager.createQuery("SELECT u from Publication u WHERE " +
                                    " u.activeStatus =?1",
                            Article.class).
                    setParameter(1, activeStatus).
                    getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Category> fetchAllCategories() {
        return entityManager.createQuery("SELECT u from Category u ", Category.class).
                getResultList();
    }

    public List<User> fetchUsersWithSkill(Skill skill) {
        if (skill == null) {
            return new ArrayList<>();
        }
        String jpql = "SELECT DISTINCT u FROM User u JOIN u.skills s WHERE s = :skill";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("skill", skill);
        return query.getResultList();
    }

    public Feature findFeatureByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        try {
            return entityManager.createQuery("SELECT u from Feature u WHERE u.name =?1", Feature.class).
                    setParameter(1, name).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> findUsersByNames(String firstName, String lastName) {
        if (StringUtils.isBlank(firstName) && StringUtils.isBlank(lastName)) {
            return new ArrayList<>();
        }
        try {
            return entityManager.createQuery("SELECT u from User u WHERE u.firstName =?1 AND u.lastName =?2", User.class).
                    setParameter(1, firstName).
                    setParameter(2, lastName).
                    getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> findUsersByFirstOrLastName(String firstName, String lastName) {
        if (StringUtils.isBlank(firstName) && StringUtils.isBlank(lastName)) {
            return new ArrayList<>();
        }
        try {
            return entityManager.createQuery("SELECT u from User u WHERE u.firstName LIKE?1 OR u.lastName LIKE?2", User.class).
                    setParameter(1, "%" + firstName + "%").
                    setParameter(2, "%" + lastName + "%").
                    getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Article> findPublicationBySource(String source) {
        if (StringUtils.isBlank(source)) {
            return new ArrayList<>();
        }
        try {
            return entityManager.createQuery("SELECT u from Publication u WHERE u.source LIKE?1", Article.class).
                    setParameter(1, "%" + source + "%").
                    getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Suburb> fetchSuburbsForCity(City city) {
        if (city == null) {
            return new ArrayList<>();
        }
        return entityManager.createQuery("SELECT u from Suburb u WHERE u.city =?1", Suburb.class).
                setParameter(1, city).
                getResultList();
    }

    public List<Skill> fetchSkillsForCategory(Category category) {
        if (category == null) {
            return new ArrayList<>();
        }
        try {
            return entityManager.createQuery("SELECT u from Skill u WHERE u.category =?1", Skill.class).
                    setParameter(1, category).
                    getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Article> findPublicationByTitle(String title) {
        if (StringUtils.isBlank(title)) {
            return new ArrayList<>();
        }
        try {
            return entityManager.createQuery("SELECT u from Publication u WHERE u.title LIKE?1", Article.class).
                    setParameter(1, "%" + title + "%").
                    getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Article> findPublicationByName(String firstName, String lastName) {
        //TODO: implement method
        return null;
    }

    public GenericRepository<BaseEntity> getRepository() {
        return repository;
    }

    @Transactional
    public <T extends BaseEntity> void save(T entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity); // Save a new entity
        } else {
            entityManager.merge(entity); // Update an existing entity
        }
    }

    public static final String HS_SELECT_TABLES = "SELECT table_name" +
            " FROM information_schema.tables" +
            " order by table_name;";

    public static final String SELECT_TABLES = "SELECT table_name" +
            " FROM information_schema.tables" +
            " WHERE table_schema = '%s' or table_schema = 'public'" + //this is for postgres
            " order by table_name;";
    public static final String ORG = "org";
    public static final String NAME = "name";
    private static final Logger LOG = LoggerFactory.getLogger(RepositoryService.class);



}
