package fr.mrmicky.ultimateparty.command;

public class PartySilentCommandException extends RuntimeException {

    public static final PartySilentCommandException INSTANCE = new PartySilentCommandException();

    private PartySilentCommandException() {
    }
}
