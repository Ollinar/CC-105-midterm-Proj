module org.saklam.pos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires derby;
    requires de.mkammerer.argon2;
    requires de.mkammerer.argon2.nolibs;
    

    opens fxml to javafx.fxml;
    exports org.saklam.pos;
}
