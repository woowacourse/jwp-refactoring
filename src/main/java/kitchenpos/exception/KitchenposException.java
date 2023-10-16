package kitchenpos.exception;

public class KitchenposException extends RuntimeException {

    private final ExceptionInformation exceptionInformation;

    public KitchenposException(ExceptionInformation exceptionInformation) {
        super();
        this.exceptionInformation = exceptionInformation;
    }

    public int getCode() {
        return exceptionInformation.getCode();
    }

    @Override
    public String getMessage() {
        return exceptionInformation.getMessage();
    }
}
