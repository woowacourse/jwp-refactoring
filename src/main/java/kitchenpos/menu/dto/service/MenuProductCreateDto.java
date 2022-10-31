package kitchenpos.menu.dto.service;

import kitchenpos.product.domain.Product;

public class MenuProductCreateDto {

    private final Product product;
    private final long quantity;

    public MenuProductCreateDto(Product product, long quantity) {
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
