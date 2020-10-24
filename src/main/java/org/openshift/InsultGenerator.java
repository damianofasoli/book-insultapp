package org.openshift;

import java.util.Random;
import java.sql.Connection;;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class InsultGenerator {
	public String generateInsult() {
		String words[][] = {{"Artless", "Bawdy", "Beslubbering"}, {"Base-court", "Bat-fowling", "Beef-witted"}, {"Apple-john", "Baggage", "Barnacle"}};
		String vowels = "AEIOU";
		String article = "an";
		String theInsult="";

		String databaseURL="jdbc:postgresql://";
		databaseURL+=System.getenv("POSTGRESQL_SERVICE_HOST");
		databaseURL+="/"+System.getenv("POSTGRESQL_DATABASE");

		String username=System.getenv("POSTGRESQL_USERNAME");
		String password=System.getenv("PGPASSWORD");

		try {
			Connection connection = DriverManager.getConnection(databaseURL, username, password);

			if (connection!=null) {
				String SQL= "select a.string as first, b.string as second, c.string as noun from short_adjective a, long_adjective b, noun c order by random() limit 1";
				Statement stmt=connection.createStatement();
				ResultSet rs=stmt.executeQuery(SQL);
				while(rs.next()) {
					if(vowels.indexOf(rs.getString("first").charAt(0))==-1) {
						article="a";
						theInsult=String.format("Thou art %s %s %s %s", article, rs.getString("first"),rs.getString("second"),rs.getString("noun"));					
					}
				}
				rs.close();
				connection.close();
			}	
		}
		catch(Exception e) {
			return String.format("Database connection problem: %s", e.getMessage());
		}
		return theInsult;

	}

}
