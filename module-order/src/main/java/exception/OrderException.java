package exception;

import org.springframework.http.HttpStatus;

public class OrderException extends BaseException {

    public OrderException(final String message, final HttpStatus status) {
        super(message, status);
    }

    public static class NoOrderLineItemsException extends OrderException {
        public NoOrderLineItemsException() {
            super("주문 항목은 필수입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NoMenuException extends OrderException {
        public NoMenuException() {
            super("메뉴에 존재하는 항목만 주문할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NoQuantityException extends OrderException {
        public NoQuantityException() {
            super("최소 한 개 이상의 메뉴를 주문하여야 합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidOrderStatusException extends OrderException {
        public InvalidOrderStatusException(final String value) {
            super(value + " 라는 주문 상태는 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class CompletionOrderChangeStatusException extends OrderException {
        public CompletionOrderChangeStatusException() {
            super("주문 상태가 완료된 주문은 주문 상태를 변경할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NoTableException extends OrderException {
        public NoTableException() {
            super("주문을 하기 위해선 주문 테이블이 필수입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class EmptyTableException extends OrderException {
        public EmptyTableException() {
            super("빈 상태의 주문 테이블에서는 주문을 할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NotFoundException extends OrderException {
        public NotFoundException(final Long id) {
            super(id + "ID를 가진 주문이 없습니다.", HttpStatus.NOT_FOUND);
        }
    }
}
