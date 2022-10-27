package kitchenpos.dto.service;

import kitchenpos.domain.Product;

public class MenuProductCreateDto {

    private Product product;
    private long quantity;

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
