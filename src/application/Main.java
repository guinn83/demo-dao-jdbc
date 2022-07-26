package application;

import db.DB;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: seller findById ===");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);


        System.out.println("\n=== TEST 2: seller findByDepartment ===");
        List<Seller> list = sellerDao.findByDepartment(2);
        list.forEach(System.out::println);


        System.out.println("\n=== TEST 3: seller findAll ===");
        list = sellerDao.findAll();
        list.forEach(System.out::println);


        System.out.println("\n=== TEST 4: seller update ===");
        seller.setEmail("alexgrey@gmail.com");
        sellerDao.update(seller);


        System.out.println("\n=== TEST 5: seller insert ===");
        long count = list.stream()
                .filter(e -> e.getEmail().equals("carl@gmail.com"))
                .count();
        if (count == 0) {
            seller = new Seller(null, "Carl Purple", "carl@gmail.com", new Date(sdf.parse("22/04/1985").getTime()), 3200.0, new Department(3, "Fashion"));
            sellerDao.insert(seller);
            System.out.println("Inserted! New seller id = " + seller.getId());
        } else {
            System.out.println("Seller already exists!");
        }


        System.out.println("\n=== TEST 6: seller deleteById ===");
        sellerDao.deleteById(15);

        DB.closeConnection();

    }
}
