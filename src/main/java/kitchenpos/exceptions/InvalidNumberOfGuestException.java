package kitchenpos.exceptions;

public class InvalidNumberOfGuestException extends RuntimeException {

    public InvalidNumberOfGuestException() {
        super("유효한 고객 수가 아닙니다.");
    }
}
