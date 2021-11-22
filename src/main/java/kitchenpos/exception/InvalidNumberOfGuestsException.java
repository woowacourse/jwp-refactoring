package kitchenpos.exception;

public class InvalidNumberOfGuestsException extends KitchenPosException {

    private static final String INVALID_NUMBER_OF_GUESTS = "손님의 수는 음수일 수 없습니다.";

    public InvalidNumberOfGuestsException() {
        super(INVALID_NUMBER_OF_GUESTS);
    }
}
