package kitchenpos.application.exceptions;

public class NotExistedProductException extends RuntimeException {
    public NotExistedProductException() {
        super("존재하지 않는 Product입니다.");
    }
}
