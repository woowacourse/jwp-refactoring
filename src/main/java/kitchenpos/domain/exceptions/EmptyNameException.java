package kitchenpos.domain.exceptions;

public class EmptyNameException extends RuntimeException {
    public EmptyNameException() {
        super("이름이 비었습니다.");
    }
}
