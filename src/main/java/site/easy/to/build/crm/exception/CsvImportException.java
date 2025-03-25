package site.easy.to.build.crm.exception;

public class CsvImportException extends Exception {
    public CsvImportException(String message) {
        super(message);
    }

    public CsvImportException(String message, Throwable cause) {
        super(message, cause);
    }
}