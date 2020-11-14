package kitchenpos.exception;

public class OrderTableNotFoundException extends RuntimeException {
    public OrderTableNotFoundException(Long orderTableId) {
        super("아이디가 " + orderTableId + "인 OrderTable을 찾을 수 없습니다!");
    }
}
