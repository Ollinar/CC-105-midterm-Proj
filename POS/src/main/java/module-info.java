module org.saklam.pos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires derby;
    requires java.base;

    opens org.saklam.pos to javafx.fxml;
    exports org.saklam.pos;
}
