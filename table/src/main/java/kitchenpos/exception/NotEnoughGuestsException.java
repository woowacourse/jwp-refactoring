package kitchenpos.exception;

public class NotEnoughGuestsException extends RuntimeException {

    private static final String MESSAGE = "방문한 손님 수가 0명 미만입니다.";

    public NotEnoughGuestsException() {
        super(MESSAGE);
    }
}
