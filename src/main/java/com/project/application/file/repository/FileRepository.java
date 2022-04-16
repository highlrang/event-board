package com.project.application.file.repository;

import com.project.application.file.domain.GenericFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<GenericFile, Long> {
}
