package org.task1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;



public class DBAccess {

	public enum SearchParameter { NAME, CITY, INDEX, NONE };

	public final static int HOTEL = 0;
	public final static int AUTO = 1;

	private String url = "jdbc:postgresql://dumbo.inf.h-brs.de/demouser";
	
	private Connection conn;
	
	public DBAccess() {   
		
	} 

	public void openConnection(){
		  try {
			DriverManager.registerDriver( new org.postgresql.Driver() ); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		  Properties props = new Properties();
		  props.setProperty("user","demouser");
		  props.setProperty("password","demouser");

		  try {
			 this.conn = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public List<Hotel> getObjects(SearchParameter searchParameterType, String value  ){
		Statement st;
		ResultSet rs;
		List<Hotel> result = new ArrayList<>();

		String searchParameter = switch (searchParameterType) {
            case INDEX -> "index";
            case CITY -> "city";
            case NAME -> "name";
			case NONE -> "";
        };

        if ("*".equals(value) ) value = "";

		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM buchungsystem.hotel WHERE buchungsystem.hotel." + searchParameter + " ilike " + "\'%" + value + "%\'");
			while (rs.next() ) {
				int index = Integer.parseInt(rs.getString( 1 ));
				String name = rs.getString( 2 );
				String city = rs.getString( 3 );

				Hotel hotel = new Hotel(index, name, city);
				result.add(hotel);
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void closeConnection(){
		   try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


}
