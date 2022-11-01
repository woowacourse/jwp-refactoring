package kitchenpos.exception.badrequest;

public class MenuNotExistsException extends BadRequestException {

    public MenuNotExistsException() {
        super("존재하지 않는 메뉴를 주문할 수 없습니다.");
    }
}
