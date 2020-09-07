package com.tdp.ms.autogestion.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.util.ConnectionUtil;

@Repository
public class ServiceDao implements ServiceDaoInterface {

	private static final Log log = LogFactory.getLog(ServiceDao.class);

	@Override
	public OAuth getOauth(int id) throws Exception {
		OAuth oAuth = new OAuth();
		PreparedStatement pstm = null;
		ConnectionUtil conn = null;
		ResultSet rs = null;
		String SQL = "select * from tbl_oauth";
		try {
			conn = new ConnectionUtil();
			conn.getConexion().setAutoCommit(false);
			pstm = conn.getConexion().prepareStatement(SQL);
			rs = pstm.executeQuery();
			while (rs.next()) {
				oAuth.setTokenKey(rs.getString(2));
				log.info(rs.getString(2));
				log.info(rs.getInt(1));
				log.info(": ");
				log.info(rs.getString(2));
			}
			conn.getConexion().commit();
			return oAuth;

		} catch (Exception e) {
			throw e;
		} finally {
			if (conn != null) {
				conn.closeConnection();
			}

			if (rs != null) {
				rs.close();
			}
		}
	}

}
