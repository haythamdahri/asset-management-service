package org.management.asset.services;

import org.management.asset.bo.Department;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface DepartmentService {

    Department saveDepartment(Department department);

    boolean deleteDepartment(Long id);

    Department getDepartment(Long id);

    Department getDepartment(String name);

    List<Department> getDepartments();

}
