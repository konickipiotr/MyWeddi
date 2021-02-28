package com.myweddi.exception;

public class FailedSaveFileException  extends RuntimeException {
    public FailedSaveFileException() {
        super("WeddingApp Exception: Failed to save file");
    }
}
