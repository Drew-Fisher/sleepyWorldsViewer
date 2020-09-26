package SleepyWorldsViewer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller implements Initializable {

    //url for the lolesports.com twitch worlds stream
    private String twitch_uri = "https://lolesports.com/live/worlds/riotgames";

    //Declares instance of Desktop
    private Desktop desktop;

    //intializes arrays used for setting the items of the combo boxes
    private String[] hours ={"00","01","02","03","04","05","06","07","08","09","10","11","12","13","15","16","17","18","19","20","21","22","23"};
    private String[] minutes = {"00","15","30","45"};

    //Observable lists that the items of the combo boxes
    private ObservableList<String> hoursItems = FXCollections.observableArrayList(hours);
    private ObservableList<String> minutesItems = FXCollections.observableArrayList(minutes);

    //Declares the java Timer
    private Timer timer;

    //intializes Boolean that will block timer request if one is already running
    private Boolean isRunning = false;

    // declares FXML components
    @FXML
    private TextArea instructions;

    @FXML
    private Label currentTriggerTimeText,statusText;

    @FXML
    private Button startButton;

    @FXML
    private ComboBox<String> hourBox,minuteBox;

    //method to open the lolesports webpage in default web browser
    // then cancels the timer killing the thread and setting running to false
    //tag is not needed I am just lazy
    @FXML
    protected void openPage() throws IOException, URISyntaxException {
        URI uri = new URI(twitch_uri);
        java.awt.Desktop.getDesktop().browse(URI.create(twitch_uri));
        statusText.setText("completed");
        timer.cancel();
        isRunning = false;
    }

    /*
    onclick function for the startButton
    checks local time and compares to trigger time which is defined by the comboboxes
    then defines the Task to be carried out by the timer and passes them in
     */

    @FXML
    protected void schedualEvent(){
        if(isRunning){

        }
        else{
            LocalDateTime runFinal;
            LocalTime curTime = LocalTime.now();
            String hour = hourBox.getValue();
            String minute = minuteBox.getValue();
            String passin = hour+":"+minute;
            LocalTime runTime = LocalTime.parse(passin);

            if(curTime.compareTo(runTime) > 0){
                runFinal = LocalDateTime.of(LocalDate.now().plusDays(1),runTime);
            }else{
                runFinal = LocalDateTime.of(LocalDate.now(),runTime);
            }
            Date runVar = Date.from(runFinal.atZone(ZoneId.systemDefault()).toInstant());
            //creates the task that will open the webpage
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        openPage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            };
            currentTriggerTimeText.setText(passin);
            statusText.setText("Running");
            timer = new Timer(true);
            timer.schedule(task,runVar);
            isRunning = true;
        }

    }

    //onclick for the stop button stops the current timer and sets isRunning to false
    @FXML
    protected void stopTimer(){
        statusText.setText("not Running");
        currentTriggerTimeText.setText("none");
        timer.cancel();
        isRunning = false;
    }

    //initialize method
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hourBox.setItems(hoursItems);
        minuteBox.setItems(minutesItems);
        hourBox.getSelectionModel().selectFirst();
        minuteBox.getSelectionModel().selectFirst();
    }
}