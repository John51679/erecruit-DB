package Admin;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


//This class holds all the log data generated from users
//We create this class in order to output the data on the TableView
public class Log {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty username;
    private final SimpleStringProperty happened_at;
    private final SimpleStringProperty action_type;
    private final SimpleStringProperty success;
    private final SimpleStringProperty table_name;

    public Log(Integer id, String username, String happened_at, String action_type, String table_name, String success) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.happened_at = new SimpleStringProperty(happened_at);
        this.action_type = new SimpleStringProperty(action_type);
        this.success = new SimpleStringProperty(success);
        this.table_name = new SimpleStringProperty(table_name);
    }

    public int getId() {
        return id.get();
    }
    public String getUsername() {
        return username.get();
    }
    public String getHappened_at() {
        return happened_at.get();
    }
    public String getAction_type() {
        return action_type.get();
    }
    public String getTable_name() {
        return table_name.get();
    }
    public String getSuccess() {
        return success.get();
    }
}
