package org.unicrm.analytic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unicrm.analytic.converter.DepartmentMapper;
import org.unicrm.analytic.dto.DepartmentFrontDto;
import org.unicrm.analytic.entities.Department;
import org.unicrm.analytic.exceptions.ResourceNotFoundException;
import org.unicrm.analytic.repositorys.DepartmentRepository;
import org.unicrm.lib.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public Department findById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Департамент c id " + id + " не найден в базе"));
    }

    public List<DepartmentFrontDto> getDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::fromEntityToFrontDto)
                .collect(Collectors.toList());
    }

    public Department departmentSaveOrUpdate(UserDto dto) {
        Department department;
        if (!departmentRepository.existsById(dto.getDepartmentId())) {
            department = departmentMapper.fromUserDto(dto);
           departmentRepository.save(department);
        } else {
            department = findById(dto.getDepartmentId());
            if (dto.getDepartmentTitle() != null) {
                department.setTitle(dto.getDepartmentTitle());
                departmentRepository.save(department);
            }
        }
        return department;
    }
}
