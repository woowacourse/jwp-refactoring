package kitchenpos.exception;

public class UnableToGroupingException extends BadRequestException {

    private static final String ERROR_MESSAGE = "그룹화 할 수 없는 항목이 있습니다.";

    public UnableToGroupingException() {
        super(ERROR_MESSAGE);
    }
}
