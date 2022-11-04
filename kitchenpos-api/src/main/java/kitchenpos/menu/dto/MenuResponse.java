package kitchenpos.menu.dto;

import static java.util.stream.Collectors.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.Menu;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(final Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice().getValue();
        this.menuGroupId = menu.getMenuGroupId();
        this.menuProducts = menu.getMenuProducts()
                .stream()
                .map(it -> new MenuProductResponse(menu.getId(), it))
                .collect(toList());
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
