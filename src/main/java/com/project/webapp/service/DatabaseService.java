package com.project.webapp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DatabaseService {
    DataSource dataSource;
    private static final Logger logger = LogManager.getLogger(DatabaseService.class);
    @Autowired
    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public boolean isServerRunning() {
        logger.debug("isServerRunning method entered");
        try(Connection connection = dataSource.getConnection()){
            logger.info("connection successful");
            return true;
        }
        catch (SQLException e){
            logger.error("encountered the following error {} while trying to connect to database",e.getMessage());
            return false;
        }
    }
}
