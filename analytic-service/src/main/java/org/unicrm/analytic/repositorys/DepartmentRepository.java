package org.unicrm.analytic.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unicrm.analytic.entities.Department;

import java.util.UUID;
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
