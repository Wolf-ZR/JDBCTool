package com.zhang.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface CallBack {
     Object doExecute(Connection conn, PreparedStatement statement, ResultSet rs);
}
