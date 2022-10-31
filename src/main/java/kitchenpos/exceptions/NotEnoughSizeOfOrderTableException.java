package kitchenpos.exceptions;

public class NotEnoughSizeOfOrderTableException extends RuntimeException {

    public NotEnoughSizeOfOrderTableException(final int size) {
        super("테이블 개수는 " + size + "개 이상이어야 합니다.");
    }
}
