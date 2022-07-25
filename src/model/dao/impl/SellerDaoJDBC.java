package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {
    Statement st = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    private Connection con;

    public SellerDaoJDBC(Connection con) {
        this.con = con;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Seller findById(int id) {
        Seller seller = null;
        try {
            pst = con.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE seller.Id = ?");


            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                Department dep = new Department(rs.getInt("DepartmentId"), rs.getString("DepName"));
                seller = new Seller(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getDouble(5), dep);
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeConnection();
            DB.closeStatement(pst);
            DB.closeResultSet(rs);
        }

        return seller;
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
