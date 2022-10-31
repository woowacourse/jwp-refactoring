package kitchenpos.menu.exception;

import kitchenpos.common.exception.InvalidRequestException;

public class InvalidMenuPriceCreateException extends InvalidRequestException {

    public InvalidMenuPriceCreateException() {
        super("올바르지 않은 메뉴 가격입니다.");
    }
}
