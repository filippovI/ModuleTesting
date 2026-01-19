package edu.innotech.JDBC.db;

import edu.innotech.JDBC.interfaces.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbClient {
    String url;

    public DbClient(String url) {
        this.url = "jdbc:h2:" + url;
    }

    public <T> List<T> select(String sql, RowMapper<T> mapper) {
        List<T> result = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                result.add(mapper.map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при SELECT: " + e.getMessage());
        }
        return result;
    }

    public int update(String sql, Object... params) {
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при UPDATE: " + e.getMessage());
        }
    }
}
