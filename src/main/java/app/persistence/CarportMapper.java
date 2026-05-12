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

    public void deleteCarport (ConnectionPool connectionPool, Carport carport) {

    }

    public static void saveCarport (ConnectionPool connectionPool, Carport carport, Context ctx) throws DatabaseException{
        String sql = "INSERT INTO carports (user_id, amount_of_cars, length, width, has_shed, shed_width, shed_length, has_gutter) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try
                (
                        Connection connection = connectionPool.getConnection();
                        PreparedStatement ps = connection.prepareStatement(sql);
                ) {
            ps.setInt(1, ((User) ctx.sessionAttribute("currentUser")).getId());
            ps.setInt(2, carport.getAmountOfCars());
            ps.setInt(3, carport.getLength());
            ps.setInt(4, carport.getWidth());
            ps.setBoolean(5, carport.isHasShed());
            ps.setInt(6, carport.getShedWidth());
            ps.setInt(7, carport.getShedLength());
            ps.setBoolean(8, carport.isHasGutter());


            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error with saveCarport", e.getMessage());
        }
    }
}
