package org.unicrm.ticket.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unicrm.ticket.entity.TicketDepartment;
import org.unicrm.ticket.exception.ResourceNotFoundException;
import org.unicrm.ticket.mapper.TicketDepartmentMapper;
import org.unicrm.ticket.repository.TicketDepartmentRepository;

@Service
@RequiredArgsConstructor
public class TicketDepartmentService {

    private final TicketDepartmentMapper mapper;
    private final TicketDepartmentRepository departmentRepository;

    public TicketDepartment findDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
    }
}
