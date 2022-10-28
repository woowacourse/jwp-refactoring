package kitchenpos.exception;

public class NotConvertableStatusException extends BadRequestException {

    private static final String ERROR_MESSAGE = "상태를 바꿀 수 없습니다.";

    public NotConvertableStatusException() {
        super(ERROR_MESSAGE);
    }
}
