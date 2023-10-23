package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

public class MenuProductPriceCalculator {

    private MenuProductPriceCalculator() {
    }

    public static Price calculateTotalPrice(List<MenuProduct> menuProducts, List<Product> products) {
        Map<Long, Product> productMap = convertToProductMap(products);

        return menuProducts.stream()
                .map(menuProduct -> calculateProductPrice(menuProduct, productMap))
                .reduce(new Price(BigDecimal.ZERO), Price::add);
    }

    private static Map<Long, Product> convertToProductMap(List<Product> products) {
        return products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    private static Price calculateProductPrice(MenuProduct menuProduct, Map<Long, Product> products) {
        Long productId = menuProduct.getProductId().getId();
        Product product = products.get(productId);

        Price productPrice = product.getPrice();
        long quantity = menuProduct.getQuantity();

        return productPrice.multiply(quantity);
    }
}
