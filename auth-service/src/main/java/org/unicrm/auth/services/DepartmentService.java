package org.unicrm.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unicrm.auth.dto.DepartmentDto;
import org.unicrm.auth.entities.Department;
import org.unicrm.auth.exceptions.ResourceNotFoundException;
import org.unicrm.auth.mappers.EntityDtoMapper;
import org.unicrm.auth.repositories.DepartmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public Department findDepartmentByTitle(String departmentTitle) {
        Department department = departmentRepository.findDepartmentByTitle(departmentTitle);
        if (department == null) {
            throw new ResourceNotFoundException(String.format("Department '%s' not found", departmentTitle));
        }
        return department;
    }

    public List<DepartmentDto> findAll() {
       return departmentRepository.findAll().stream().map(EntityDtoMapper.INSTANCE::toDto).collect(Collectors.toList());
    }
}
