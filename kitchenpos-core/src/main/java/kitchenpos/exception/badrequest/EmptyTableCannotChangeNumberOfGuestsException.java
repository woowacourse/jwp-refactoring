package kitchenpos.exception.badrequest;

public class EmptyTableCannotChangeNumberOfGuestsException extends BadRequestException {

    public EmptyTableCannotChangeNumberOfGuestsException() {
        super("빈 테이블의 인원 수를 변경할 수 없습니다.");
    }
}
