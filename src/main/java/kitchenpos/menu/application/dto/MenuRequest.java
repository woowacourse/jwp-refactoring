package kitchenpos.menu.application.dto;

import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {

    private String name;
    private Integer price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    MenuRequest() {
    }

    public MenuRequest(final String name,
                       final Integer price,
                       final Long menuGroupId,
                       final List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
            .map(MenuProductRequest::getProductId)
            .collect(Collectors.toUnmodifiableList());
    }

    public List<Long> getQuantities() {
        return menuProducts.stream()
            .map(MenuProductRequest::getQuantity)
            .collect(Collectors.toUnmodifiableList());
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
