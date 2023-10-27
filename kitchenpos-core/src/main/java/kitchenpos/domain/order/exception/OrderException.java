package kitchenpos.domain.order.exception;


import kitchenpos.exception.CustomException;

public class OrderException extends CustomException {

    public OrderException(final String message) {
        super(message);
    }

    public static class NotFoundOrderLineItemMenuExistException extends OrderException {

        public NotFoundOrderLineItemMenuExistException() {
            super("주문 항목 목록에 메뉴가 누락된 주문 항목이 존재합니다.");
        }
    }

    public static class CannotOrderStateByOrderTableEmptyException extends OrderException {

        public CannotOrderStateByOrderTableEmptyException() {
            super("주문 테이블이 비어있는 상태일 때 주문할 수 없습니다.");
        }
    }

    public static class NotFoundOrderException extends OrderException {

        public NotFoundOrderException() {
            super("주문을 찾을 수 없습니다.");
        }
    }

    public static class CannotChangeOrderStatusByCurrentOrderStatusException extends OrderException {


        public CannotChangeOrderStatusByCurrentOrderStatusException() {
            super("기존 주문의 주문 상태가 계산 완료인 주문은 상태를 변경할 수 없습니다.");
        }
    }
}
