package edu.ap.softwareproject.api.interfaces;

public interface ExceptionHelper {
    void warn(Exception exception);
    void warn(String message, Exception exception);
    void info(String message);
    void infoReport(String message);
}
