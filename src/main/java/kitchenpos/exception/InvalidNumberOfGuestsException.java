package kitchenpos.exception;

public class InvalidNumberOfGuestsException extends RuntimeException {
    public InvalidNumberOfGuestsException() {
        super("방문한 손님 수가 잘못됐습니다.");
    }
}
