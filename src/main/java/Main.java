import java.sql.*;

public class Main {

    static final String DB_URL = "jdbc:mysql://localhost/jdbc_connection?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static final String USERNAME = "root";
    static final String PASSWORD = "a1b2c3d4e5";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //createTableOwners(connection);
            //createTableCars(connection);
            //executeInsertIntoOwners(connection);
            //executeInsertIntoCars(connection);
            //executeRead(connection);
            //executeDelete(connection);
            executeUpdate(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Task 3
    public static void createTableOwners(Connection connection) throws SQLException {
        try {
            System.out.println("Executing CREATE statement");
            Statement stmt = connection.createStatement();
            //owner_id, first name, last name and address.
            //don't forget commas, they are a crucial part of the sql queries
            String sql = "CREATE TABLE owners " +
                    "(owner_id INT not NULL AUTO_INCREMENT, " +
                    " first_name VARCHAR(255), " +
                    " last_name VARCHAR(255), " +
                    " address VARCHAR(255)," +
                    " PRIMARY KEY ( owner_id ))";
            stmt.executeUpdate(sql);
            System.out.println("Created table owners in given database...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Task 3
    public static void createTableCars(Connection connection) throws SQLException {
        try {
            System.out.println("Executing CREATE statement");
            Statement stmt = connection.createStatement();
            //don't forget commas, they are a crucial part of the sql queries!
            //car_id, name, registration and owner_id
            String sql = "CREATE TABLE cars " +
                    "(car_id INT not NULL AUTO_INCREMENT, " +
                    " name VARCHAR(255), " +
                    " registration VARCHAR(255), " +
                    "owner_id INT, " +  //you always need to add the foreign_key asa column before you specify it what it is
                    " PRIMARY KEY ( car_id )," +
                    " FOREIGN KEY ( owner_id ) REFERENCES owners(owner_id))";
            stmt.executeUpdate(sql);
            System.out.println("Created table cars in given database...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Task 4
    public static void executeInsertIntoOwners(Connection connection) throws SQLException {
        System.out.println("Executing INSERT statement int owners table");

        //owner_id, first name, last name and address.
        String insert = "INSERT INTO owners (first_name, last_name, address ) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(insert);

        String[] firstNames = {"Steven", "Tani", "June"};
        String[] lastNames = {"McGarrett", "Rey", "Reigns"};
        String[] addresses = {"Oahu, Hawaii", "Los Angeles, California", "New York City, New York"};

        for (int i = 0; i < 3; i++) {
            statement.setString(1, firstNames[i]);
            statement.setString(2, lastNames[i]);
            statement.setString(3, addresses[i]);

            int count = statement.executeUpdate();
            System.out.println("Rows affected " + (i + 1) + ": " + count);
        }

        statement.close();
    }

    public static void executeInsertIntoCars(Connection connection) throws SQLException {
        System.out.println("Executing INSERT statement int cars table");

        //car_id, name, registration and owner_id
        String insert = "INSERT INTO cars (name, registration, owner_id ) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(insert);

        String[] names = {"RMC", "Chevrolet", "Suzuki"};
        String[] registrations = {"2018/2019", "2022/2023", "2021/2022"};
        int[] ownerIDs = {1, 3, 2};

        for (int i = 0; i < 3; i++) {
            statement.setString(1, names[i]);
            statement.setString(2, registrations[i]);
            statement.setInt(3, ownerIDs[i]);

            int count = statement.executeUpdate();
            System.out.println("Rows affected " + (i + 1) + ": " + count);
        }

        statement.close();
    }

    //Task 5 - Implement query that will read name of car and name of owner of car with id 1
    public static void executeRead(Connection connection) throws SQLException {
        System.out.println("Executing READ statement");
        Statement stmt = connection.createStatement();
        String sql = " SELECT c.name AS car_name, CONCAT(o.first_name, \" \", o.last_name) AS owner_name\n" +
                "FROM cars c\n" +
                "JOIN owners o ON c.owner_id = o.owner_id\n" +
                "WHERE c.car_id = 1;";

        ResultSet resultSet = stmt.executeQuery(sql);
        //even though, one record is being retrieved, this next function is still very important and that is because
        //it will put the cursor to point to the first row of the resultSet
        if (resultSet.next()) {
            String car_name = resultSet.getString("car_name");
            String owner_name = resultSet.getString("owner_name");
            System.out.println("The car " + car_name + " with the ID 1 belongs to " + owner_name + ".");
        }
        resultSet.close();
    }

    //Task 6 - Implement query that will delete one row
    public static void executeDelete(Connection connection) throws SQLException {
        System.out.println("Executing DELETE statement");
        String deletion = "DELETE FROM cars WHERE car_id = ?";
        PreparedStatement stmt = connection.prepareStatement (deletion);
        stmt.setInt(1, 3);
        int count = stmt.executeUpdate();
        System.out.println("The deletion has finished. Number of the affected rows is " + count);
        stmt.close();
    }

    //Task 7 - Implement one update query
    public static void executeUpdate(Connection connection) throws SQLException {
        System.out.println("Executing UPDATE statement");
        String update = "UPDATE owners SET address = ? WHERE first_name = ? AND last_name = ?";
        PreparedStatement stmt = connection.prepareStatement(update);
        stmt.setString(1, "Bern, Switzerland");
        stmt.setString(2, "Steven");
        stmt.setString(3, "McGarrett");
        int count =  stmt.executeUpdate();
        System.out.println("Records affected by UPDATE:" + count);
        stmt.close();
    }



}






