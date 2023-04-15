package org.unicrm.ticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unicrm.ticket.dto.DepartmentDto;
import org.unicrm.ticket.entity.Department;
import org.unicrm.ticket.exception.ResourceNotFoundException;
import org.unicrm.ticket.mapper.TicketDepartmentMapper;
import org.unicrm.ticket.repository.TicketDepartmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketDepartmentService {

    private final TicketDepartmentMapper mapper;
    private final TicketDepartmentRepository departmentRepository;

    public Department findDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
    }

    public List<DepartmentDto> findAllDepartments() {
        return departmentRepository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
