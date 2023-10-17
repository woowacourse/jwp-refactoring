package kitchenpos.domain.menu;

import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.List;

public abstract class MenuPriceValidator {

    public static void validate(Menu menu, List<MenuProduct> menuProducts) {
        final BigDecimal price = menu.getPrice();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 포함한 메뉴 상품들의 가격 합보다 비쌀 수 없습니다.");
        }
    }
}
