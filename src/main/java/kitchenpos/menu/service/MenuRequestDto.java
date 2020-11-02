package kitchenpos.menu.service;

import kitchenpos.product.domain.Product;

public class MenuRequestDto {

    private final Product product;
    private final Long quantity;

    public MenuRequestDto(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }
}
