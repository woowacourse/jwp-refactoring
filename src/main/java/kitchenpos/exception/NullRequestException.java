package kitchenpos.exception;

public class NullRequestException extends RuntimeException {
    public NullRequestException() {
        super("값이 비어있는 요청입니다");
    }
}
