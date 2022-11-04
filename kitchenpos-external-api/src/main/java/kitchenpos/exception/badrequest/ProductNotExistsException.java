package kitchenpos.exception.badrequest;

public class ProductNotExistsException extends BadRequestException {

    public ProductNotExistsException() {
        super("존재하지 않는 상품을 메뉴로 등록할 수 없습니다.");
    }
}
