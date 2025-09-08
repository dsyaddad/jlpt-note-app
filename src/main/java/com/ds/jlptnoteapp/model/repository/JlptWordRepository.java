package com.ds.jlptnoteapp.model.repository;

import com.ds.jlptnoteapp.model.entity.JlptWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JlptWordRepository extends JpaRepository<JlptWord, Long>, JpaSpecificationExecutor<JlptWord> { }
