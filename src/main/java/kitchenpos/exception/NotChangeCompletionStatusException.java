package kitchenpos.exception;

public class NotChangeCompletionStatusException extends KitchenPosException {

    private static final String NOT_CHANGE_COMPLETION_STATUS = "계산 완료된 주문은 상태를 변경할 수 없습니다.";

    public NotChangeCompletionStatusException() {
        super(NOT_CHANGE_COMPLETION_STATUS);
    }
}
