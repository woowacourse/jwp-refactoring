package kitchenpos.exception;

public class InvalidGuestNumberException extends IllegalArgumentException {

    private static final String MESSAGE = "고객의 수는 1명 이상이어야 합니다.";

    public InvalidGuestNumberException() {
        super(MESSAGE);
    }
}
