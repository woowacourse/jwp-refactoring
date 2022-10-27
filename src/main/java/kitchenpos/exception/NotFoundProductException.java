package kitchenpos.exception;

public class NotFoundProductException extends IllegalArgumentException {

    public NotFoundProductException() {
        super("해당 상품이 존재하지 않습니다");
    }
}
