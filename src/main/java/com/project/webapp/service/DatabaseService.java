package com.project.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DatabaseService {
    DataSource dataSource;
    @Autowired
    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public boolean isServerRunning() {
        try(Connection connection = dataSource.getConnection()){
            return true;
        }
        catch (SQLException e){
            return false;
        }
    }
}
