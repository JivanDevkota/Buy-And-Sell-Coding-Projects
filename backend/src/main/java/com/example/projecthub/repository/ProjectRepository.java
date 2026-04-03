package com.example.projecthub.repository;

import com.example.projecthub.dto.project.PendingProjects;
import com.example.projecthub.model.Project;
import com.example.projecthub.model.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByLanguages_Id(Long languagesId);

    @Query("""
            select p from Project p join p.languages l
                        where l.id= :languageId
                                    order by p.viewCount desc 
            """)
    Page<Project> findTopByLanguage(@Param("languageId") Long languageId, Pageable pageable);

    Optional<Project> findByIdAndSellerId(Long projectId, Long userId);


    @Query("""
                    select distinct p
                    from Project  p
                    left join fetch p.languages
                    left join fetch p.tags
                    left join fetch p.category
                    left join fetch p.projectFiles
                    where p.seller.id= :sellerId
            """)
    List<Project> findAllBySellerId(@Param("sellerId") Long sellerId);


    @Query("""
                    select p from Project p
                    left join fetch p.languages
                    left join fetch p.tags
                    left join fetch p.category
                    left join fetch p.projectFiles
                    where p.id= :projectId
            """)
    Optional<Project> findByIdWithLanguages(Long projectId);

    @Query("""
                select distinct p 
                from Project p
                left join fetch p.category
                left join fetch p.languages
                left join fetch p.projectFiles
                where p.isActive=true 
            """)
    List<Project> findAllWithLanguagesAndCategory();

    @Query("""
                select distinct p 
                from Project p
                left join fetch p.category
                left join fetch p.languages
                left join fetch p.tags
                left join fetch p.projectFiles
                where p.id  = :projectId
            """)
    Optional<Project> findProjectDetailsById(@Param("projectId") Long projectId);

    @Query("""
            select new com.example.projecthub.dto.project.PendingProjects(
                p.id,
                p.title,
                s.username,
                c.name,
                p.createdAt,
                p.price
            )
            from Project p
            join p.seller s
            join p.category c
            where p.status = :status
            order by p.createdAt desc
            """)
    Page<PendingProjects> findAllPendingProjects(@Param("status") ProjectStatus status, Pageable pageable);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.tags WHERE p.id = :projectId")
    Optional<Project> findByIdWithTags(Long projectId);


    @Query("""
            select count(p)
                from Project p
                    WHERE p.seller.id = :sellerId
                AND p.isActive=true 
            """)
    Long getActiveProjectsBySeller(Long sellerId);

    Long countProjectBySeller_IdAndStatus(Long sellerId,ProjectStatus status);

    Long countProjectByStatus(ProjectStatus status);
    
    @Query("SELECT COALESCE(SUM(p.viewCount),0)  from Project p")
    Long getTotalViewsCount();

}
