module com.example.socialnetworkfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.socialnetworkfx to javafx.fxml;
    exports com.example.socialnetworkfx;
    opens com.example.socialnetworkfx.guiAdmin to javafx.fxml;
    exports com.example.socialnetworkfx.guiAdmin;
    opens com.example.socialnetworkfx.domain to javafx.fxml;
    exports com.example.socialnetworkfx.domain;
    exports com.example.socialnetworkfx.domain.abstractDomain;
    opens com.example.socialnetworkfx.domain.abstractDomain to javafx.fxml;
    exports com.example.socialnetworkfx.domain.dto;
    exports com.example.socialnetworkfx.guiUser;

}