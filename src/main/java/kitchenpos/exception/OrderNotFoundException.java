package kitchenpos.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long orderId) {
        super("아이디가 " + orderId + "인 Order를 찾을 수 없습니다!");
    }
}
