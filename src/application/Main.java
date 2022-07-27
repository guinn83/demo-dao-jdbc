package application;

import db.DB;
import db.DbException;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ParseException {

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("=== TEST 1: department findById ===");
        Department dep = departmentDao.findById(6);
        System.out.println(dep);

        System.out.println("\n=== TEST 2: department findAll ===");
        List<Department> list = departmentDao.findAll();
        list.forEach(System.out::println);

        System.out.println("\n=== TEST 3: department update ===");
        dep.setName("Toys");
        departmentDao.update(dep);

        System.out.println("\n=== TEST 4: department insert ===");
        try {
            Department newDep = new Department(null, "Foods");
            departmentDao.insert(newDep);
            System.out.println("Inserted! New department id: " + newDep.getId());
        } catch (DbException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n=== TEST 5: department delete ===");
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        do {
            try {
                System.out.print("Enter the department ID to delete: ");
                int id = sc.nextInt();
                departmentDao.deleteById(id);
                System.out.println("Department deleted: " + id);
                exit = true;
            } catch (DbException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
                System.out.println();
            }
        } while (!exit);
        sc.close();
    }

    private static void sellerDaoTest() throws ParseException {
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
        try {
            seller = new Seller(null, "Carl Purple", "carl@gmail.com", new Date(sdf.parse("22/04/1985").getTime()), 3200.0, new Department(3, "Fashion"));
            sellerDao.insert(seller);
            System.out.println("Inserted! New seller id = " + seller.getId());
        } catch (DbException e) {
            System.out.println(e.getMessage());
        }


        System.out.println("\n=== TEST 6: seller deleteById ===");
        try {
            sellerDao.deleteById(15);
        } catch (DbException e) {
            System.out.println(e.getMessage());
        }

        DB.closeConnection();
    }
}
