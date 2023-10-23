package kitchenpos.domain.exception;

public class InvalidNameException extends IllegalArgumentException {

    public InvalidNameException() {
        super("유효한 이름이 아닙니다.");
    }
}
