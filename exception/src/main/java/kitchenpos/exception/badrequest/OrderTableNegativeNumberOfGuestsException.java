package kitchenpos.exception.badrequest;

public class OrderTableNegativeNumberOfGuestsException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "테이블 고객 인원수는 0 이상이어야 합니다";
    private static final String MESSAGE_FORMAT = "테이블 고객 인원수는 0 이상이어야 합니다 : %s";

    public OrderTableNegativeNumberOfGuestsException() {
        super(DEFAULT_MESSAGE);
    }

    public OrderTableNegativeNumberOfGuestsException(final int numberOfGuests) {
        super(String.format(MESSAGE_FORMAT, numberOfGuests));
    }
}
