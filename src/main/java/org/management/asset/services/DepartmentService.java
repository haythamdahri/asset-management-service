package org.management.asset.services;

import org.management.asset.bo.Department;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface DepartmentService {

    Department saveDepartment(Department department);

    boolean deleteDepartment(String id);

    Department getDepartment(String id);

    Department getDepartmentByName(String name);

    List<Department> getDepartments();

}
