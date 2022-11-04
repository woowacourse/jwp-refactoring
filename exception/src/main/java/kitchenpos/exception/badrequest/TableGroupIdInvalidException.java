package kitchenpos.exception.badrequest;

public class TableGroupIdInvalidException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "테이블 그룹 아이디가 유효하지 않습니다";
    private static final String MESSAGE_FORMAT = "테이블 그룹 아이디가 유효하지 않습니다 : %d";

    public TableGroupIdInvalidException() {
        super(DEFAULT_MESSAGE);
    }

    public TableGroupIdInvalidException(final Long invalidTableGroupId) {
        super(String.format(MESSAGE_FORMAT, invalidTableGroupId));
    }
}
