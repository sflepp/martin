package ch.zhaw.psit4.martin.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;

import org.springframework.context.ApplicationContext;

/**
 * Entry point for the application to bootstrap jpf, SPRING
 *
 * @version 0.0.1-SNAPSHOT
 */

@SpringBootApplication
@ImportResource("classpath:Beans.xml")
public class MartinBoot {
    /**
     * The application context
     */
    private static ApplicationContext context;

    /**
     * Main application entry point launches MArtIn and used components.
     * 
     * @param args
     *            Command line arguments (unused)
     */
    public static void main(String[] args) {
        MartinBoot.preBoot(args);
        MartinBoot.boot(args);
        MartinBoot.postBoot(args);
    }

    public static void preBoot(String[] args) {
        // Nothing to do
    }

    public static void boot(String[] args) {
        // Start Spring Application
        MartinBoot.context = SpringApplication.run(MartinBoot.class, args);
    }

    public static void postBoot(String[] args) {
        // Start Plugin-Library
        IPluginLibrary library = (IPluginLibrary) MartinBoot.context
                .getBean("IPluginLibrary");
        library.startLibrary();
        
    }
}
