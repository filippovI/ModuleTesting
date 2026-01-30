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

    public void update(String sql, Object... params) {
        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при UPDATE: " + e.getMessage());
        }
    }

    public void createDB() {
        try (Connection con = DriverManager.getConnection(url)) {
            Statement stm = con.createStatement();
            stm.executeUpdate("DROP TABLE Department IF EXISTS");
            stm.executeUpdate("CREATE TABLE Department(ID INT PRIMARY KEY, NAME VARCHAR(255))");
            stm.executeUpdate("INSERT INTO Department VALUES(1,'Accounting')");
            stm.executeUpdate("INSERT INTO Department VALUES(2,'IT')");
            stm.executeUpdate("INSERT INTO Department VALUES(3,'HR')");
            stm.executeUpdate("DROP TABLE Employee IF EXISTS");
            stm.executeUpdate("CREATE TABLE Employee(" +
                    "ID INT PRIMARY KEY, " +
                    "NAME VARCHAR(255), " +
                    "DepartmentID INT, " +
                    "FOREIGN KEY (DepartmentID) REFERENCES Department(ID) ON DELETE CASCADE)");
            stm.executeUpdate("INSERT INTO Employee VALUES(1,'Pete',1)");
            stm.executeUpdate("INSERT INTO Employee VALUES(2,'Ann',1)");
            stm.executeUpdate("INSERT INTO Employee VALUES(3,'Liz',2)");
            stm.executeUpdate("INSERT INTO Employee VALUES(4,'Tom',2)");
            stm.executeUpdate("INSERT INTO Employee VALUES(5,'todd',3)");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
