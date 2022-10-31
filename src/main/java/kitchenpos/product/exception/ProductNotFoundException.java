package kitchenpos.product.exception;

import kitchenpos.common.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException() {
        super("상품을 찾을 수 없습니다.");
    }
}
