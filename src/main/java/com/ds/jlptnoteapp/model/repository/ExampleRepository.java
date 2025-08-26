package com.ds.jlptnoteapp.model.repository;

import com.ds.jlptnoteapp.model.entity.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long>, JpaSpecificationExecutor<Example> {}