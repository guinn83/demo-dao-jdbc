package model.dao.impl;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private final Connection con;

    public DepartmentDaoJDBC(Connection con) {
        this.con = con;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;

        try{
            st = con.prepareStatement("" +
                    "INSERT INTO department " +
                    "(Name) " +
                    "VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("No rows affected");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("" +
                    "UPDATE department " +
                    "SET Name = ? " +
                    "WHERE Id = ?");

            pst.setString(1, obj.getName());
            pst.setInt(2, obj.getId());

            int rowsAffected = pst.executeUpdate();

            System.out.println("Done! Updated row : " + rowsAffected);

        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
        }
    }

    @Override
    public void deleteById(int id) {
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("" +
                    "DELETE FROM department " +
                    "WHERE Id = ?");

            pst.setInt(1, id);

            int rowsAffected = pst.executeUpdate();

            System.out.println("Done! Deleted row : " + rowsAffected);

        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
        }
    }

    @Override
    public Department findById(int id) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement("" +
                    "SELECT * " +
                    "FROM department " +
                    "WHERE Id = ?");

            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                return new Department(id, rs.getString("Name"));
            }

            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement("" +
                    "SELECT * " +
                    "FROM department " +
                    "ORDER BY Name");

            rs = pst.executeQuery();

            List<Department> list = new ArrayList<>();

            while (rs.next()) {
                Department dep = new Department(rs.getInt("Id"), rs.getString("Name"));
                list.add(dep);
            }

            return list;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
            DB.closeResultSet(rs);
        }
    }
}
