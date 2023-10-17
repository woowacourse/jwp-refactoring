package kitchenpos.exception;

public class OrderTableNotFoundException extends RuntimeException{
    public static final String error = "OrderTable을 찾을 수 없습니다.";
    public OrderTableNotFoundException() {
        super(error);
    }
}
