package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class MenuValidator {
    private Menu menu;

    public MenuValidator(Menu menu) {
        this.menu = menu;
    }

    public void validateToCreate(BigDecimal totalMenuProductsPrice) {
        validateMenuPrice();
        validateTotalMenuProductsPrice(totalMenuProductsPrice);
    }

    private void validateMenuPrice() {
        BigDecimal price = menu.getPrice();
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴의 가격을 입력하지 않으셨거나, 적절하지 않은 가격을 입력하셨습니다.");
        }
    }

    private void validateTotalMenuProductsPrice(BigDecimal totalMenuProductsPrice) {
        BigDecimal price = menu.getPrice();
        if (price.compareTo(totalMenuProductsPrice) > 0) {
            throw new IllegalArgumentException("메뉴 가격이, 해당 매뉴의 전체 구성 상품을 합친 가격이보다 낮아야 합니다.");
        }
    }

}
