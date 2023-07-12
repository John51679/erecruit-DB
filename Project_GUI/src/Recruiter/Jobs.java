package Recruiter;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

//This class holds all the jobs we currently have on our database
//We create this class in order to output the data on the TableView
public class Jobs {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty start_date;
    private final SimpleDoubleProperty salary;
    private final SimpleStringProperty position;
    private final SimpleStringProperty edra;
    private final SimpleStringProperty recruiter;
    private final SimpleStringProperty announce_date;
    private final SimpleStringProperty submission_date;
    private final SimpleStringProperty antikeim_title;

    public Jobs(Integer id, String start_date, Double salary, String position, String edra, String recruiter, String announce_date, String submission_date, String antikeim_title) {
        this.id = new SimpleIntegerProperty(id);
        this.start_date = new SimpleStringProperty(start_date);
        this.salary = new SimpleDoubleProperty(salary);
        this.position = new SimpleStringProperty(position);
        this.edra = new SimpleStringProperty(edra);
        this.recruiter = new SimpleStringProperty(recruiter);
        this.announce_date = new SimpleStringProperty(announce_date);
        this.submission_date = new SimpleStringProperty(submission_date);
        this.antikeim_title = new SimpleStringProperty(antikeim_title);
    }

    public int getId() {
        return id.get();
    }

    public String getStart_date() {
        return start_date.get();
    }

    public double getSalary() {
        return salary.get();
    }

    public String getPosition() {
        return position.get();
    }

    public String getEdra() {
        return edra.get();
    }

    public String getRecruiter() {
        return recruiter.get();
    }

    public String getAnnounce_date() {
        return announce_date.get();
    }

    public String getAntikeim_title() {
        return antikeim_title.get();
    }
    public String getSubmission_date() {
        return submission_date.get();
    }

    public String getStatus() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = formater.parse(getSubmission_date());
            Date date2 = formater.parse(String.valueOf(LocalDate.now()));

            if(date1.after(date2)){
                return "OPEN";
            }else {
                return "CLOSE";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
