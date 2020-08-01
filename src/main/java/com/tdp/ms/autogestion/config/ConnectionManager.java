package com.tdp.ms.autogestion.config;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {
	
	Connection conn;
	
	 public ConnectionManager() throws Exception {

		  try{

			  DataSourceConfig dsn = new DataSourceConfig();
			  conn = dsn.dataSource().getConnection();
			  
		  }catch(Exception e){
			  throw new Exception(e.getMessage());
		  }
//	     try{
//	    	 //conn = ds.getConnection();
//	     }catch (Exception sqle) {
//			throw new Exception(sqle.getMessage());
//		}

	  }
	  
	  public Connection getConexion(){
		  return this.conn;
		  
	  }
	  
	  public void closeConnection() throws Exception{
		  closeResources(conn, null, null,null);	  
	  }
	  public void closeResultSet(ResultSet rs) throws Exception{
		  closeResources(null, rs, null,null);	  
	  }
	  public void closeStatement(Statement stmt) throws Exception{
		  closeResources(null, null, stmt,null);	  
	  }
	  public void closeCallableStatement(CallableStatement cstmt) throws Exception{
		  closeResources(null, null, null,cstmt);	  
	  }
	  public void closeResources(Connection conn, ResultSet rs,Statement stmt,CallableStatement cstmt ) throws Exception{	 
		   if (conn != null) {
		        try {
		            conn.close();
		        } catch (SQLException ex) {
		            //System.out.println(ex.getMessage());
		            throw new Exception(ex.getMessage());
		        }
		    }
		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException ex) {
		        	//System.out.println(ex.getMessage());
		            throw new Exception(ex.getMessage());
		        }
		    }

		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException ex) {
		        	//System.out.println(ex.getMessage());
		            throw new Exception(ex.getMessage());
		        }
		    }
		    if (cstmt != null) {
		        try {
		            cstmt.close();
		        } catch (SQLException ex) {
		        	//System.out.println(ex.getMessage());
		            throw new Exception(ex.getMessage());
		        }
		    }
	  }

}
