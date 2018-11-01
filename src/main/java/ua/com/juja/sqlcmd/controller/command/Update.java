package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.InputWrapper;

import java.sql.SQLException;
import java.util.List;


public class Update implements Command {

    private DatabaseManager manager;

    public Update(DatabaseManager manager) throws DBConnectionException {
        this.manager = manager;
        if (!manager.isConnected()) {
            throw new DBConnectionException();
        }
    }

    @Override
    public String getStatusProcess() {
        int numberOfParameters = InputWrapper.getNumberOfParameters();
        if (numberOfParameters < 6 || numberOfParameters % 2 == 1) {
            return ERROR_ENTERING_MESSAGE + "'update|tableName|column1|value1|" +
                    "column2|value2|...|columnN|valueN'";
        }
        String tableName = InputWrapper.getTableName();
        List<String> values = InputWrapper.getTableData();
        List<String> columns = InputWrapper.getColumns();
        List<Object> rows = InputWrapper.getRows();
        String keyColumn = values.get(0);
        String keyValue = values.get(1);

        try {
            manager.update(tableName, columns, rows, keyColumn, keyValue);
            return "The row has been updated";
        } catch (SQLException e) {
            return String.format(SQL_EXCEPTION_MESSAGE, e.getMessage());
        }
    }
}
