package Client.Controller;

import javafx.fxml.FXML;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

/**
 * Created by Michele Iessi on 20/05/2017.
 */
public class MainChatViewController {

    @FXML private Text actionTarget;
    @FXML private TextField userNameTextField;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        String name = userNameTextField.getText();
        if(!name.equals("")) { // Controllare se c'è già un utente loggato con quello stesso nome
            actionTarget.setText("Benvenuto " + name);

        }
        else {
            actionTarget.setText("Inserire un username valido");
        }
    }
}
