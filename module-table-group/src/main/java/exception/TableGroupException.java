package exception;

import org.springframework.http.HttpStatus;

public abstract class TableGroupException extends BaseException {

    public TableGroupException(final String message, final HttpStatus status) {
        super(message, status);
    }

    public static class NoMinimumOrderTableSizeException extends TableGroupException {
        public NoMinimumOrderTableSizeException() {
            super("단체 지정은 2개 이상의 테이블들과 가능합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class HasEmptyTableException extends TableGroupException {
        public HasEmptyTableException() {
            super("빈 테이블과는 단체 지정을 할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class HasAlreadyGroupedTableException extends TableGroupException {
        public HasAlreadyGroupedTableException() {
            super("이미 단체 지정이 된 테이블과는 단체지정을 할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class CannotUngroupException extends TableGroupException {
        public CannotUngroupException() {
            super("조리중/식사중 에는 그룹을 해제할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NoOrderTableException extends TableGroupException {
        public NoOrderTableException() {
            super("주문 테이블을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }
    }

    public static class NotFoundException extends TableGroupException {
        public NotFoundException(final Long id) {
            super(id + "ID 로 지정된 그룹이 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
