package it.unibo.ingsoft.fortunagest.auth;

import java.io.IOException;

import it.unibo.ingsoft.fortunagest.StageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends StageController {
    @FXML
    TextField username;
    @FXML
    PasswordField password;

    public void login(ActionEvent event) throws IOException {
        if (!username.getText().isBlank()) {
            boolean success = AuthSingleton.getInstance().login(username.getText().strip(), password.getText().strip());
            if (success) {
                switchToHomeTitolare(event);
            }
        }
    }
}
