package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.sql.SQLException;
import java.util.List;

public class Connect implements Command {

    private DataSet data;
    private DatabaseManager manager;
    private View view;

    public Connect(DataSet data, DatabaseManager manager, View view) {
        this.data = data;
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {
        List<String> parameters = data.getParameters(command);
        if (parameters.size() != 4) {
            view.write(ERROR_ENTERING_MESSAGE + "'connect|database|username|password'");
            return;
        }
        String databaseName = data.getTableName(command);
        List<String> values = data.getTableData(command);
        String userName = values.get(0);
        String password = values.get(1);
        try {
            manager.connect(databaseName, userName, password);
            view.write(String.format("You have сonnected to database '%s' successfully!", databaseName));
        } catch (SQLException e) {
            view.write(String.format("The connection to database '%s' for user '%s' is failed due to'%s'", databaseName, userName, e.getMessage()));
        }
    }
}