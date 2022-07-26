package application;

import db.DB;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

import java.util.List;

public class Main {
    public static void main(String[] args) {


        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: seller findById ===");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);
        System.out.println();

        System.out.println("=== TEST 2: seller findByDepartment ===");
        List<Seller> list = sellerDao.findByDepartment(2);
        list.forEach(System.out::println);
        System.out.println();

        System.out.println("=== TEST 3: seller update ===");
        seller.setEmail("alexg@gmail.com");
        sellerDao.update(seller);
        System.out.println();


/*
        System.out.println("=== TEST 2: seller deleteById ===");
        sellerDao.deleteById(12);
*/
        DB.closeConnection();

    }
}
