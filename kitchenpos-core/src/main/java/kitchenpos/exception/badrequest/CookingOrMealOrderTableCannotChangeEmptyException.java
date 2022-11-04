package kitchenpos.exception.badrequest;

public class CookingOrMealOrderTableCannotChangeEmptyException extends BadRequestException {

    public CookingOrMealOrderTableCannotChangeEmptyException() {
        super("조리 중이거나 식사 중인 주문이 있는 테이블의 빈 테이블 여부를 변경할 수 없습니다.");
    }
}
