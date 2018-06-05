package fr.isep.algoprogra.tp6.strategy;

public class TimeoutException extends RuntimeException {

    public TimeoutException() {
        super("Timeout reached!");
    }
}
