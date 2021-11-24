package kitchenpos.exception.table;

public class InvalidNumberOfGuestsException extends RuntimeException {
    private static final String MESSAGE = "테이블의 방문한 손님의 수는 음수일 수 없습니다.";

    public InvalidNumberOfGuestsException() {
        super(MESSAGE);
    }
}
