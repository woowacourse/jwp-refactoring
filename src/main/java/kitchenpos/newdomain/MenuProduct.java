package kitchenpos.newdomain;

import kitchenpos.newdomain.vo.Price;
import kitchenpos.newdomain.vo.Quantity;

public class MenuProduct {

    private Product product;
    private Quantity quantity;

    public MenuProduct(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Price calculateTotalPrice() {
        return this.product.calculateTotalPrice(quantity);
    }
}
