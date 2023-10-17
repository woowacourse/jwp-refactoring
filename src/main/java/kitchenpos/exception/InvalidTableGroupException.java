package kitchenpos.exception;

public class InvalidTableGroupException extends RuntimeException{
    private final static String error = "잘못된 Table Group 입니다.";
    public InvalidTableGroupException() {
        super(error);
    }
}
