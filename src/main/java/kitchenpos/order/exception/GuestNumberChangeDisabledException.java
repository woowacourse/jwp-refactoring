package kitchenpos.order.exception;

public class GuestNumberChangeDisabledException extends IllegalArgumentException {

    private static final String MESSAGE = "비어 있는 테이블의 손님 수를 변경할 수 없습니다.";

    public GuestNumberChangeDisabledException() {
        super(MESSAGE);
    }
}
