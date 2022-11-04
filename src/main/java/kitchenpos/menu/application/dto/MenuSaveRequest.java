package kitchenpos.menu.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuSaveRequest {

    private String name;
    private Integer price;
    private Long menuGroupId;
    private List<MenuProductSaveRequest> menuProducts;

    public MenuSaveRequest(String name, Integer price, Long menuGroupId, List<MenuProductSaveRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toEntity() {
        return new Menu(Name.of(name), Price.from(price), menuGroupId);
    }

    public List<MenuProduct> toMenuProductEntities(Long menuId) {
        return menuProducts.stream()
            .map(it -> it.toEntity(menuId))
            .collect(Collectors.toList());
    }
}
