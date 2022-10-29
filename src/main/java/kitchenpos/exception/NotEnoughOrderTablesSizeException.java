package kitchenpos.exception;

public class NotEnoughOrderTablesSizeException extends RuntimeException {
    public NotEnoughOrderTablesSizeException(final long count) {
        super(String.format("주문 테이블이 부족합니다. 입력받은 주문 테이블 갯수 : %d", count));
    }
}
