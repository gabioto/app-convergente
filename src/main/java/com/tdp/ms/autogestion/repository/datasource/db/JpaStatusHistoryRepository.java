package com.tdp.ms.autogestion.repository.datasource.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tdp.ms.autogestion.repository.datasource.db.entities.TblStatusHistory;

@Repository
public interface JpaStatusHistoryRepository extends JpaRepository<TblStatusHistory, Integer> {

}
