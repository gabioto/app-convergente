package com.tdp.ms.autogestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tdp.ms.autogestion.repository.datasource.db.entities.TblLogData;


@Repository
public interface LogDataRepository extends JpaRepository<TblLogData, Integer> {

}
