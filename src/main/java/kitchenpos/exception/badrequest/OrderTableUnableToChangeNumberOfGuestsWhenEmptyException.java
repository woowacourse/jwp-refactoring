package kitchenpos.exception.badrequest;

public class OrderTableUnableToChangeNumberOfGuestsWhenEmptyException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "주문 불가 테이블의 고객 인원수를 수정할 수 없습니다";

    public OrderTableUnableToChangeNumberOfGuestsWhenEmptyException() {
        super(DEFAULT_MESSAGE);
    }
}
