package org.unicrm.auth.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unicrm.auth.dto.DepartmentDto;
import org.unicrm.auth.services.DepartmentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/departments")
    public List<DepartmentDto> getAllDepartment() {
        return departmentService.findAll();
    }
}