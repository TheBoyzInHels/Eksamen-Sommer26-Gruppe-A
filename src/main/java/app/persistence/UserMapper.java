package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;
import java.sql.*;

public class UserMapper {
    
    public static User login (String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM users where email = ? AND password = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String phoneNumber = rs.getString("phone_number");
                boolean administrator = rs.getBoolean("is_admin");


                return new User (id, email, password, firstName, lastName, phoneNumber, administrator);
            } else {
                throw new DatabaseException ("User doesn't match DB");
            }
        } catch (SQLException e) {
            throw new DatabaseException ("Connection to DB doesn't work", e.getMessage());
        }
    }
    
    public static User createUser(User user,ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO users (email, password, first_name, last_name, phone_number) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getPhoneNumber());

            ps.executeUpdate();

            return login(user.getEmail(), user.getPassword(), connectionPool);

        }catch (SQLException e) {
            throw new DatabaseException("Error with database", e.getMessage());
        }
    }
}
