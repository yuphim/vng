package com.vng.adsnew.db.dao;

import java.sql.Connection;

public interface SMSMappingDAO {
	public String convertMOIDMappingToRequestID(Connection connection,
			int requestIDMapping) throws Exception;

}
