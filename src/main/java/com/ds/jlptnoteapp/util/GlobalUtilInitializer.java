package com.ds.jlptnoteapp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class GlobalUtilInitializer {

    @Value("${app.maint.mysql.container:notes-mysql}")
    private String container;

    @Value("${app.maint.mysql.db:notesdb}")
    private String dbName;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${app.maint.sql.filepath:script-db/02_dml.sql}")
    private String sqlFilePath;

    @PostConstruct
    public void init() {
        GlobalUtil.CONTAINER = container;
        GlobalUtil.DB_NAME = dbName;
        GlobalUtil.USER = user;
        GlobalUtil.PASSWORD = password;
        GlobalUtil.SQL_FILE_PATH = sqlFilePath;
    }
}
