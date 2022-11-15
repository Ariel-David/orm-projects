package Utils;

public enum ExceptionMessage {

    MULTIPLE_PRIMARY_KEY("A table can not contain multiple primary keys."),
    MULTIPLE_AUTO_INCREMENT("A table can not contain multiple auto increment fields."),
    TRUNCATE("Couldn't truncate the table properly."),
    SQL_CONNECTION("Could'nt connect to the database."),
    EMPTY_NOTNULL_FIELD("Not null fields must be filled out before creation"),
    FIELDS_OF_OBJECT("Something went wrong when tried to get object's fields..."),

    RUNTIME("Runtime exception"),
    NEGATIVE_FIELD("This field can not be negative"),
    TOO_SHORT_STRING("This field can not be as short as inserted"),
    CREATE_TABLE("Couldn't create the table properly.");


    private final String message;

    private ExceptionMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
