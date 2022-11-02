package kitchenpos.menu.application.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuRequest {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts = new ArrayList<>();

    private MenuRequest() {
    }

    public MenuRequest(Long id, String name, BigDecimal price, Long menuGroupId,
        List<MenuProductRequest> menuProducts) {
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.stream()
            .map(MenuProductRequest::toEntity)
            .collect(Collectors.toUnmodifiableList());
    }

    public Menu toEntity() {
        return new Menu(id, name, price, menuGroupId, getMenuProducts());
    }
}
