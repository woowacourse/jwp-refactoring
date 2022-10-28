package kitchenpos.exception;

public class TableGroupNotNullException extends BadRequestException {

    private static final String ERROR_MESSAGE = "테이블 그룹이 존재하여 비울 수 없습니다.";

    public TableGroupNotNullException() {
        super(ERROR_MESSAGE);
    }
}
