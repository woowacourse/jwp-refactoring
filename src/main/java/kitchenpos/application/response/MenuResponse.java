package kitchenpos.application.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public static MenuResponse from(Menu menu) {
        final MenuResponse menuResponse = new MenuResponse();
        menuResponse.id = menu.getId();
        menuResponse.name = menu.getName();
        menuResponse.price = menu.getPrice();
        menuResponse.menuGroupId = menu.getMenuGroupId();
        menuResponse.menuProducts =
            menu.getMenuProducts()
                .stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());

        return menuResponse;
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
