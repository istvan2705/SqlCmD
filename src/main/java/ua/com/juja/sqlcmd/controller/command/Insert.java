package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.sql.SQLException;
import java.util.*;

public class Insert implements Command {
    private DataSet data;
    private DatabaseManager manager;
    private View view;

    public Insert(DataSet data, DatabaseManager manager, View view) {
        this.data = data;
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("insert|");
    }

    @Override
    public void process(String command) {
        List<String> parameters = data.getParameters(command);
        if (parameters.size() < 6 || parameters.size() % 2 == 1) {
            view.write(String.format(ERROR_ENTERING_MESSAGE + "'insert|tableName|column1|value1|" +
                    "column2|value2|...|columnN|valueN", command));
            return;
        }
        String tableName = data.getTableName(command);
        List<String> values = data.getTableData(command);
        Map<String, String> map = data.setValuesToColumns(values);
        try {
            manager.insert(tableName, map);
            view.write(String.format("Statement are added into the table '%s'", tableName));
        } catch (SQLException e) {
            view.write(String.format(SQL_EXCEPTION_MESSAGE, e.getMessage()));
        }
    }
}