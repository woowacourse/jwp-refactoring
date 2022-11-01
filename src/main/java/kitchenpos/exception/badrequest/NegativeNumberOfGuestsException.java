package kitchenpos.exception.badrequest;

public class NegativeNumberOfGuestsException extends BadRequestException {

    public NegativeNumberOfGuestsException() {
        super("인원 수는 0명 이하일 수 없습니다.");
    }
}
