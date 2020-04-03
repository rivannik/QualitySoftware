/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import static javafxapplication1.Launcher.*;
import static javafxapplication1.GUIImplementation.*;
import static javafxapplication1.ValidationLayer.*;

/**
 * The Graphical User Interface of the software. The GUI has a login panel, create an account panel,
 * displays different charts when the user has logged in, displays current time in London, displays
 * pollution percentages in London and calculates insurance score for all the different sites in the
 * database. Displays current weather data in London when the user logs in. If the user is a staff member
 * it gives an option to go to a separate email panel where the staff member can email customers
 * @author sttsenov
 */
public class GUIHandler extends Application {
    
    //pie chart object
    private PieChart chart;
    
    //bar chart group
    private Group barChartGroup;
    
    //line chart group
    private Group lineChartGroup;
    
    //starting date
    private LocalDate startDatePicked;
    
    //end date
    private LocalDate endDatePicked;
    
    //boolean to check if the end date has been changed
    private boolean checkEndDate;
    
    //integer to check to how many people the email is going to be sent
    private int countEmails = 0;
    
    /**
     * Paints the GUI and all the components on it. Makes sure all the buttons are working correctly and that the error messages
     * are displayed accordingly. Works with the GUIImplementation layer to create new accounts, to login, to email customers.
     * Works with the WeatherForecast to get the current weather data for London.
     * @param stage main stage that everything is painted on
     * @throws SQLException
     * @throws IOException
     * @throws javax.mail.MessagingException
     * @throws InsuranceException 
     */
    @Override
    public void start(Stage stage) throws SQLException, IOException, javax.mail.MessagingException, InsuranceException {
        //adds the title to the main stage
        stage.setTitle("Pollution Insurance");
        
        //makes the main stage maximized
        stage.setMaximized(true);

        //Create Account
        BorderPane newAcc = new BorderPane();
        GridPane accPane = new GridPane();

        //combobox to determine if the user is a staff member or a customer
        ObservableList<String> optionStaff = FXCollections.observableArrayList("Staff", "Customer");
        Text staffText = new Text("I am a:");
        ComboBox staff = new ComboBox<>(optionStaff);

        //username label and text field
        Text accUserName = new Text("Username:");
        TextField accUserNameText = new TextField();
        accUserNameText.setPrefColumnCount(10);

        //password label and text field
        Text accPass = new Text("Password:");
        PasswordField accPassText = new PasswordField();
        accPassText.setPrefColumnCount(10);

        //first name label and text field
        Text firstName = new Text("First name:");
        TextField firstNameText = new TextField();
        firstNameText.setPrefColumnCount(10);

        //last name label and text field
        Text lastName = new Text("Last name:");
        TextField lastNameText = new TextField();
        lastNameText.setPrefColumnCount(10);

        //age label and text field
        Text age = new Text("Age:");
        TextField ageText = new TextField();
        ageText.setPrefColumnCount(10);

        //options for the gender combobox
        ObservableList<String> optionGender = FXCollections.observableArrayList("Male", "Female");

        //gender combobox
        Text genderText = new Text("Gender:");
        ComboBox gender = new ComboBox<>(optionGender);

        //phone number label and text field
        Text phoneNum = new Text("Phone number:");
        TextField phoneText = new TextField();
        phoneText.setPrefColumnCount(10);
        
        //email label and text field
        Text email = new Text("Email:");
        TextField emailText = new TextField();
        emailText.setPrefColumnCount(10);

        //Combobox and label for smoking status
        Text smokingText = new Text("Are you a smoker:");
        ObservableList<String> optionSmoker = FXCollections.observableArrayList("Yes", "No, but I used to be", "No");
        ComboBox smokingStatus = new ComboBox<>(optionSmoker);

        //creates register and back button
        Button goBack = new Button("Go back");
        Button register = new Button("Register");

        //Error messages
        //weak username error message
        Text errUsername = new Text("Username should have 5-15 lowercase characters, digits or _-!");
        errUsername.setFill(Color.RED);

        //username taken error message
        Text errUsernameTaken = new Text("The username is taken!");
        errUsernameTaken.setFill(Color.RED);

        //password is too weak error message
        Text errPass = new Text("Password is too weak!");
        errPass.setFill(Color.RED);

        //not a valid first name error message
        Text errFirstName = new Text("Please input a correct first name!");
        errFirstName.setFill(Color.RED);

        //not a valid last name error message
        Text errLastName = new Text("Please input a correct last name!");
        errLastName.setFill(Color.RED);

        //not a valid age error message
        Text errAge = new Text("Please enter a valid age!");
        errAge.setFill(Color.RED);

        //not selected gender error message
        Text errGender = new Text("Please select your gender!");
        errGender.setFill(Color.RED);

        //not a valid phone number error message
        Text errPhone = new Text("Please enter a valid phone number!");
        errPhone.setFill(Color.RED);

        //not a valid email address error message
        Text errEmail = new Text("Please enter a valid email address!");
        errEmail.setFill(Color.RED);

        //not selected smoking stauts
        Text errSmoke = new Text("You haven't selected an option!");
        errSmoke.setFill(Color.RED);

        //not a valid staff number error message
        Text errStaff = new Text("Not a valid staff number!");
        errStaff.setFill(Color.RED);

        //not clarified if the user is a customer or a staff member error message
        Text errStaffChoose = new Text("Pick an option!");
        errStaffChoose.setFill(Color.RED);

        //all the error messages collected in an ArrayList object
        ArrayList<Text> errMessages = new ArrayList<>();

        //adding CSS to the "Go back" button
        goBack.setStyle("-fx-border-radius: 8px; -fx-background-color: #f44336; -fx-text-fill: white;");

        //chaging CSS when the mouse has entered the button
        goBack.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            goBack.setStyle("-fx-border-radius: 8px; -fx-background-color: #FF0000; -fx-text-fill: white;");
        });

        //restoring CSS when the mouse exites the button
        goBack.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            goBack.setStyle("-fx-border-radius: 8px; -fx-background-color: #f44336; -fx-text-fill: white;");
        });

        //Staff number
        Text staffNumber = new Text("Enter staff number:");
        TextField staffNumberText = new TextField();
        staffNumberText.setPrefColumnCount(10);

        //checks if the user is a staff member or a customer
        staff.setOnAction((event) -> {
            if (staff.getValue().equals("Staff")) {
                //if the user is a Staff Member then adds a text field for staff number
                if (!accPane.getChildren().contains(staffNumber) && !accPane.getChildren().contains(staffNumberText)) {
                    accPane.add(staffNumber, 10, 7);
                    accPane.add(staffNumberText, 11, 7);

                    accPane.getChildren().removeAll(errMessages);
                }
            } else {
                //if the user is a customer it checks if there is a staff number text field and removes it if there is
                if (accPane.getChildren().contains(staffNumber) && accPane.getChildren().contains(staffNumberText)) {
                    accPane.getChildren().remove(staffNumber);
                    accPane.getChildren().remove(staffNumberText);

                    accPane.getChildren().removeAll(errMessages);
                }
            }
        });

        //add staff or customer 
        accPane.add(staffText, 10, 6);
        accPane.add(staff, 11, 6);

        //add username
        accPane.add(accUserName, 10, 8);
        accPane.add(accUserNameText, 11, 8);

        //add password
        accPane.add(accPass, 10, 9);
        accPane.add(accPassText, 11, 9);

        //add first name
        accPane.add(firstName, 10, 10);
        accPane.add(firstNameText, 11, 10);

        //add last name
        accPane.add(lastName, 10, 11);
        accPane.add(lastNameText, 11, 11);

        //add age
        accPane.add(age, 10, 12);
        accPane.add(ageText, 11, 12);

        //add gender select
        accPane.add(genderText, 10, 13);
        accPane.add(gender, 11, 13);

        //add phone number
        accPane.add(phoneNum, 10, 14);
        accPane.add(phoneText, 11, 14);

        //add email
        accPane.add(email, 10, 15);
        accPane.add(emailText, 11, 15);

        //add smoking status
        accPane.add(smokingText, 10, 16);
        accPane.add(smokingStatus, 11, 16);

        //button pane
        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().addAll(goBack, register);
        flowPane.setHgap(5);
        flowPane.setMaxWidth(150);

        //add the button pane to the main pane
        accPane.add(flowPane, 11, 17);

        //positions all labels and text fields
        GridPane.setHalignment(staffText, HPos.RIGHT);
        GridPane.setHalignment(accUserName, HPos.RIGHT);
        GridPane.setHalignment(accPass, HPos.RIGHT);
        GridPane.setHalignment(firstName, HPos.RIGHT);
        GridPane.setHalignment(lastName, HPos.RIGHT);
        GridPane.setHalignment(age, HPos.RIGHT);
        GridPane.setHalignment(genderText, HPos.RIGHT);
        GridPane.setHalignment(phoneNum, HPos.RIGHT);
        GridPane.setHalignment(email, HPos.RIGHT);
        GridPane.setHalignment(smokingText, HPos.RIGHT);

        //adds vertical and horizontal gap
        accPane.setVgap(10);
        accPane.setHgap(10);

        //empty grid panes that are used to position the important panes
        GridPane emptyLeft = new GridPane();
        emptyLeft.setMinWidth(700);
        GridPane emptyTop = new GridPane();
        emptyTop.setMinHeight(200);

        //adds all the panes
        newAcc.setCenter(accPane);
        newAcc.setLeft(emptyLeft);
        newAcc.setTop(emptyTop);

        //create account scene
        Scene createAcc = new Scene(newAcc, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());

        //Log in systems
        BorderPane loginPanel = new BorderPane();

        //empty grids that are used to position the important panes
        GridPane emptyLeft1 = new GridPane();
        emptyLeft1.setMinWidth(700);
        GridPane emptyRight = new GridPane();
        emptyRight.setMinWidth(600);
        GridPane emptyTop1 = new GridPane();
        emptyTop1.setMinHeight(300);

        //login pane
        GridPane loginPane = new GridPane();
        loginPane.setPadding(new Insets(10, 10, 10, 10));
        
        //adds vertical and horizontal gap
        loginPane.setVgap(5);
        loginPane.setHgap(5);

        //username label
        Text username = new Text("Username:");
        loginPane.add(username, 10, 10);

        //username text field
        TextField text = new TextField();
        text.setPrefColumnCount(10);
        
        //username text field size
        text.setMinWidth(150);
        text.setMaxWidth(150);
        
        //adds the username text field to the login panel
        loginPane.add(text, 11, 10);

        //password label
        Text password = new Text("Password:");
        loginPane.add(password, 10, 11);

        //password text field
        PasswordField text2 = new PasswordField();
        text2.setPrefColumnCount(10);
        
        //password text field size
        text2.setMinWidth(150);
        text2.setMaxWidth(150);
        loginPane.add(text2, 11, 11);

        //login button
        Button loginBtn = new Button("Come on in");
        loginPane.add(loginBtn, 11, 12);

        //Create an account button
        Button createAccBtn = new Button("Create an account");
        loginPane.add(createAccBtn, 11, 13);

        //Error message for incorect login
        Text err = new Text("Incorrect username or password!");
        err.setFill(Color.RED);

        //adds all panels
        loginPanel.setCenter(loginPane);
        loginPanel.setLeft(emptyLeft1);
        loginPanel.setRight(emptyRight);
        loginPanel.setTop(emptyTop1);

        //login scene
        Scene logScene = new Scene(loginPanel, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());

        //----------------------------------------------------------------------
        //Pie Chart
        BorderPane borderPane = new BorderPane();
        try {
            //Pie Chart with Species and Values from the table
            Map<String, Integer> pieChartMap = getSpeciesValues();

            Set<Map.Entry< String, Integer>> st = pieChartMap.entrySet();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            
            //adds categories and their values to the chart
            st.forEach((me) -> {
                pieChartData.add(new PieChart.Data(me.getKey(), me.getValue()));
            });

            //creates the chart 
            chart = new PieChart(pieChartData);
        } catch (IOException e) {
            storeError(e.getMessage(), getCurrentTime());
        }

        //Bar Chart------------------------------------------------------------- 
        try {
            CategoryAxis xAxis = new CategoryAxis();
            Map<String, Integer> barChartMap = getSpeciesValues();
            Set<Map.Entry<String, Integer>> st1 = barChartMap.entrySet();
            ObservableList<String> xAxisOL = FXCollections.observableArrayList();
            
            //adds the categories and values for the chart
            st1.stream().map((me) -> {
                xAxisOL.add(me.getKey());
                return me;
            }).forEachOrdered((_item) -> {
                xAxis.setCategories(xAxisOL);
            });

            xAxis.setLabel("category");

            //Defining the y axis 
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("score");

            //Creating the Bar chart 
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Comparison between different species of pollution");

            //Prepare XYChart.Series objects by setting data     
            XYChart.Series<String, Number> series1 = new XYChart.Series<>();
            series1.setName("Overall value of species of pollution");
            st1.forEach((me) -> {
                series1.getData().add(new XYChart.Data<>(me.getKey(), me.getValue()));
            });

            //Setting the data to bar chart        
            barChart.getData().addAll(series1);
            barChart.setMinHeight(600);
            barChart.setMinWidth(800);

            barChartGroup = new Group(barChart);
        } catch (IOException e) {
            storeError(e.getMessage(), getCurrentTime());
        }
        //----------------------------------------------------------------------
        //Line Chart 
        try {
            CategoryAxis xAsixLine = new CategoryAxis();

            Map<String, Integer> barChartMap = getSpeciesValues();
            Set<Map.Entry<String, Integer>> st1 = barChartMap.entrySet();
            ObservableList<String> xAxisOL = FXCollections.observableArrayList();
            
            //adds the categories and values for the chart
            st1.stream().map((me) -> {
                xAxisOL.add(me.getKey());
                return me;
            }).forEachOrdered((_item) -> {
                xAsixLine.setCategories(xAxisOL);
            });

            NumberAxis yAxis = new NumberAxis();
            XYChart.Series series1 = new XYChart.Series();

            //adds the data to the line object
            st1.stream().map((me) -> {
                series1.getData().add(new XYChart.Data<>(me.getKey(), me.getValue()));
                return me;
            }).forEachOrdered((_item) -> {
                series1.setName("Overall value of species of pollution");
            });

            //Creates the actual chart and gives it a title
            StackedAreaChart<String, Number> areaChart = new StackedAreaChart(xAsixLine, yAxis);
            areaChart.setTitle("Comparison between different species of pollution");

            //adds the "Line" to the chart
            areaChart.getData().add(series1);
            
            //sets minimal size for the chart
            areaChart.setMinHeight(600);
            areaChart.setMinWidth(800);

            //adds the chart to a global Group variable
            lineChartGroup = new Group(areaChart);
        } catch (NumberFormatException e) {
            storeError(e.getMessage(), getCurrentTime());
        }

        //----------------------------------------------------------------------
        //side panel
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(70, 10, 10, 50)); //up, right, down, left
        gridPane.setMinWidth(500);
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        //Emailing customers
        GridPane gridMail = new GridPane();
        gridMail.setPadding(new Insets(10, 10, 10, 10));
        gridMail.setMinSize(300, 300);
        gridMail.setVgap(5);
        gridMail.setHgap(5);

        //user credentials table
        TableView tableView = new TableView();
        
        //First name column
        TableColumn<String, Customer> column1 = new TableColumn<>("First Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        //Last name column
        TableColumn<String, Customer> column2 = new TableColumn<>("Last Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        //Age column
        TableColumn<Integer, Customer> column3 = new TableColumn<>("Age");
        column3.setCellValueFactory(new PropertyValueFactory<>("age"));

        //Email column
        TableColumn<String, Customer> column4 = new TableColumn<>("Email");
        column4.setCellValueFactory(new PropertyValueFactory<>("email"));

        //Phone number column
        TableColumn<String, Customer> column5 = new TableColumn<>("Phone Number");
        column5.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));

        //Smoking status column
        TableColumn<String, Customer> column6 = new TableColumn<>("Smoking Status");
        column6.setCellValueFactory(new PropertyValueFactory<>("smokeStatus"));

        //adding the columns to the table
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);
        tableView.getColumns().add(column5);
        tableView.getColumns().add(column6);

        //inserting the credentials from the database into the table
        for (int i = 0; i < Credentials.getUserCredentials().size(); i++) {
            tableView.getItems().add(
                    new Customer(
                            Credentials.getUserCredentials().get(i).get(0), //first name
                            Credentials.getUserCredentials().get(i).get(1), //last name
                            Integer.parseInt(Credentials.getUserCredentials().get(i).get(2)), //age
                            Credentials.getUserCredentials().get(i).get(3), //phone number
                            Credentials.getUserCredentials().get(i).get(4), //email
                            Credentials.getUserCredentials().get(i).get(5)) //smoking status
            );
        }
        
        //Email stage
        
        //Labels and text fields 
        Text to = new Text("To:");
        gridMail.add(to, 0, 1);
        TextField toText = new TextField();
        toText.setPrefColumnCount(10);
        gridMail.add(toText, 1, 1);

        Text subject = new Text("Subject:");
        gridMail.add(subject, 0, 2);
        TextField subjectText = new TextField();
        subjectText.setPrefColumnCount(10);
        gridMail.add(subjectText, 1, 2);

        Text message = new Text("Message:");
        gridMail.add(message, 0, 3);

        //all the emails in the database
        ArrayList<ArrayList<String>> emails = Credentials.getUserEmails();

        //list view that will store emails of customers
        ListView<String> list = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        list.setItems(items);
        list.setPrefWidth(200);
        list.setPrefHeight(250);

        //Inserting the emails into the list view
        for (int i = 0; i < emails.size(); i++) {
            for (int j = 0; j < emails.get(i).size(); j++) {
                if (!emails.get(i).get(j).equals("") || !emails.get(i).get(j).isEmpty()) {
                    items.add(emails.get(i).get(j));
                } else {
                    items.add("Missing Records");
                }
            }
        }

        //when an email is clicked it adds it to the "To:" text field
        //it keeps count of how many emails were added and separates them with comma
        list.setOnMouseClicked((MouseEvent event) -> {
            if (countEmails == 0 || toText.getText().equals("") || toText.getText().equals(" ")) {
                toText.appendText(list.getSelectionModel().getSelectedItem());
                countEmails++;
            } else {
                toText.appendText(" , " + list.getSelectionModel().getSelectedItem());
            }
        });

        //adds the list view
        gridMail.add(list, 2, 4);

        //html editor for the actual message
        HTMLEditor htmlEditor = new HTMLEditor();
        VBox vBox = new VBox(htmlEditor);
        gridMail.add(vBox, 1, 4);

        //Back button
        Button goToProfile = new Button("Back");
        gridMail.add(goToProfile, 0, 5);

        //Send button
        Button sendBtn = new Button("Send");

        //Sending the email when the "Send" button is pressed and clearing the text fields
        sendBtn.setOnAction((ActionEvent ae) -> {
            try {
                Email.sendGmail(toText.getText(), subjectText.getText(), getNormalText(htmlEditor.getHtmlText()));
            } catch (IOException ex) {
                Logger.getLogger(GUIHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            subjectText.clear();
            toText.clear();
            htmlEditor.setHtmlText("");
        });

        //adding the send button and adding a background color
        gridMail.add(sendBtn, 2, 5);
        gridMail.setStyle("-fx-background-color: #D8BFD8;");

        //Email button
        Button emailBtn = new Button("Email");
        Scene emailScene = new Scene(gridMail, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());

        emailBtn.setOnAction((ActionEvent ae) -> {
            stage.setScene(emailScene);
        });

        //Labels
        Text pickSiteLabel = new Text("Choose a site:");
        Text startDateLabel = new Text("Pick a starting date:");
        Text endDateLabel = new Text("Pick an eding date:");

        //Drop down menu
        ObservableList<String> sites = FXCollections.observableArrayList("Overall"); 
        for (int i = 0; i < GUIImplementation.getSites().size(); i++) {
            sites.add(GUIImplementation.getSites().get(i));
        }
        
        ObservableList<String> siteOptions = sites;
        ComboBox siteSelect = new ComboBox(siteOptions);
        siteSelect.getSelectionModel().selectFirst();

        //Date Pickers
        DatePicker startDate = new DatePicker();
        startDate.setValue(LOCAL_DATE("01-06-2019"));

        DatePicker endDate = new DatePicker();
        endDate.setValue(LOCAL_DATE("31-12-2019"));
        
        //chooses the range of available dates in the date picker
        restrictDatePicker(startDate, LocalDate.of(2019, Month.JUNE, 1), LocalDate.of(2019, Month.DECEMBER, 31));
        restrictDatePicker(endDate, LocalDate.of(2019, Month.JUNE, 1), LocalDate.of(2019, Month.DECEMBER, 31));

        //restricts the second date picker based on the first date picker
        startDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                startDatePicked = startDate.getValue();
                //checks if the first date picker is picked
                if (startDatePicked != null) {
                    restrictDatePicker(endDate, startDatePicked, LocalDate.of(2019, Month.DECEMBER, 31));
                    checkEndDate = true;
                } else {
                    restrictDatePicker(endDate, LocalDate.of(2019, Month.JUNE, 1), LocalDate.of(2019, Month.DECEMBER, 31));
                    checkEndDate = true;
                }
            }
        });
        
        //gets the value from the end date if another value is picked 
        endDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                endDatePicked = endDate.getValue();
            }
        });

        //bottom panel
        BorderPane bottomPanel = new BorderPane();
        bottomPanel.setMinHeight(500);
        
        GridPane speciesPercent = new GridPane();
        speciesPercent.setMinHeight(500);
        speciesPercent.setStyle("-fx-background-color: DAE6F3;");
        speciesPercent.setVgap(5);
        
        //creates a map object for the names of the species and their percentage value
        Map<String, Double> percentages = CalculateInsurance.calPercentages();
        Iterator it = percentages.entrySet().iterator();

        ArrayList<Label> pairs = new ArrayList<>();
        
        //iterates through the percentages and turns the name and the value pairs
        //of the pollution percentages to label objects
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            pairs.add(new Label(pair.getKey() + " = " + pair.getValue() + "%"));
            it.remove(); // avoids a ConcurrentModificationException
        }
        
        //adds a label for overall pollution percentages
        speciesPercent.add(new Label("Overall pollution percentages: "), 0, 0);

        //prints the name of the precentage of every species of pollution
        for (int i = 0; i < pairs.size(); i++) {
            speciesPercent.add(pairs.get(i), 0, i + 1);
        }

        // Create the WebView
        WebView webView = new WebView();
         
        // Create the WebEngine
        final WebEngine webEngine = webView.getEngine();
        
        //loads the LondAirQuaility main page
        webEngine.load("http://londonair.org.uk/LondonAir/Default.aspx");
        // Create the VBox
        VBox root = new VBox();
        // Add the WebView to the VBox
        root.getChildren().add(webView);
        
        //add the label for the overall insrance score
        speciesPercent.add(new Label("Overall insurance bonus: " + CalculateInsurance.calInsurance()), 1, 0);
        
        //adds the calculated insurance bonus for all different sites to the bottom panel
        for (int i = 0; i < siteOptions.size(); i++) {
            if(!siteOptions.get(i).equals("Overall")){
                speciesPercent.add(new Label("Insurance bonus for " + siteOptions.get(i) + "equals: " + CalculateInsurance.calInsuranceSite(siteOptions.get(i))), 1, i + 1);
            }    
        }
        
        //adds data from the OpenWeatherAPI to the bottom panel
        speciesPercent.add(WeatherForecast.getTemperature(), 2, 0);
        speciesPercent.add(WeatherForecast.getHumidity(), 2, 1);
        speciesPercent.add(WeatherForecast.getWindSpeed(), 2, 2);
        speciesPercent.add(WeatherForecast.getWindAngle(), 2, 3);
        
        //adds the percentages of the species, the current insurance score,
        //the different characteristics for the weather in London
        //and the webpage viewer object to the bottom panel
        bottomPanel.setLeft(speciesPercent);
        bottomPanel.setCenter(root);
        
        //Creates a middle panel
        BorderPane center = new BorderPane();
        center.setCenter(chart);
        center.setBottom(bottomPanel);

        //A button to turn the main chart to a bar chart
        Button chartToBar = new Button("Bar Chart");
        chartToBar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //checks if the user has selected a different end date
                if (checkEndDate) {
                    try {
                        CategoryAxis xAxis = new CategoryAxis();
                        Map<String, Integer> barChartMap;
                        Set<Map.Entry<String, Integer>> st1;
                        Set<Map.Entry<String, Integer>> st2;
                        ObservableList<String> xAxisOL;
                        
                        //gets the starting date from the Date Picker object and turns it into a proper format
                        //so that it can be used in the GUIImplementation class
                        String fixedStartDate = Integer.toString(startDatePicked.getYear()) + "-"
                                + Integer.toString(startDatePicked.getMonthValue()) + "-"
                                + Integer.toString(startDatePicked.getDayOfMonth());
                        
                        //gets the end date from the Date Picker object and turns it into a proper format
                        //so that it can be used in the GUIImplementation class
                        String fixedEndDate = Integer.toString(endDatePicked.getYear()) + "-"
                                + Integer.toString(endDatePicked.getMonthValue()) + "-"
                                + Integer.toString(endDatePicked.getDayOfMonth());
                        
                        //checks which site is selected
                        if (siteSelect.getValue().equals("Overall")) {
                            barChartMap = getSpeciesDate(fixedStartDate);
                            st1 = barChartMap.entrySet();
                            xAxisOL = FXCollections.observableArrayList();

                            Map<String, Integer> barChartMap1 = getSpeciesDate(fixedEndDate);
                            st2 = barChartMap1.entrySet();
                        } else {
                            barChartMap = getSpeciesSite(fixedStartDate, siteSelect.getValue().toString());
                            st1 = barChartMap.entrySet();
                            xAxisOL = FXCollections.observableArrayList();

                            Map<String, Integer> barChartMap1 = getSpeciesSite(fixedEndDate, siteSelect.getValue().toString());
                            st2 = barChartMap1.entrySet();
                        }
                        
                        //adds all the values to the bar chart
                        for (Map.Entry< String, Integer> me : st1) {
                            xAxisOL.add(me.getKey());
                            xAxis.setCategories(xAxisOL);
                        }
                        //sets a label to the xAxis of the chart
                        xAxis.setLabel("category");
                        
                        //creates and sets a label to the yAxis of the chart
                        NumberAxis yAxis = new NumberAxis();
                        yAxis.setLabel("score");

                        //Creating the Bar chart 
                        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
                        barChart.setTitle("Comparison between different species of pollution");

                        //Prepare XYChart.Series objects by setting data     
                        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
                        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
                        
                        //adds value to the first bar of the bar chart
                        //the first bar represents the first selected date or if the user has not selected different dates
                        //it represents the overall score of the different species of pollution.
                        for (Map.Entry< String, Integer> me : st1) {
                            series1.getData().add(new XYChart.Data<>(me.getKey(), me.getValue()));
                            series1.setName("First date values");
                        }

                        //adds value to the second bar of the bar chart
                        //the second bar represents the date selected from the second datepicker object and will be empty
                        //if the user has not selected any different dates
                        for (Map.Entry< String, Integer> me : st2) {
                            series2.getData().add(new XYChart.Data<>(me.getKey(), me.getValue()));
                            series2.setName("Second date values");
                        }

                        //Setting the data to bar chart        
                        barChart.getData().addAll(series1, series2);
                        
                        //setting a minimum size for the bar chart
                        barChart.setMinHeight(600);
                        barChart.setMinWidth(800);
                        
                        //adding the bar chart to a global Group object
                        barChartGroup = new Group(barChart);
                    } catch (IOException bar) {
                        System.out.println(bar.getMessage());
                    }
                }
                //adds the bar chart to a boorder pane
                center.setCenter(barChartGroup);
            }
        });

        //a button object that turns the main chart to a pie chart
        Button chartToPie = new Button("Pie Chart");
        chartToPie.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //checks if the user has selected an end date
                if (checkEndDate) {
                    try {
                        Map<String, Integer> test;
                        
                        //gets the starting date from the Date Picker object and turns it into a proper format
                        //so that it can be used in the GUIImplementation class
                        String fixedStartDate = Integer.toString(startDatePicked.getYear()) + "-"
                                + Integer.toString(startDatePicked.getMonthValue()) + "-"
                                + Integer.toString(startDatePicked.getDayOfMonth());
                        
                        //gets the end date from the Date Picker object and turns it into a proper format
                        //so that it can be used in the GUIImplementation class
                        String fixedEndDate = Integer.toString(endDatePicked.getYear()) + "-"
                                + Integer.toString(endDatePicked.getMonthValue()) + "-"
                                + Integer.toString(endDatePicked.getDayOfMonth());
                        
                        //checks which site is selected
                        if (siteSelect.getValue().equals("Overall")) {
                            test = getBetweenDates(fixedStartDate, fixedEndDate);
                        } else {
                            test = getSiteBetweenDates(fixedStartDate, fixedEndDate, siteSelect.getValue().toString());
                        }
                        
                        Set<Map.Entry< String, Integer>> st = test.entrySet();
                        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
                        
                        //adds all the values of the pollution species into the pie chart
                        for (Map.Entry< String, Integer> me : st) {
                            pieChartData.add(new PieChart.Data(me.getKey(), me.getValue()));
                        }
                        
                        //creates the pie chart
                        chart = new PieChart(pieChartData);
                    } catch (IOException pie) {
                        System.out.println(pie.getMessage());
                    }

                }
                //adds the pie chart to a border pane
                center.setCenter(chart);
            }
        });
        
        //Creates a button object that will display a Line Chart in the main stage
        Button chartToLine = new Button("Line Chart");
        chartToLine.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //checks if user has selected a different end date from the date pickers
                if (checkEndDate) {
                    try {
                        Map<String, Integer> barChartMap;
                        Map<String, Integer> barChartMap1;
                        
                        //gets the starting date from the Date Picker object and turns it into a proper format
                        //so that it can be used in the GUIImplementation class
                        String fixedStartDate = Integer.toString(startDatePicked.getYear()) + "-"
                                + Integer.toString(startDatePicked.getMonthValue()) + "-"
                                + Integer.toString(startDatePicked.getDayOfMonth());

                        
                        //gets the end date from the Date Picker object and turns it into a proper format
                        //so that it can be used in the GUIImplementation class
                        String fixedEndDate = Integer.toString(endDatePicked.getYear()) + "-"
                                + Integer.toString(endDatePicked.getMonthValue()) + "-"
                                + Integer.toString(endDatePicked.getDayOfMonth());

                        CategoryAxis xAsixLine = new CategoryAxis();
                        
                        //checks which site is selected
                        if (siteSelect.getValue().equals("Overall")) {
                            barChartMap = getSpeciesDate(fixedStartDate);
                            barChartMap1 = getSpeciesDate(fixedEndDate);
                        } else {
                            barChartMap = getSpeciesSite(fixedStartDate, siteSelect.getValue().toString());
                            barChartMap1 = getSpeciesSite(fixedStartDate, siteSelect.getValue().toString());
                        }
                        
                        //Creates proper Axis objects for the lines in the Line Chart
                        Set<Map.Entry<String, Integer>> st1 = barChartMap.entrySet();
                        ObservableList<String> xAxisOL = FXCollections.observableArrayList();

                        Set<Map.Entry<String, Integer>> st2 = barChartMap1.entrySet();

                        NumberAxis yAxis = new NumberAxis();
                        XYChart.Series series1 = new XYChart.Series();
                        XYChart.Series series2 = new XYChart.Series();
                        
                        //inserts all the different types of pollution species and adds them to the chart
                        st1.stream().map((me) -> {
                            xAxisOL.add(me.getKey());
                            return me;
                        }).forEachOrdered((_item) -> {
                            xAsixLine.setCategories(xAxisOL);
                        });
                        
                        //inserts the values of the species of pollution for the starting date and adds them to
                        //the chart
                        st1.stream().map((me) -> {
                            series1.getData().add(new XYChart.Data<>(me.getKey(), me.getValue()));
                            return me;
                        }).forEachOrdered((_item) -> {
                            series1.setName("First date values");
                        });
                                                
                        //inserts the values of the species of pollution for the end date and adds them to
                        //the chart
                        st2.stream().map((me) -> {
                            series2.getData().add(new XYChart.Data<>(me.getKey(), me.getValue()));
                            return me;
                        }).forEachOrdered((_item) -> {
                            series2.setName("Second date values");
                        });
                        
                        //creates the chart and gives it a title
                        StackedAreaChart<String, Number> areaChart = new StackedAreaChart<>(xAsixLine, yAxis);
                        areaChart.setTitle("Comparison of the values of different species of pollution");
                        
                        //adds the lines with their values in the chart
                        areaChart.getData().addAll(series1, series2);
                        
                        //sets minimum size for the chart
                        areaChart.setMinHeight(600);
                        areaChart.setMinWidth(800);
                        
                        //adds the chart to a global Group object
                        lineChartGroup = new Group(areaChart);
                    } catch (IOException line) {
                        System.out.println(line.getMessage());
                    }
                }
                //adds the lineChartGroup object to a border pane
                center.setCenter(lineChartGroup);
            }
        });
        
        //creates a log out button object that clears the username and password field when the user logs in,
        //removes all the error messages on the login stage and removes the extra features for staff members when
        //they log in
        Button logOutBtn = new Button("Log out");
        logOutBtn.setOnAction((ActionEvent e) -> {
            text.clear();
            text2.clear();
            loginPane.getChildren().remove(err);
            gridPane.getChildren().removeAll(emailBtn, tableView);
            stage.setScene(logScene);
        });

        //adding all the comboboxes, datepickers and buttons to the gridPane -----------------------------------------------
        
        //Creates a label object that will update every second and change to display the current time
        Label timeLabel = new Label();
        Label clock = new Label("Current Time:");
        final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500),
                event -> {
                    timeLabel.setText(new GregorianCalendar().getTime().toString());
                }
        )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        
        //adds a functional clock and a label next to it
        gridPane.add(clock, 0, 0);
        gridPane.add(timeLabel, 1, 0);
        
        //adds a combobox for Site Selection and a label next to it
        gridPane.add(pickSiteLabel, 0, 3);
        gridPane.add(siteSelect, 1, 3);
        
        //adds a DatePicker object for a starting date and a label next to it
        gridPane.add(startDateLabel, 0, 5);
        gridPane.add(startDate, 1, 5);
        
        //adds a DatePicker object for an ending date and a label next to it
        gridPane.add(endDateLabel, 0, 6);
        gridPane.add(endDate, 1, 6);

        //adds buttons that change the type of the displayed chart.
        gridPane.add(chartToBar, 1, 7);
        gridPane.add(chartToPie, 1, 8);
        gridPane.add(chartToLine, 1, 9);
        
        //adds a logout button
        gridPane.add(logOutBtn, 0, 21);

        //adds all the different panes into a bigger borderPane object that will be added to the main scene
        borderPane.setCenter(center);
        borderPane.setLeft(gridPane);

        //the main scene that is as big as the screen
        Scene scene = new Scene(borderPane, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());

        //changes the main stage from the email stage back to the main stage
        goToProfile.setOnAction((ActionEvent ae) -> {
            countEmails = 0;
            toText.clear();
            stage.setScene(scene);
            stage.setMaximized(true);
        });
        
        //sets the default stage so that it asks the user to login whenever the program starts
        stage.setScene(logScene);

        //checks if the user has inserted the correct login information and displays the correct error messages if
        //the credentials are wrong. The whole check is made when the Enter key is pressed by the user.
        logScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                try {
                    if (Credentials.checkLogin(text.getText(), text2.getText()) || Credentials.checkStaffLogin(text.getText(), text2.getText())) {
                        if (Credentials.checkStaffLogin(text.getText(), text2.getText())) {
                            gridPane.add(emailBtn, 1, 20);
                            gridPane.add(tableView, 1, 10);
                            stage.setScene(scene);
                        } else {
                            stage.setScene(scene);
                        }
                    } else {
                        if (!loginPane.getChildren().contains(err)) {
                            loginPane.add(err, 11, 14);
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUIHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        //checks if the user has inserted the correct login information and displays the correct error messages if
        //the credentials are wrong. The whole check is made when the Login button is pressed
        loginBtn.setOnAction((ActionEvent e) -> {
            try {
                if (Credentials.checkLogin(text.getText(), text2.getText()) || Credentials.checkStaffLogin(text.getText(), text2.getText())) {
                    if (Credentials.checkStaffLogin(text.getText(), text2.getText())) {
                        gridPane.add(emailBtn, 1, 20);
                        gridPane.add(tableView, 1, 10);
                        stage.setScene(scene);
                    } else {
                        stage.setScene(scene);
                    }
                } else {
                    if (!loginPane.getChildren().contains(err)) {
                        loginPane.add(err, 11, 14);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(GUIHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //go to the scene that is used to create an account
        createAccBtn.setOnAction((ActionEvent e) -> {
            loginPane.getChildren().remove(err);
            text.clear();
            text2.clear();
            stage.setScene(createAcc);
        });

        //go back to the main scene
        goBack.setOnAction((ActionEvent e) -> {
            accPane.getChildren().removeAll(errMessages);
            stage.setScene(logScene);
        });

        //goes back to the main scene if the user has imported his credentials
        register.setOnAction((ActionEvent e) -> {
            int correct = 0;
            accPane.getChildren().removeAll(errMessages);

            //checks if the first name is correct
            if (firstNameValidation(firstNameText.getText())) {
                if (accPane.getChildren().contains(errFirstName)) {
                    accPane.getChildren().remove(errFirstName);
                }
                correct++;
            } else {
                if (!accPane.getChildren().contains(errFirstName)) {
                    accPane.add(errFirstName, 12, 10);
                    errMessages.add(errFirstName);
                }
            }

            //checks if the last name is correct
            if (lastNameValidation(lastNameText.getText())) {
                if (accPane.getChildren().contains(errLastName)) {
                    accPane.getChildren().remove(errLastName);
                }
                correct++;
            } else {
                if (!accPane.getChildren().contains(errLastName)) {
                    accPane.add(errLastName, 12, 11);
                    errMessages.add(errLastName);
                }
            }

            try {
                //checks if the the age is valid
                if (validateAge(Integer.parseInt(ageText.getText()))) {
                    if (accPane.getChildren().contains(errAge)) {
                        accPane.getChildren().remove(errAge);
                    }
                    correct++;
                } else {
                    if (!accPane.getChildren().contains(errAge)) {
                        accPane.add(errAge, 12, 12);
                        errMessages.add(errAge);
                    }
                }
            } catch (NumberFormatException nfe) {
                if (!accPane.getChildren().contains(errAge)) {
                    accPane.add(errAge, 12, 12);
                    errMessages.add(errAge);
                }
            }

            //checks if the user has selected gender
            if (gender.getValue() != null) {
                if (accPane.getChildren().contains(errGender)) {
                    accPane.getChildren().remove(errGender);
                }
                correct++;
            } else {
                if (!accPane.getChildren().contains(errGender)) {
                    accPane.add(errGender, 12, 13);
                    errMessages.add(errGender);
                }
            }

            //checks if the phone number is valid
            if (validatePhone(phoneText.getText())) {
                if (accPane.getChildren().contains(errPhone)) {
                    accPane.getChildren().remove(errPhone);
                }
                correct++;
            } else {
                if (!accPane.getChildren().contains(errPhone)) {
                    accPane.add(errPhone, 12, 14);
                    errMessages.add(errPhone);
                }
            }

            //checks if the email is valid
            if (validateEmail(emailText.getText())) {
                if (accPane.getChildren().contains(errEmail)) {
                    accPane.getChildren().remove(errEmail);
                }
                correct++;
            } else {
                if (!accPane.getChildren().contains(errEmail)) {
                    accPane.add(errEmail, 12, 15);
                    errMessages.add(errEmail);
                }
            }

            //checks if the user is a smoker
            if (smokingStatus.getValue() != null) {
                if (accPane.getChildren().contains(errSmoke)) {
                    accPane.getChildren().remove(errSmoke);
                }
                correct++;
            } else {
                if (!accPane.getChildren().contains(errSmoke)) {
                    accPane.add(errSmoke, 12, 16);
                    errMessages.add(errSmoke);
                }
            }

            //checks if the user has selected if he is a customer or a staff member
            if (staff.getValue() == null) {
                accPane.add(errStaffChoose, 12, 6);
                errMessages.add(errStaffChoose);
            }

            try {
                //checks if the username is not taken and if it the prefered symbols
                if (validateUsername(accUserNameText.getText()) && Credentials.checkUsernameFree(accUserNameText.getText())) {
                    if (accPane.getChildren().contains(errUsername)) {
                        accPane.getChildren().remove(errUsername);
                    } else if (accPane.getChildren().contains(errUsernameTaken)) {
                        accPane.getChildren().remove(errUsernameTaken);
                    }
                    correct++;

                } else if (!Credentials.checkUsernameFree(accUserNameText.getText()) && validateUsername(accUserNameText.getText())) {
                    if (!accPane.getChildren().contains(errUsernameTaken)) {
                        accPane.add(errUsernameTaken, 12, 8);
                        errMessages.add(errUsernameTaken);
                    }

                    if (accPane.getChildren().contains(errUsername)) {
                        accPane.getChildren().remove(errUsername);
                    }

                } else if (!validateUsername(accUserNameText.getText()) && Credentials.checkUsernameFree(accUserNameText.getText())) {
                    if (!accPane.getChildren().contains(errUsername)) {
                        accPane.add(errUsername, 12, 8);
                        errMessages.add(errUsername);
                    }
                    if (accPane.getChildren().contains(errUsernameTaken)) {
                        accPane.getChildren().remove(errUsernameTaken);
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(GUIHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

            //checks if the password is strong
            if (validatePassword(accPassText.getText())) {
                if (accPane.getChildren().contains(errPass)) {
                    accPane.getChildren().remove(errPass);
                }
                correct++;
            } else {
                if (!accPane.getChildren().contains(errPass)) {
                    accPane.add(errPass, 12, 9);
                    errMessages.add(errPass);
                }
            }

            //checks if the member has to input a staff number and if that number is correct
            if (accPane.getChildren().contains(staffNumberText) && accPane.getChildren().contains(staffNumber)) {
                try {
                    if (validateStaff(staffNumberText.getText()) && Credentials.checkStaffNumber(staffNumberText.getText())) {
                        if (accPane.getChildren().contains(errStaff)) {
                            accPane.getChildren().remove(errStaff);
                        }
                        correct++;
                    } else {
                        if (!accPane.getChildren().contains(errStaff)) {
                            accPane.add(errStaff, 12, 7);
                            errMessages.add(errStaff);
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUIHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //checks if the user has filled everything and stores their credentials into the system
            if (accPane.getChildren().contains(staffNumberText) && accPane.getChildren().contains(staffNumber)) {
                //the user is a staff member
                if (correct == 10) {
                    try {
                        //inserts users credentials into the Staff Member table in the database
                        Credentials.insertStaffCredentials(accUserNameText.getText(), accPassText.getText(), staffNumberText.getText(), firstNameText.getText(), lastNameText.getText(), Integer.parseInt(ageText.getText()),
                                gender.getValue().toString(), phoneText.getText(), emailText.getText(), smokingStatus.getValue().toString());
                    } catch (IOException ex) {
                        Logger.getLogger(GUIHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    //clears all the text fields and comboboxes
                    staffNumberText.clear();
                    accUserNameText.clear();
                    accPassText.clear();
                    firstNameText.clear();
                    lastNameText.clear();
                    ageText.clear();
                    gender.getSelectionModel().clearSelection();
                    phoneText.clear();
                    emailText.clear();
                    smokingStatus.getSelectionModel().clearSelection();

                    stage.setScene(logScene);
                }
            } else {
                //the user is a customer
                if (correct == 9) {
                    try {
                        //inserts users credentials into the Customers table in the database
                        Credentials.insertUserCredentials(accUserNameText.getText(), accPassText.getText(), firstNameText.getText(), lastNameText.getText(), Integer.parseInt(ageText.getText()),
                                gender.getValue().toString(), phoneText.getText(), emailText.getText(), smokingStatus.getValue().toString());
                    } catch (IOException ex) {
                        Logger.getLogger(GUIHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    //clears all the text fields and comboboxes
                    accUserNameText.clear();
                    accPassText.clear();
                    firstNameText.clear();
                    lastNameText.clear();
                    ageText.clear();
                    gender.getSelectionModel().clearSelection();
                    phoneText.clear();
                    emailText.clear();
                    smokingStatus.getSelectionModel().clearSelection();

                    stage.setScene(logScene);
                }
            }
        });
        stage.show();
    }
    
    /**
     * A method that restricts a DatePicker object in a specified time period
     * @param datePicker
     * @param minDate the starting date of the datePicker
     * @param maxDate the ending date of the datePicker
     */
    public void restrictDatePicker(DatePicker datePicker, LocalDate minDate, LocalDate maxDate) {
        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker1) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(minDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                } else if (item.isAfter(maxDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
    }

    /**
     * Used to convert a String object to a LocalDate object so that it can be used in a
     * Date Picker object.
     * @param dateString string containing the date
     * @return LocalDate object made from the dateString string
     */
    public static final LocalDate LOCAL_DATE(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }

    /**
     * Gets the text from the HTMLText field and removes the HTML tags in the message.
     * For that purpose it uses Regex and replaces the opening and closing tags with empty
     * characters
     * @param htmlText the text with the HTML tags in it
     * @return the text without the HTML tags in it
     */
    public static String getNormalText(String htmlText) {
        String result = "";

        Pattern pattern = Pattern.compile("<[^>]*>");
        Matcher matcher = pattern.matcher(htmlText);
        final StringBuffer text = new StringBuffer(htmlText.length());

        while (matcher.find()) {
            matcher.appendReplacement(
                    text,
                    " ");
        }

        matcher.appendTail(text);
        result = text.toString().trim();
        return result;
    }
}
