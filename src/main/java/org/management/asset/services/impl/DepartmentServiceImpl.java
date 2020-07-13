package org.management.asset.services.impl;

import org.management.asset.bo.Department;
import org.management.asset.dao.DepartmentRepository;
import org.management.asset.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department saveDepartment(Department department) {
        return this.departmentRepository.save(department);
    }

    @Override
    public boolean deleteDepartment(Long id) {
        this.departmentRepository.deleteById(id);
        return !this.departmentRepository.findById(id).isPresent();
    }

    @Override
    public Department getDepartment(Long id) {
        return this.departmentRepository.findById(id).orElse(null);
    }

    @Override
    public Department getDepartment(String name) {
        return this.departmentRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<Department> getDepartments() {
        return this.departmentRepository.findAll();
    }
}
