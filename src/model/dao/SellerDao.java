package model.dao;

import model.entities.Seller;

import java.util.List;

public interface SellerDao {

    void insert(Seller obj);

    void update(Seller obj);

    void deleteById(int id);

    Seller findById(int id);

    List<Seller> findByDepartment(int departmentId);

    List<Seller> findAll();

}
