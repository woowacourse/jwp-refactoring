package kitchenpos.exception;

public class AlreadyCompletionOrderStatusException extends IllegalArgumentException {

    private static final String MESSAGE = "이미 완료된 주문의 상태는 변경할 수 없습니다.";
}
