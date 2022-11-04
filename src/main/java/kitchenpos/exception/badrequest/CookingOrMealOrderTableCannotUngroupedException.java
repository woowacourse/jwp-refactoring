package kitchenpos.exception.badrequest;

public class CookingOrMealOrderTableCannotUngroupedException extends BadRequestException {

    public CookingOrMealOrderTableCannotUngroupedException() {
        super("조리 중이거나 식사 중인 주문이 있는 테이블의 단체 지정을 해제할 수 없습니다.");
    }
}
