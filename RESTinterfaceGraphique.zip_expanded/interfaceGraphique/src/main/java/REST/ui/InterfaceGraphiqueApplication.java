// IMPORTANT : code généré à 90% par chat gpt
package REST.ui;

import javax.swing.SwingUtilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import manager.WebServiceManager;
import ui.login.LoginFrame;

@SpringBootApplication(scanBasePackages = {"manager"})
public class InterfaceGraphiqueApplication {

    public static void main(String[] args) {
        // Désactive le serveur web
        System.setProperty("server.port", "0");

        // Lancer Spring pour obtenir les beans nécessaires
        SpringApplication app = new SpringApplication(InterfaceGraphiqueApplication.class);
        app.setHeadless(false); // <--- désactive le headless

        ConfigurableApplicationContext context = app.run(args);

        // Récupérer les beans dont on a besoin pour l'UI
        WebServiceManager manager = context.getBean(WebServiceManager.class);

        // Lancer Swing sur l'EDT
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(manager);
            loginFrame.setVisible(true);
        });
    }
}
