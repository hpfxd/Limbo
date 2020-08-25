package nl.hpfxd.limbo;

import lombok.Getter;
import lombok.extern.java.Log;
import nl.hpfxd.limbo.logging.LimboLogFormatter;
import nl.hpfxd.limbo.network.NetworkManager;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Log
public class Limbo {
    @Getter private static Limbo instance;
    @Getter private boolean running;

    @Getter private final Properties config = new Properties();
    @Getter private NetworkManager networkManager;

    @Getter private JSONObject motd;
    @Getter private int maxPlayers;
    @Getter private int port = -1;

    public void start(String[] args) {
        this.configureLogger();
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }
        instance = this;

        log.info("Starting Limbo.");

        try {
            this.loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        this.networkManager = new NetworkManager();
        this.networkManager.start();
    }

    public void shutdown() {
        if (!this.running) return;
        this.running = false;
        log.info("Shutting down.");
        this.networkManager.shutdown();
        instance = null;
    }

    private void loadConfig() throws IOException {
        File file = new File("server.properties");

        if (!file.exists()) {
            try (InputStream in = Limbo.class.getResourceAsStream("/server.properties")) {
                Files.copy(in, file.toPath());
            }
        }

        try (FileInputStream in = new FileInputStream(file)) {
            this.config.load(in);
        }

        if (this.port == -1) {
            this.port = Integer.parseInt(this.config.getProperty("network.port"));
        }

        this.motd = new JSONObject(this.config.getProperty("server.motd"));
        this.maxPlayers = Integer.parseInt(this.config.getProperty("server.maxplayers"));

        log.info("Config loaded.");
    }

    private void configureLogger() {
        try {
            Logger rootLogger = LogManager.getLogManager().getLogger("");
            LimboLogFormatter formatter = new LimboLogFormatter();

            for (Handler handler : rootLogger.getHandlers()) {
                handler.setFormatter(formatter);
            }
        } catch (Exception e) {
            log.info("Error configuring logger. " + e.getMessage());
        }
    }
}
