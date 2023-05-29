package org.unicrm.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unicrm.auth.dto.DepartmentDto;
import org.unicrm.auth.services.DepartmentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
@Tag(name = "DepartmentController", description = "All possible operations with departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "Getting a list of departments")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LOCAL_ADMIN')")
    @GetMapping("/departments")
    public List<DepartmentDto> getAllDepartment() {
        return departmentService.findAll();
    }

    @Operation(summary = "Request to add or change a department")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LOCAL_ADMIN')")
    @PostMapping("/departments")
    public void addOrUpdateDepartment(@RequestBody DepartmentDto departmentDto) {
        departmentService.addOrUpdateDepartment(departmentDto);
    }

    @Operation(summary = "Request to delete a department")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LOCAL_ADMIN')")
    @DeleteMapping("/departments/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }

}
