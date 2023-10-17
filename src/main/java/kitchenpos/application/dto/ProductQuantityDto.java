package kitchenpos.application.dto;

import kitchenpos.domain.Product;

public class ProductQuantityDto {

    private Product product;
    private long quantity;

    public ProductQuantityDto(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
