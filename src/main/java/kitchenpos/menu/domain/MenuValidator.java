package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.product.domain.Product;

public class MenuValidator {

    public void validatePriceUnderProductsSum(final BigDecimal price, final List<MenuProduct> menuProducts, final List<Product> products) {
        if (price.compareTo(calculateProductsSum(menuProducts, products)) > 0) {
            throw new IllegalArgumentException("price must be equal to or less than the sum of product prices");
        }
    }

    private BigDecimal calculateProductsSum(final List<MenuProduct> menuProducts, final List<Product> products) {
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < menuProducts.size(); i++) {
            validateMenuProductMatchProduct(menuProducts, products.get(i), i);
            sum = sum.add(products.get(i).getPrice().multiply(BigDecimal.valueOf(menuProducts.get(i).getQuantity())));
        }
        return sum;
    }

    private void validateMenuProductMatchProduct(final List<MenuProduct> menuProducts, final Product product,
        final int index) {
        if (!product.isSameId(menuProducts.get(index).getProductId())) {
            throw new IllegalArgumentException("menu product not match product");
        }
    }
}
