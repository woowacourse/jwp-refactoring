package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.request.CreateMenuRequest;

public class CreateMenuProductsEvent {

    private final CreateMenuRequest request;
    private List<MenuProduct> menuProducts;

    public CreateMenuProductsEvent(CreateMenuRequest request) {
        this.request = request;
    }

    public List<MenuProductDto> getMenuProductDtos() {
        return request.getMenuProducts();
    }

    public BigDecimal getPrice(){
        return request.getPrice();
    }

    public void setMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
