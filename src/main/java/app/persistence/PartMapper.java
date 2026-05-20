package app.persistence;

import app.entities.Part;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PartMapper {
    public static ArrayList<Part> getAvailableParts(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM parts";
        ArrayList<Part> availableParts = new ArrayList<>();
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("part_id");
                    String partName = rs.getString("part_name");
                    String partDescription = rs.getString("part_description");
                    int partPricer = rs.getInt("part_price");
                    int partLength = rs.getInt("part_length");

                    availableParts.add(new Part(id, partName, partDescription, partPricer, partLength));
                }
            if (availableParts.isEmpty()) {
                throw new DatabaseException("Can't find parts in DB");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Connection to DB doesn't work", e.getMessage());
        }
        return availableParts;
    }
}
