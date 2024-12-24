package com.project.LMS.repository;

import com.project.LMS.dto.material.MaterialDto;
import com.project.LMS.entity.Material;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends CrudRepository<Material, Integer> {

    @Query("""
        SELECT new com.project.LMS.dto.material.MaterialDto(
            c.title AS courseName,
            m.title AS title,
            m.url AS url
        )
        FROM Material m
        JOIN m.course c
        WHERE c.id = :courseId
    """)
    List<MaterialDto> findMaterialsByCourseId(Integer courseId);
}
