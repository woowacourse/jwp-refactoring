package kitchenpos.domain.productquantity;

import kitchenpos.domain.product.Product;
import kitchenpos.domain.quantity.Quantity;
import kitchenpos.domain.price.Price;

public class ProductQuantity {

    private final Product product;
    private final Quantity quantity;

    public ProductQuantity(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Price getTotalPrice() {
        final Price productPrice = product.getPrice();
        return productPrice.multiply(quantity);
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
