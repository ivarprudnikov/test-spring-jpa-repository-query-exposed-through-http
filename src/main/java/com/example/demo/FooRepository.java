package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "foo")
public interface FooRepository extends JpaRepository<FooEntity, Long> {

    /**
     * Accessible over http://localhost:8080/foo/search/getByIdToProjected?id=1
     * @param id Long
     * @return Optional<ProjectedFooResult>
     */
    @Query(nativeQuery = true, value = "SELECT f.name as name, count(b.id) as barCount FROM foo f LEFT JOIN bar b ON b.foo_id = f.id WHERE f.id = :id")
    Optional<ProjectedFooResult> getByIdToProjected(Long id);
}



