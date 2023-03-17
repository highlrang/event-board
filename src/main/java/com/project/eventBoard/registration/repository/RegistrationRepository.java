package com.project.eventBoard.registration.repository;

import com.project.eventBoard.registration.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long>, RegistrationRepositoryCustom {
}
