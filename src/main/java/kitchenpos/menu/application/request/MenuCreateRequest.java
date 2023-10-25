package kitchenpos.menu.application.request;

import java.util.List;
import kitchenpos.dto.ProductQuantityDto;

public class MenuCreateRequest {

    private String name;
    private long price;
    private Long menuGroupId;
    private List<ProductQuantityDto> menuProducts;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, long price, Long menuGroupId, List<ProductQuantityDto> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<ProductQuantityDto> getMenuProducts() {
        return menuProducts;
    }
}
