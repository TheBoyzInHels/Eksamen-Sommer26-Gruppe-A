package app.persistence;

import app.entities.Carport;
import app.entities.Inquiry;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InquiryMapper {
    public static List<Inquiry> listInquiry(ConnectionPool connectionPool, User user) throws DatabaseException {
        List<Inquiry> inquiries = new ArrayList<>();
        String listUserSQL;

        if (user != null) {
            listUserSQL = "SELECT * FROM inquiries WHERE user_id = ?";
        } else {
            listUserSQL = "SELECT * FROM inquiries";
        }
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(listUserSQL);
        ) {
            ps.setInt(1, user.getId());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int inquiryId = rs.getInt("inquiry_id");
                String status = rs.getString("status");
                int userId = rs.getInt("user_id");
                int carportId = rs.getInt("carport_id");
                Date date = rs.getDate("date");
                int price = rs.getInt("price");

                inquiries.add(new Inquiry(inquiryId, status, carportId, userId, date, price));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl med databasen" + e.getMessage());
        }
        return inquiries;
    }

    public void actionInquiry(ConnectionPool connectionPool, Inquiry inquiry) {

    }

    public void findInquiry(ConnectionPool connectionPool, Inquiry inquiry) {

    }

    public void editInquiry(ConnectionPool connectionPool, Inquiry inquiry) {

    }

    public static void deleteInquiry(ConnectionPool connectionPool, int inquiryId) throws DatabaseException {
        String deleteSql = "DELETE FROM inquiries WHERE inquiry_id = ?";

        try
                (
                        Connection connection = connectionPool.getConnection();
                        PreparedStatement ps = connection.prepareStatement(deleteSql);
                ) {
            ps.setInt(1, inquiryId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error with deleteInquiry", e.getMessage());
        }

    }

    public static void createInquiry(ConnectionPool connectionPool, Inquiry inquiry, Carport carport, User user) throws DatabaseException {
        String sql = "INSERT INTO inquiries( status, user_id, carport_id, date, price) VALUES (?,?,?,?,?)";

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ) {
            ps.setString(1, inquiry.getStatus());
            ps.setInt(2, user.getId());
            ps.setInt(3, carport.getCarportId());
            ps.setDate(4, inquiry.getDate());
            ps.setInt(5, inquiry.getPrice());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Error with database", e.getMessage());
        }

    }

}
