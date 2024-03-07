module com.example.pslab1fx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pslab1fx to javafx.fxml;
    exports com.example.pslab1fx;
}