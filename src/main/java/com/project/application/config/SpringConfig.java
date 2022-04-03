package com.project.application.config;

import com.project.application.file.service.FileService;
import com.project.application.file.service.FileServiceLocal;
import com.project.application.file.service.FileServiceS3;
import com.querydsl.jpa.impl.JPAQueryFactory;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class SpringConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }

    @Bean
    public LayoutDialect layoutDialect(){
        return new LayoutDialect();
    }

    @Bean
    public FileService fileService(){
        return new FileServiceLocal();
    }
}
