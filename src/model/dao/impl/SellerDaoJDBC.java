package model.dao.impl;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {
   

    private final Connection con;

    public SellerDaoJDBC(Connection con) {
        this.con = con;
    }

    @Override
    public void insert(Seller obj) {
        PreparedStatement st = null;

        try{
            st = con.prepareStatement("" +
                    "INSERT INTO seller " +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                    "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, (Date) obj.getBirthDate());
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                while (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Done! New seller Id = " + id);
                }
            } else {
                System.out.println("No rows affected");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("" +
                    "UPDATE seller " +
                    "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                    "WHERE Id = ?");

            pst.setString(1, obj.getName());
            pst.setString(2, obj.getEmail());
            pst.setDate(3, (Date) obj.getBirthDate());
            pst.setDouble(4, obj.getBaseSalary());
            pst.setInt(5, obj.getDepartment().getId());

            pst.setInt(6, obj.getId());

            int rowsAffected = pst.executeUpdate();

            System.out.println("Done! Updated row : " + rowsAffected);

        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
//            DB.closeConnection();
            DB.closeStatement(pst);
        }
    }

    @Override
    public void deleteById(int id) {
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("" +
                    "DELETE FROM seller " +
                    "WHERE Id = ?");

            pst.setInt(1, id);

            int rowsAffected = pst.executeUpdate();

            System.out.println("Done! Deleted row : " + rowsAffected);

        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
//            DB.closeConnection();
            DB.closeStatement(pst);
        }
    }

    @Override
    public Seller findById(int id) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement("" +
                    "SELECT seller.*, department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE seller.Id = ?");

            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                return instatiateSeller(rs, dep);
            }

            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
//            DB.closeConnection();
            DB.closeStatement(pst);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(int depId) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement("" +
                    "SELECT *, department.Name as DepName  " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE DepartmentId = ? " +
                    "ORDER BY seller.Name");


            pst.setInt(1, depId);
            rs = pst.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department dep = map.get(depId);
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(depId, dep);
                }

                list.add(instatiateSeller(rs, dep));
            }

            return list;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
//            DB.closeConnection();
            DB.closeStatement(pst);
            DB.closeResultSet(rs);
        }
    }

    private Seller instatiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt(1));
        seller.setName(rs.getString(2));
        seller.setEmail(rs.getString(3));
        seller.setBirthDate(rs.getDate(4));
        seller.setBaseSalary(rs.getDouble(5));
        seller.setDepartment(dep);
        return seller;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department(rs.getInt("DepartmentId"), rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
