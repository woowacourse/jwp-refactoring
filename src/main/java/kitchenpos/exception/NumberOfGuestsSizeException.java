package kitchenpos.exception;

public class NumberOfGuestsSizeException extends IllegalArgumentException {

    public NumberOfGuestsSizeException() {
        super("손님의 숫자는 0보다 작을 수 없습니다");
    }
}
