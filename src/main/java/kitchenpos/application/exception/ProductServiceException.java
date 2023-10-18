package kitchenpos.application.exception;

import java.math.BigDecimal;
import kitchenpos.domain.exception.KitchenPosException;

public class ProductServiceException extends KitchenPosException {

    public ProductServiceException(String message) {
        super(message);
    }

    // TODO: 중복된 예외 존재 -> 도메인 로직? 서비스 로직?
    public static class NoPriceException extends ProductServiceException {

        private static final String NO_PRICE_MESSAGE = "금액은 0원 이상이어야 합니다. \n현재 금액: ";

        public NoPriceException(final BigDecimal price) {
            super(NO_PRICE_MESSAGE + price);
        }
    }
}
