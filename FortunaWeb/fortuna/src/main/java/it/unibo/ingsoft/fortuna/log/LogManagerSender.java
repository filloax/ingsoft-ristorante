package it.unibo.ingsoft.fortuna.log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.ConfigProps;

@Component
public class LogManagerSender implements ILogManager {
    @Autowired
    private ConfigProps config;

    private Socket socket;

    @Override
    public void scriviMessaggio(String msg) {
        try {
            DataOutputStream dos = new DataOutputStream(getSocket().getOutputStream());

            dos.writeInt(LogManagerServices.LOG.getValue());
            dos.writeUTF(msg);

        } catch (IOException e) {
            e.printStackTrace();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            System.err.println(String.format("%s: Tentativo di log remoto fallito! Messaggio era: %s", timestamp, msg));
        }
    }

    @Override
    public void scriviOperazione(String ip, String operazione) {
        scriviMessaggio(String.format("%s: %s", ip, operazione));
    }

    @Override
    public List<String> listaLogFile(LocalDate from, LocalDate to) {
        try(DataOutputStream dos = new DataOutputStream(getSocket().getOutputStream());
        DataInputStream dis = new DataInputStream(getSocket().getInputStream())) {
            dos.writeInt(LogManagerServices.LIST_FILES.getValue());

            String current;
            List<String> out = new ArrayList<>();

            while (!"#%done".equals(current = dis.readUTF())) {
                out.add(current);
            }

            return out;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    

    private void apriConnessione() throws IOException {
        InetAddress address = InetAddress.getByName(config.getLog().getHost());
        socket = new Socket(address, config.getLog().getPort());
        socket.setSoTimeout(120000);
    }


    public ConfigProps getConfig() {
        return this.config;
    }

    public void setConfig(ConfigProps config) {
        this.config = config;
    }

    public Socket getSocket() throws IOException {
        if (socket == null || socket.isClosed() || socket.isOutputShutdown() || socket.isInputShutdown() || !socket.isConnected())
            apriConnessione();
        return socket;
    }
}
