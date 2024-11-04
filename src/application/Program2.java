package application;


import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {
    public static void main(String[] args) {
        
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("=== Teste 1: findById ===");
        Department department = departmentDao.findById(1);
        System.out.println(department);

        System.out.println("\n=== Teste 2: findAll ===");
        List<Department> list = departmentDao.findAll();
        for (Department obj : list) {
            System.out.println(obj);
        }

        System.out.println("\n=== Teste 3: insert ===");
        Department newDepartment = new Department(5, "Music");
        departmentDao.insert(5, "Music");
        System.out.println("Inserido! Novo id = " + newDepartment.getId());

        System.out.println("\n=== Teste 4: update ===");
        //department = departmentDao.findById(10);
        //department.setId(5);
        //departmentDao.update(department);
        //System.out.println("Atualização completa");

        System.out.println("\n=== Teste 5: delete ===");
        //departmentDao.deleteById(10);
        //System.out.println("Exclusão completa");
    }
}

