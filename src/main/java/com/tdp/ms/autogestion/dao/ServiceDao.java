package com.tdp.ms.autogestion.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.stereotype.Repository;

import com.tdp.ms.autogestion.model.OAuth;
import com.tdp.ms.autogestion.util.Conexion;

@Repository
public class ServiceDao implements ServiceDaoInterface {

	
	@Override
	public OAuth getOauth(int id) {
		OAuth oAuth= new OAuth();
		PreparedStatement pstm = null;
		Conexion con = null;
		ResultSet rs =  null;
		String SQL = "select * from tbl_oauth";
		try {
			con = new Conexion();
			con.getConexion().setAutoCommit(false);
			pstm = con.getConexion().prepareStatement(SQL);			
			rs = pstm.executeQuery();
			while (rs.next()) {	    
				oAuth.setTokenKey(rs.getString(2));
				System.out.println(rs.getString(2));
                System.out.println(rs.getInt(1));
                System.out.println(": ");
                System.out.println(rs.getString(2));
            }		
			con.getConexion().commit();
			return oAuth;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}
	

}
