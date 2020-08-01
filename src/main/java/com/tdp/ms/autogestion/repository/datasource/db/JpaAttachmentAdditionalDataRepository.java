package com.tdp.ms.autogestion.repository.datasource.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAttachmentAdditionalData;

public interface JpaAttachmentAdditionalDataRepository extends JpaRepository<TblAttachmentAdditionalData, String> {

}
