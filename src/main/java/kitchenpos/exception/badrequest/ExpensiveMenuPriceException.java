package kitchenpos.exception.badrequest;

public class ExpensiveMenuPriceException extends BadRequestException {

    public ExpensiveMenuPriceException() {
        super("메뉴 가격은 메뉴에 포함된 상품의 가격 총합보다 클 수 없습니다.");
    }
}
