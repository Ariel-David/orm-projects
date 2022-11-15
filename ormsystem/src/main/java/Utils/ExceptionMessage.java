package Utils;

public enum ExceptionMessage {

    MULTIPLE_PRIMARY_KEY("A table can not contain multiple primary keys"),
    MULTIPLE_AUTO_INCREMENT("A table can not contain multiple auto increment fields");

    private final String message;

    private ExceptionMessage(final String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
