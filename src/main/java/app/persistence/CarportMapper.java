package app.persistence;

import app.entities.Carport;
import app.entities.User;
import app.exceptions.DatabaseException;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarportMapper {

    public static List<Carport> listCarports(ConnectionPool connectionPool, User user) throws DatabaseException {
        List<Carport> carports = new ArrayList<>();
        String listUserSQL;

        if (user != null) {
            listUserSQL = "SELECT * FROM carports WHERE user_id = ?";
        } else {
            listUserSQL = "SELECT * FROM carports";
        }
        try
                (
                        Connection connection = connectionPool.getConnection();
                        PreparedStatement ps = connection.prepareStatement(listUserSQL);
                ) {
            ps.setInt(1, user.getId());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int carportId = rs.getInt("carport_id");
                int amountOfCars = rs.getInt("amount_of_cars");
                int carportLength = Integer.parseInt(rs.getString("carport_length"));
                int carportWidth = Integer.parseInt(rs.getString("carport_width"));
                boolean hasShed = rs.getBoolean("has_shed");
                int shedLength = Integer.parseInt(rs.getString("shed_length"));
                int shedWidth = Integer.parseInt(rs.getString("shed_width"));
                boolean hasGutter = rs.getBoolean("has_gutter");
                int userId = rs.getInt("user_id");


                carports.add(new Carport(carportId, amountOfCars, carportLength, carportWidth, hasShed, shedLength, shedWidth, hasGutter, userId));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error with finding carports", e.getMessage());
        }
        return carports;
    }


    public void actionCarport(ConnectionPool connectionPool, String action) {

    }

    public static Carport findCarport(ConnectionPool connectionPool, int carportId) throws DatabaseException {
        Carport carport = null;
        String findSQL = "SELECT * FROM carports WHERE carport_id = ?";

        try
                (
                        Connection connection = connectionPool.getConnection();
                        PreparedStatement ps = connection.prepareStatement(findSQL);
                ) {
            ps.setInt(1, carportId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("carport_id");
                int amountOfCars = rs.getInt("amount_of_cars");
                int carportLength = Integer.parseInt(rs.getString("carport_length"));
                int carportWidth = Integer.parseInt(rs.getString("carport_width"));
                boolean hasShed = rs.getBoolean("has_shed");
                int shedLength = Integer.parseInt(rs.getString("shed_length"));
                int shedWidth = Integer.parseInt(rs.getString("shed_width"));
                boolean hasGutter = rs.getBoolean("has_gutter");
                int userId = rs.getInt("user_id");


                carport = new Carport(id, amountOfCars, carportLength, carportWidth, hasShed, shedLength, shedWidth, hasGutter, userId);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error with saveCarport", e.getMessage());
        }
        return carport;
    }

    public static void editCarport(ConnectionPool connectionPool, Carport carport) throws DatabaseException {
        String editSQL = " UPDATE carports SET carport_length = ?, carport_width = ? WHERE carport_id = ?";

        try
                (
                        Connection connection = connectionPool.getConnection();
                        PreparedStatement ps = connection.prepareStatement(editSQL);
                ) {
            ps.setInt(1, carport.getLength());
            ps.setInt(2, carport.getWidth());
            ps.setInt(3, carport.getCarportId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error with saveCarport", e.getMessage());
        }

    }

    public static void deleteCarport(ConnectionPool connectionPool, int carportId) throws DatabaseException {
        String deleteSql = "DELETE FROM carports WHERE carport_id = ?";

        try
                (
                        Connection connection = connectionPool.getConnection();
                        PreparedStatement ps = connection.prepareStatement(deleteSql);
                ) {
            ps.setInt(1, carportId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error with saveCarport", e.getMessage());
        }

    }

    public static Carport findNewestCarport(ConnectionPool connectionPool, Context ctx) throws DatabaseException {
        Carport carport = null;
        String findSQL = "SELECT * FROM carports ORDER BY carport_id DESC LIMIT 1;";

        try
                (
                        Connection connection = connectionPool.getConnection();
                        PreparedStatement ps = connection.prepareStatement(findSQL);
                ) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("carport_id");
                int amountOfCars = rs.getInt("amount_of_cars");
                int carportLength = Integer.parseInt(rs.getString("carport_length"));
                int carportWidth = Integer.parseInt(rs.getString("carport_width"));
                boolean hasShed = rs.getBoolean("has_shed");
                int shedLength = Integer.parseInt(rs.getString("shed_length"));
                int shedWidth = Integer.parseInt(rs.getString("shed_width"));
                boolean hasGutter = rs.getBoolean("has_gutter");
                int userId = rs.getInt("user_id");


                carport = new Carport(id, amountOfCars, carportLength, carportWidth, hasShed, shedLength, shedWidth, hasGutter, userId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error with saveCarport", e.getMessage());
        }
        return carport;
    }

    public static void saveCarport(ConnectionPool connectionPool, Carport carport, Context ctx) throws DatabaseException {
        String sql = "INSERT INTO carports (amount_of_cars, carport_length, carport_width, has_shed, shed_width, shed_length, has_gutter, user_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try
                (
                        Connection connection = connectionPool.getConnection();
                        PreparedStatement ps = connection.prepareStatement(sql);
                ) {
            ps.setInt(1, carport.getAmountOfCars());
            ps.setInt(2, carport.getLength());
            ps.setInt(3, carport.getWidth());
            ps.setBoolean(4, carport.isHasShed());
            ps.setInt(5, carport.getShedWidth());
            ps.setInt(6, carport.getShedLength());
            ps.setBoolean(7, carport.isHasGutter());
            ps.setInt(8, ((User) ctx.sessionAttribute("currentUser")).getId());


            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error with saveCarport", e.getMessage());
        }
    }
}
