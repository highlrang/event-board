package com.project.application.registration.repository;

import com.project.application.registration.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long>, RegistrationRepositoryCustom {
}
