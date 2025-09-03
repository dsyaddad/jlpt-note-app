package com.ds.jlptnoteapp.model.repository;

import com.ds.jlptnoteapp.model.entity.JlptExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JlptExampleRepository extends JpaRepository<JlptExample, Long>, JpaSpecificationExecutor<JlptExample> {

}
