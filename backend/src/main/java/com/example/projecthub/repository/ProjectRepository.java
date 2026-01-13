package com.example.projecthub.repository;

import com.example.projecthub.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
        List<Project> findByLanguages_Id(Long languagesId);

        @Query("""
                select p from Project p join p.languages l
                            where l.id= :languageId
                                        order by p.viewCount desc 
                """)
        Page<Project> findTopByLanguage(@Param("languageId") Long languageId, Pageable pageable);
}
