package com.example.projecthub.repository;

import com.example.projecthub.model.Language;
import com.example.projecthub.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language>findAllById(Iterable<Long> ids);
}
