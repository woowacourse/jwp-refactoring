package kitchenpos.exception;

public class GuestSizeException extends BadRequestException {

    private static final String ERROR_MESSAGE = "손님 수를 변경할 수 없습니다.";

    public GuestSizeException() {
        super(ERROR_MESSAGE);
    }
}
