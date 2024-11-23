package br.com.fiap.global;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 *
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        final ResourceConfig rc = new ResourceConfig().packages(
            "br.com.fiap.global.controller",
            "br.com.fiap.global.excecao"
        );
        
        logger.log(Level.INFO, "Iniciando o servidor no URI: {0}", BASE_URI);
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        logger.log(Level.INFO, "Quiz API iniciada com endpoints disponíveis em {0}", BASE_URI);
        logger.log(Level.INFO, "Pressione Ctrl-C para parar o servidor...");
        try {
            System.in.read();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao ler entrada do sistema", e);
        } finally {
            server.stop();
            logger.log(Level.INFO, "Servidor parado.");
        }
    }
}