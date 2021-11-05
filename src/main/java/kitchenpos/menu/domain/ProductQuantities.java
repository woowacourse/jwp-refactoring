package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ProductQuantities {
    List<ProductQuantity> productQuantities;

    public ProductQuantities(List<ProductQuantity> productQuantities) {
        this.productQuantities = productQuantities;
    }

    public BigDecimal totalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ProductQuantity productQuantity : productQuantities) {
            totalPrice = totalPrice.add(productQuantity.multipleProductAndQuantity());
        }
        return totalPrice;
    }

    public List<MenuProduct> groupToMenuProduct(Menu menu) {
        return productQuantities.stream()
                .map(productQuantity -> new MenuProduct(menu, productQuantity))
                .collect(Collectors.toList());
    }
}
