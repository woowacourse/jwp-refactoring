package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class TableException extends BaseException {

    public TableException(final String message, final HttpStatus status) {
        super(message, status);
    }

    public static class NoGuestException extends TableException {
        public NoGuestException() {
            super("손님은 최소 0명 이어야 합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class TableEmptyException extends TableException {
        public TableEmptyException() {
            super("테이블이 빈 상태에서는 손님수를 변경할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NotFoundException extends TableException {
        public NotFoundException(final Long id) {
            super(id + "ID에 해당하는 주문 테이블을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    public static class NotCompletionTableCannotChangeEmptyException extends TableException {
        public NotCompletionTableCannotChangeEmptyException() {
            super("주문이 완료되지 않은 테이블은 비울 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class TableGroupedTableCannotChangeEmptyException extends TableException {
        public TableGroupedTableCannotChangeEmptyException() {
            super("테이블 그룹에 속한 테이블은 비울 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
