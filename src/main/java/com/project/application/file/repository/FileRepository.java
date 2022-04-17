package com.project.application.file.repository;

import com.project.application.file.domain.GenericFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FileRepository extends JpaRepository<GenericFile, Long> {

    @Modifying
    @Query("DELETE FROM GenericFile f WHERE f.board is null")
    void deleteUnnecessary();
}
