package org.unicrm.analytic.services.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unicrm.analytic.converter.DepartmentMapper;
import org.unicrm.analytic.converter.TicketMapper;
import org.unicrm.analytic.converter.UserMapper;
import org.unicrm.analytic.repositorys.DepartmentRepository;
import org.unicrm.analytic.repositorys.TicketRepository;
import org.unicrm.analytic.repositorys.UserRepository;
@Component
@RequiredArgsConstructor
@Getter
public class AnalyticFacade {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;
    private final DepartmentMapper departmentMapper;
    private final TicketMapper ticketMapper;
}
