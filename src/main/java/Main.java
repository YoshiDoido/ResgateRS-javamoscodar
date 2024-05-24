import uol.compass.api.view.GeneralView;
import uol.compass.infrastructure.connection.DatabaseConnection;
import uol.compass.infrastructure.exception.ConnectionException;
import uol.compass.infrastructure.exception.RepositoryException;

public class Main {

    public static void main(String[] args) {
       verificarConnection();
        var generalView = new GeneralView();

        try {
            generalView.start();
        } catch (RepositoryException e) {
            e.printStackTrace();
            System.out.println("\n"+e.getMessage() + ". Entre em contato com o administrador caso o erro persista.");
        }

    }

    private static void verificarConnection() {
        try {
            DatabaseConnection.testConnection();
        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
