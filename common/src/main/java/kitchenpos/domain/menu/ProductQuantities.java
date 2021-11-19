package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;

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
}
