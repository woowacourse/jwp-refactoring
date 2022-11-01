package kitchenpos.exception;

public class OrderLineItemMenuException extends RuntimeException  {
    public OrderLineItemMenuException() {
        super("주문 제품의 메뉴가 잘못됐습니다.");
    }
}
