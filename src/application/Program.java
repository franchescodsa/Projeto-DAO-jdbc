package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("= = = = = = TEST 1: seller findById = = = =  = =");
		Seller seller= sellerDao.findById(3);
		
		System.out.println(seller);
		
		System.out.println("\n= = = = = = TEST 2: seller findByDepartment = = = =  = =");
		Department dep =  new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(dep);
		for(Seller obj : list) {
			System.out.println(obj);
		}
		System.out.println("\n= = = = = = TEST 3: seller findByALL = = = =  = =");
		
		list = sellerDao.findAll();
		for(Seller obj : list) {
			System.out.println(obj);
		}
		System.out.println("\n= = = = = = TEST 4: seller insert = = = =  = =");
		Seller newSeller = new Seller(null, "John Doe", "johndoe@example.com", new Date(), 3000.0, dep);
		sellerDao.insert(newSeller);
		System.out.println("Inserted! New id = " + newSeller.getId());
		
		System.out.println("\n= = = = = = TEST 5: seller upadte = = = =  = =");
		seller= sellerDao.findById(1);
		seller.setName("Marta Waine");
		sellerDao.update(seller);
		System.out.println("Update completo!");
		
		System.out.println("\n= = = = = = TEST 6: seller delete = = = =  = =");
		int id = 8; // ID do vendedor que deseja excluir
		sellerDao.deleteById(id);
		System.out.println("Vendedor excluído com sucesso!");

		
		
		
		

	}

}
