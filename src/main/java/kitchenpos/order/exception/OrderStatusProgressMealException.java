package kitchenpos.order.exception;

public class OrderStatusProgressMealException extends RuntimeException {

    public OrderStatusProgressMealException() {
        super("조리 혹은 식사중인 테이블은 그룹 해제가 불가능합니다.");
    }
}
