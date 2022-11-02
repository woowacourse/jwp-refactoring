package kitchenpos.product.event;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.dto.request.CreateMenuProductRequest;

public class VerifiedMenuProductsEvent {

    private BigDecimal price;
    private List<CreateMenuProductRequest> menuProducts;

    public VerifiedMenuProductsEvent(BigDecimal price, List<CreateMenuProductRequest> menuProducts) {
        this.price = price;
        this.menuProducts = menuProducts;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<CreateMenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
