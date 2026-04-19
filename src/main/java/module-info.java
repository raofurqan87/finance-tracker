module com.furqan.financetracker {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.furqan.financetracker to javafx.fxml;
    exports com.furqan.financetracker;
}