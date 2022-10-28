package kitchenpos.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;

public class MenuCreateRequest {

    private String name;
    private int price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(final String name, final int price, final Long menuGroupId,
                             final List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toMenu() {
        return new Menu(
                null,
                name,
                new BigDecimal(price),
                menuGroupId,
                menuProducts.stream()
                        .map(MenuProductRequest::toMenuProduct)
                        .collect(Collectors.toList())
        );
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}

