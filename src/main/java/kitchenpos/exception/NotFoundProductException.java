package kitchenpos.exception;

public class NotFoundProductException extends KitchenPosException {

    private static final String NOT_FOUND_PRODUCT = "존재하지 않는 상품 ID 입니다.";

    public NotFoundProductException() {
        super(NOT_FOUND_PRODUCT);
    }
}
