package kitchenpos.dto.menu;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.dto.menuproduct.MenuProductResponse;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductResponse> menuProducts;

    public MenuResponse(Menu menu, List<MenuProductResponse> menuProductResponses) {
        this(menu.getId(), menu.getName(), BigDecimal.valueOf(menu.getPriceAsInt()), menu.getMenuGroupId(), menuProductResponses);
    }

    public MenuResponse(
        Long id,
        String name,
        BigDecimal price,
        Long menuGroupId,
        List<MenuProductResponse> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
