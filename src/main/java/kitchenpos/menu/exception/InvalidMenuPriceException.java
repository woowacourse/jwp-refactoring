package kitchenpos.menu.exception;

import java.math.BigDecimal;

import kitchenpos.common.CreateFailException;

public class InvalidMenuPriceException extends CreateFailException {
    public InvalidMenuPriceException(BigDecimal price) {
        super("유효하지않은 금액 입력입니다. price = " + price);
    }
}
