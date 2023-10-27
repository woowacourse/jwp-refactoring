package kitchenpos.domain.exception;

public class InvalidNumberOfGuestsException extends IllegalArgumentException {

    public InvalidNumberOfGuestsException() {
        super("유효하지 않은 방문한 손님 수입니다.");
    }
}
