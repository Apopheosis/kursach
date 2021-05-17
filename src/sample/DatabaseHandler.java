package sample;
import java.sql.*;
import java.sql.Connection;

public class DatabaseHandler extends configs {
    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://localhost:3306/trainingmanual?autoReconnect=true&useSSL=false&serverTimezone=Europe/London";

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection  = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void signUpUser(String firstName, String lastName, String userName, String password, String occupation) {
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" +
                Const.USER_LOGIN + "," + Const.USER_PASSWORD
                + "," + Const.USER_FIRSTNAME + "," + Const.USER_LASTNAME + "," + Const.USER_OCCUPATION + ")" + "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, userName);
            prSt.setString(2, password);
            prSt.setString(3, firstName);
            prSt.setString(4, lastName);
            prSt.setString(5, occupation);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getUser(User user) {
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USER_LOGIN + "=? AND " + Const.USER_PASSWORD + "=?";

        try {

            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getUserName());
            prSt.setString(2, user.getUserPass());

            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public ResultSet getAllUsers() {
        ResultSet resSet = null;

        String AllUsers = "SELECT * FROM " + Const.USER_TABLE;

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(AllUsers);
            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resSet;
    }

}
