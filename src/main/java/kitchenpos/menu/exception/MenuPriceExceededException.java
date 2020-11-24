package kitchenpos.menu.exception;

import kitchenpos.common.CreateFailException;

public class MenuPriceExceededException extends CreateFailException {
    public MenuPriceExceededException() {
        super("메뉴의 가격이 아이템들의 가격보다 값이 높습니다.");
    }
}
