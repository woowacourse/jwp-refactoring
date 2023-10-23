package kitchenpos.domain.exception;

import static kitchenpos.domain.product.Product.MINIMUM_PRODUCT_NAME_LENGTH;
import static kitchenpos.domain.product.Product.MINIMUM_PRODUCT_PRICE;

import java.math.BigDecimal;
import kitchenpos.common.KitchenPosException;

public class ProductException extends KitchenPosException {

    private static final String NO_PRICE_MESSAGE =
        "금액은 " + MINIMUM_PRODUCT_PRICE + "원 이상이어야 합니다. \n현재 금액: ";
    private static final String NO_PRODUCT_NAME_MESSAGE =
        "상품명은 최소 " + MINIMUM_PRODUCT_NAME_LENGTH + "자 이상이어야 합니다.";

    public ProductException(String message) {
        super(message);
    }

    public static class NoPriceException extends ProductException {

        public NoPriceException(final BigDecimal price) {
            super(NO_PRICE_MESSAGE + price);
        }
    }

    public static class NoProductNameException extends ProductException {

        public NoProductNameException() {
            super(NO_PRODUCT_NAME_MESSAGE);
        }
    }
}
