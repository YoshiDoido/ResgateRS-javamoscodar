import uol.compass.api.view.GeneralView;
import uol.compass.domain.model.Produto;
import uol.compass.infrastructure.connection.DatabaseConnection;
import uol.compass.infrastructure.exception.ConnectionException;

public class Main {

    public static void main(String[] args) {
        if (!verificarConnection()) return;
        var generalView = new GeneralView();

        generalView.start();
    }

    private static boolean verificarConnection() {
        try {
            DatabaseConnection.testConnection();
        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
