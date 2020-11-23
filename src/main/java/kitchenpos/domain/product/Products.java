package kitchenpos.domain.product;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.menu.Menu;
import kitchenpos.util.ValidateUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Products {
    private final Map<Product, Long> productsAndQuantities;

    private Products(Map<Product, Long> productsAndQuantities) {
        this.productsAndQuantities = productsAndQuantities;
    }

    public static Products from(Map<Product, Long> productsAndQuantities) {
        ValidateUtil.validateNonNull(productsAndQuantities);

        return new Products(productsAndQuantities);
    }

    public BigDecimal calculateMenuProductPriceSum() {
        BigDecimal menuProductPriceSum = BigDecimal.ZERO;

        for (Product product : this.productsAndQuantities.keySet()) {
            ProductPrice productPrice = product.getProductPrice();
            BigDecimal quantity = BigDecimal.valueOf(this.productsAndQuantities.get(product));
            menuProductPriceSum = menuProductPriceSum.add(productPrice.multiply(quantity));
        }

        return menuProductPriceSum;
    }

    public List<MenuProduct> createMenuProducts(Menu menu) {
        return this.productsAndQuantities.keySet().stream()
                .map(product -> MenuProduct.of(menu, product, this.productsAndQuantities.get(product)))
                .collect(Collectors.toList());
    }
}
