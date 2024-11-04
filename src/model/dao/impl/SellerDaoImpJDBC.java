package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoImpJDBC implements SellerDao {

	private Connection conn;
	
	public SellerDaoImpJDBC(Connection conn) {
		this.conn= conn;
	}
	
	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
	    try {
	        st = conn.prepareStatement(
	            "INSERT INTO seller "
	            + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
	            + "VALUES (?, ?, ?, ?, ?)", 
	            Statement.RETURN_GENERATED_KEYS);

	        st.setString(1, obj.getName());
	        st.setString(2, obj.getEmail());
	        st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
	        st.setDouble(4, obj.getBaseSalary());
	        st.setInt(5, obj.getDepartment().getId());

	        int rowsAffected = st.executeUpdate();

	        if (rowsAffected > 0) {
	            ResultSet rs = st.getGeneratedKeys();
	            if (rs.next()) {
	                int id = rs.getInt(1);
	                obj.setId(id); // Atualiza o ID do objeto Seller
	            }
	            rs.close();
	        } else {
	            throw new DbException("Erro inesperado! Nenhuma linha foi inserida.");
	        }
	    } 
	    catch (SQLException e) {
	        throw new DbException(e.getMessage());
	    } 
	    finally {
	        DB.closeStatement(st);
	    }
		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
	    try {
	        st = conn.prepareStatement(
	            "UPDATE seller "
	            + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
	            + "WHERE Id = ?");

	        st.setString(1, obj.getName());
	        st.setString(2, obj.getEmail());
	        st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
	        st.setDouble(4, obj.getBaseSalary());
	        st.setInt(5, obj.getDepartment().getId());
	        st.setInt(6, obj.getId()); // Define o ID para localizar o registro

	    st.executeUpdate();

	    } 
	    catch (SQLException e) {
	        throw new DbException(e.getMessage());
	    } 
	    finally {
	        DB.closeStatement(st);
	    }
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
	    try {
	        st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
	        
	        st.setInt(1, id);  // Define o ID do vendedor que será excluído
	        
	        int rowsAffected = st.executeUpdate();
	        
	        if (rowsAffected == 0) {
	            throw new DbException("Nenhum registro encontrado com o ID fornecido.");
	        }
	    } 
	    catch (SQLException e) {
	        throw new DbException(e.getMessage());
	    } 
	    finally {
	        DB.closeStatement(st);
	    }
		
	}

	/* buscar o vendedor pelo id especificado. Além disso, 
	 * vamos mapear os resultados para um objeto Seller
	 *  e associar o departamento (Department) ao qual ele pertence.*/
	@Override
	public Seller findById(Integer id) {
		PreparedStatement st =null;
		ResultSet rs = null;
		
		try {
			st= conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+"FROM seller INNER JOIN department " 
					+"ON seller.DepartmentId = department.Id "
					+"WHERE seller.Id = ? "
					);
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Department dep = instantiateDepartment(rs);
				
				Seller obj = instantiateSeller(rs, dep);
				
				
				
				return obj;
				
				
			}
			return null;
			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);		
		
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException{
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
	    ResultSet rs = null;
	    List<Seller> list = new ArrayList<>();
	    
	 // Mapa para armazenar instâncias de Department e evitar duplicação
	    Map<Integer, Department> map = new HashMap<>();
	    
	    try {
	        st = conn.prepareStatement(
	            "SELECT seller.*, department.Name as DepName "
	            + "FROM seller INNER JOIN department "
	            + "ON seller.DepartmentId = department.Id "
	            +"ORDER BY Name" );
	        
	       rs = st.executeQuery();
	        
	       while (rs.next()) {
	        	// Verifica se o Department já existe no mapa
	            Department dep = map.get(rs.getInt("DepartmentId"));
	            
	            // Se não existe, cria e adiciona ao mapa
	            if (dep == null) {
	                dep = instantiateDepartment(rs);
	                
	                map.put(rs.getInt("DepartmentId"), dep);
	            }
	             // Criando e populando o vendedor
	            Seller obj = instantiateSeller(rs, dep);
	           
	            
	            // Adicionando o vendedor à lista
	            list.add(obj);
	        }
	        return list;
	    } 
	    catch (SQLException e) {
	        throw new DbException(e.getMessage());
	    } 
	    finally {
	        DB.closeResultSet(rs);
	        DB.closeStatement(st);
	    }
	}

	
	/*Para implementar o método findByDepartment, 
	 * que busca vendedores (Seller) com base em um determinado departamento (Department)*/
	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
	    ResultSet rs = null;
	    List<Seller> list = new ArrayList<>();
	    
	 // Mapa para armazenar instâncias de Department e evitar duplicação
	    Map<Integer, Department> map = new HashMap<>();
	    
	    
	    try {
	        st = conn.prepareStatement(
	            "SELECT seller.*, department.Name as DepName "
	            + "FROM seller INNER JOIN department "
	            + "ON seller.DepartmentId = department.Id "
	            + "WHERE department.Id = ? "
	            +"ORDER BY Name" );
	        
	        st.setInt(1, department.getId());
	        rs = st.executeQuery();
	        
	       
	        
	        while (rs.next()) {
	        	// Verifica se o Department já existe no mapa
	            Department dep = map.get(rs.getInt("DepartmentId"));
	            
	            // Se não existe, cria e adiciona ao mapa
	            if (dep == null) {
	                dep = instantiateDepartment(rs);
	                
	                map.put(rs.getInt("DepartmentId"), dep);
	            }
	             // Criando e populando o vendedor
	            Seller obj = instantiateSeller(rs, dep);
	           
	            
	            // Adicionando o vendedor à lista
	            list.add(obj);
	        }
	        return list;
	    } 
	    catch (SQLException e) {
	        throw new DbException(e.getMessage());
	    } 
	    finally {
	        DB.closeResultSet(rs);
	        DB.closeStatement(st);
	    }

	}

}
