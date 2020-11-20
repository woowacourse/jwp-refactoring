package kitchenpos.exception;

public class NotEnoughGuestsException extends RuntimeException {
    public NotEnoughGuestsException() {
        super("손님 숫자가 충분하지 않습니다");
    }
}
