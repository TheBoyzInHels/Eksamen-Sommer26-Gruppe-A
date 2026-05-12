package app.persistence;

import app.entities.Carport;
import app.entities.User;
import app.exceptions.DatabaseException;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CarportMapper {

    public void listCarports(ConnectionPool connectionPool) {

    }

    public void actionCarport(ConnectionPool connectionPool, String action) {

    }

    public void findCarport(ConnectionPool connectionPool, Carport carport) {

    }

    public void editCarport(ConnectionPool connectionPool, Carport carport) {

    }

    public static void deleteCarport (ConnectionPool connectionPool, Context ctx, int carportId) throws DatabaseException{
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

    public static void saveCarport (ConnectionPool connectionPool, Carport carport, Context ctx) throws DatabaseException{
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
