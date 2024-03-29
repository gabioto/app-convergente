package com.tdp.ms.autogestion.repository.datasource.db;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.tdp.ms.autogestion.repository.datasource.db.entities.TblAttachmentAdditionalData;
/**
 * Class: TrazabilidadpruebaServiceImpl. <br/>
 * <b>Copyright</b>: &copy; 2019 Telef&oacute;nica del Per&uacute;<br/>
 * <b>Company</b>: Telef&oacute;nica del Per&uacute;<br/>
 *
 * @author Telef&oacute;nica del Per&uacute; (TDP) <br/>
 *         <u>Service Provider</u>: Everis Per&uacute; SAC (EVE) <br/>
 *         <u>Developed by</u>: <br/>
 *         <ul>
 *         <li>Developer name</li>
 *         </ul>
 *         <u>Changes</u>:<br/>
 *         <ul>
 *         <li>YYYY-MM-DD Creaci&oacute;n del proyecto.</li>
 *         </ul>
 * @version 1.0
 */
@EnableJpaRepositories(considerNestedRepositories = true)
public interface JpaAttachmentAdditionalDataRepository extends JpaRepository<TblAttachmentAdditionalData, String> {

	@Query					  
	(value = "SELECT add FROM TblAttachmentAdditionalData add "
			+ "WHERE tblAttachment.idAttachment = ?1 AND add.keyAttachmentAdditional = ?2")
	Optional<List<TblAttachmentAdditionalData>> getValue(int idAttachment, String field);
	
}
