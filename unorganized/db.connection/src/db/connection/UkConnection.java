package db.connection;

import db.connection.query.UkStatement;
import java.sql.Connection;
import java.sql.SQLException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UkConnection {
	
	private final Connection connection;
	
	public UkStatement prepareStatement(String sql){
		try {
			return new UkStatement(connection.prepareStatement(sql));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
