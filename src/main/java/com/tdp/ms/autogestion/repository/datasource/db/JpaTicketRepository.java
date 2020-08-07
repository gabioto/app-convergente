package com.tdp.ms.autogestion.repository.datasource.db;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tdp.ms.autogestion.repository.datasource.db.entities.TblTicket;
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
@Repository
public interface JpaTicketRepository extends JpaRepository<TblTicket, Integer> {

	@Query
	(value = "SELECT t FROM TblTicket t "
			+ "WHERE t.tblCustomer.id.documentType = ?1 "
			+ "AND t.tblCustomer.id.documentNumber = ?2 "
			+ "AND t.tblCustomer.id.serviceCode = ?3 "
			+ "AND t.involvement = ?4 "
			+ "AND t.creationDate >= ?5 AND t.creationDate < ?6 "
			+ "ORDER BY t.idTicketTriage,t.eventTimeKafka DESC")
	List<TblTicket> findByCustomerAndUseCase(String docType, String docNumber,
			String reference, String involvement, LocalDateTime creationDate, LocalDateTime endDate);

	@Query
	(value = "SELECT t FROM TblTicket t "
			+ "WHERE t.tblCustomer.id.documentType = ?1 "
			+ "AND t.tblCustomer.id.documentNumber = ?2 "
			+ "AND t.tblCustomer.id.serviceCode = ?3 "
			+ "AND t.involvement = ?4 "			
			+ "ORDER BY t.idTicketTriage,t.eventTimeKafka DESC")
	List<TblTicket> findByCustomerAndUseCasePast(String docType, String docNumber,
			String reference, String involvement);
	
	@Query					
	(value = "SELECT t FROM TblTicket t "
			+ "WHERE t.idTicketTriage = ?1 ORDER BY t.eventTimeKafka DESC")
	Optional<List<TblTicket>> getTicketStatus(int idTicket);
	
	@Query					
	(value = "SELECT t FROM TblTicket t "
			+ "WHERE t.idTicket = ?1")
	Optional<List<TblTicket>> getTicket(int idTicket);
	
	@Query
	(value = "SELECT t FROM TblTicket t where t.idTicketTriage = ?1")
	Optional<List<TblTicket>> findByIdTicketTriage(int idTicketTriage);	

	@Modifying
	@Query
	(value = "UPDATE TblTicket t SET t.status = ?2, modifiedDateTicket = ?3 WHERE t.idTicketTriage = ?1")
	void updateTicketStatus(int idTicket, String status, LocalDateTime sysDate);

	
}