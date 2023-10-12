package kitchenpos.exception;

public class OrderTableException extends RuntimeException {

    public OrderTableException(final String message) {
        super(message);
    }

    public static class NotFoundOrderTableException extends OrderTableException {

        public NotFoundOrderTableException() {
            super("[ERROR] 해당하는 OrderTable이 존재하지 않습니다.");
        }
    }

    public static class AlreadyExistTableGroupException extends OrderTableException {

        public AlreadyExistTableGroupException() {
            super("[ERROR] 이미 Table Group이 존재합니다.");
        }
    }

    public static class CannotChangeEmptyStateByOrderStatusException extends OrderTableException {

        public CannotChangeEmptyStateByOrderStatusException() {
            super("[ERROR] 주문 테이블의 주문 상태가 조리중이거나 식사중일 때 주문 테이블의 상태를 변경할 수 없습니다.");
        }
    }

    public static class CannotChangeNumberOfGuestsStateInEmptyException extends OrderTableException {


        public CannotChangeNumberOfGuestsStateInEmptyException() {
            super("[ERROR] 주문 테이블이 비어있는 상태에서 손님 수를 변경할 수 없습니다.");
        }
    }
}
