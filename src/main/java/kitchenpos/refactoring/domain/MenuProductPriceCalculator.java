package kitchenpos.refactoring.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MenuProductPriceCalculator {

    public Price calculateTotalPrice(List<MenuProduct> menuProducts, Map<Long, Product> products) {
        return menuProducts.stream()
                .map(menuProduct -> multiply(menuProduct, products))
                .reduce(new Price(BigDecimal.ZERO), Price::add);
    }

    private Price multiply(MenuProduct menuProduct, Map<Long, Product> products) {
        Long productId = menuProduct.getProductId().getId();
        Product product = products.get(productId);
        Price price = product.getPrice();
        long quantity = menuProduct.getQuantity();

        return price.multiply(quantity);
    }
}
