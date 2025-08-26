package com.ds.jlptnoteapp.model.repository;

import com.ds.jlptnoteapp.model.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long>, JpaSpecificationExecutor<Level> {}
