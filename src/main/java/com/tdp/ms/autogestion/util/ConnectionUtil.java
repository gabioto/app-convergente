package com.tdp.ms.autogestion.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.tdp.ms.autogestion.config.DataSourceConfig;
import com.tdp.ms.autogestion.exception.ErrorCategory;
import com.tdp.ms.autogestion.exception.GenericDomainException;

public class ConnectionUtil {

	Connection conn;

	public ConnectionUtil() throws GenericDomainException {

		try {

			DataSourceConfig dsn = new DataSourceConfig();
			conn = dsn.dataSource().getConnection();

		} catch (Exception e) {
			throw new GenericDomainException(ErrorCategory.UNEXPECTED, e.getMessage());
		}

	}

	public Connection getConexion() {
		return this.conn;

	}

	public void closeConnection() throws GenericDomainException {
		closeResources(conn, null, null, null);
	}

	public void closeResultSet(ResultSet rs) throws GenericDomainException {
		closeResources(null, rs, null, null);
	}

	public void closeStatement(Statement stmt) throws GenericDomainException {
		closeResources(null, null, stmt, null);
	}

	public void closeCallableStatement(CallableStatement cstmt) throws GenericDomainException {
		closeResources(null, null, null, cstmt);
	}

	public void closeResources(Connection conn, ResultSet rs, Statement stmt, CallableStatement cstmt)
			throws GenericDomainException {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException ex) {
				throw new GenericDomainException(ErrorCategory.UNEXPECTED, ex.getMessage());
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				throw new GenericDomainException(ErrorCategory.UNEXPECTED, ex.getMessage());
			}
		}

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException ex) {
				throw new GenericDomainException(ErrorCategory.UNEXPECTED, ex.getMessage());
			}
		}
		if (cstmt != null) {
			try {
				cstmt.close();
			} catch (SQLException ex) {
				throw new GenericDomainException(ErrorCategory.UNEXPECTED, ex.getMessage());
			}
		}
	}

}
