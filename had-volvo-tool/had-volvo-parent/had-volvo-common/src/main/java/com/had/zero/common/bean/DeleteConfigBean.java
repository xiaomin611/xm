package com.had.zero.common.bean;

import java.util.List;
import java.util.Map;

public class DeleteConfigBean {

    private List<Map<String,List<String>>> DELETE_COLUMNS;

    private List<String> DELETE_TABLES;

    public List<Map<String, List<String>>> getDELETE_COLUMNS() {
        return DELETE_COLUMNS;
    }

    public void setDELETE_COLUMNS(List<Map<String, List<String>>> DELETE_COLUMNS) {
        this.DELETE_COLUMNS = DELETE_COLUMNS;
    }

    public List<String> getDELETE_TABLES() {
        return DELETE_TABLES;
    }

    public void setDELETE_TABLES(List<String> DELETE_TABLES) {
        this.DELETE_TABLES = DELETE_TABLES;
    }
}
