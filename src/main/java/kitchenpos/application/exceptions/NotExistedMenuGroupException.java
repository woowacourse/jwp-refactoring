package kitchenpos.application.exceptions;

public class NotExistedMenuGroupException extends RuntimeException {
    public NotExistedMenuGroupException() {
        super("존재하지 않는 MenuGroup입니다.");
    }
}
