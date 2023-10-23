package kitchenpos.exception.orderTableException;

public class OrderTableNotFoundException extends OrderTableExcpetion {
    public static final String error = "OrderTable을 찾을 수 없습니다.";

    public OrderTableNotFoundException() {
        super(error);
    }
}
