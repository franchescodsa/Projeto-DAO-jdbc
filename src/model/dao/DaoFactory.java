package model.dao;

import model.dao.impl.SellerDaoImpJDBC;

public class DaoFactory {
 public static SellerDao createSellerDao() {
	 return new SellerDaoImpJDBC();
 }
}
