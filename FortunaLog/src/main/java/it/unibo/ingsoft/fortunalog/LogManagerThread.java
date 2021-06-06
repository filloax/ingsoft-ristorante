package it.unibo.ingsoft.fortunalog;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

public class LogManagerThread extends Thread {
    private Socket clientSocket = null;
    private String dir;

    private static String LOG_FILE = "log.txt";

    /**
	 * Constructor
	 * @param clientSocket
	 */
	public LogManagerThread(Socket clientSocket, String dir) {
		this.clientSocket = clientSocket;
        this.dir = dir;
	}

    private void logMessage(String msg) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Path logFile = Paths.get(dir, LOG_FILE);

        if (Files.exists(logFile)) {
            try {
                checkAndUpdateLogDate(logFile);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Impossibile rinominare vecchio log file: " + e.getMessage());
                return;
            }
        }

        OpenOption openOption = Files.exists(logFile) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE_NEW;

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(logFile, Charset.defaultCharset(), openOption))) {
            pw.println(String.format("%s - %s", timestamp, msg));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore in scrittura log: " + e.getMessage());
            return;
        }
    }

    private void checkAndUpdateLogDate(Path logFile) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(logFile, BasicFileAttributes.class);
        LocalDate creationDate = LocalDate.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());

        if (!creationDate.equals(LocalDate.now())) {
            String newName = "log-" + creationDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + ".txt";
            Files.move(logFile, Path.of(dir, newName));
        }
    }

    private void pushDirFilenames() {
        Path dirPath = Paths.get(dir);

        try (DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
                for (Path path : stream) {
                    if (!Files.isDirectory(path)) {
                        dos.writeUTF(path.getFileName().toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore in lettura cartella: " + e.getMessage());
                logMessage("Errore in lettura cartella: " + e.getMessage());
            }

            dos.writeUTF("#%done"); // caratteri non validi per nome di file
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore in comunicazione nel push filename: " + e.getMessage());
            logMessage("Errore in comunicazione nel push filename: " + e.getMessage());
        }
    }

    public void run() {
        try(DataInputStream dis = new DataInputStream(clientSocket.getInputStream())) {
            try {
                for(;;) {
                    int op = dis.readInt();
                    LogManagerServices operation;
                    try {
                        operation = LogManagerServices.get(op);
                    } catch (NoSuchElementException e) {
                        System.err.println("Operazione sconosciuta: " + op);
                        logMessage("Richiesta operazione sconosciuta " + op + " da " + clientSocket.getRemoteSocketAddress());
                        continue;
                    }

                    if (operation == LogManagerServices.LOG) {
                        String msg = dis.readUTF();
                        logMessage(msg);
                    } else if (operation == LogManagerServices.LIST_FILES) {
                        pushDirFilenames();
                    }
                }
            } catch (SocketTimeoutException e) {
                System.err.println("Timeout, chiusura");
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                clientSocket.close();
            } catch (SocketTimeoutException e) {
                System.err.println("Timeout scattato");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
