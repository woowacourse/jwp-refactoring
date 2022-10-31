package kitchenpos.exception;

public class OrderTableUnableUngroupingStatusException extends BadRequestException {

    private static final String ERROR_MESSAGE = "그룹화를 해제 할 수 없는 상태가 포함되어 있습니다.";

    public OrderTableUnableUngroupingStatusException() {
        super(ERROR_MESSAGE);
    }
}
