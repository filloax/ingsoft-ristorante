package it.unibo.ingsoft.fortuna.autenticazione;

public interface IAutenticazione {
    public boolean autentica(String username, String password) throws AutenticazioneException;
    public void setUsername(String username, String password, String newUsername) throws AutenticazioneException;
    public void setPassword(String username, String password, String newPassword) throws AutenticazioneException;
}
