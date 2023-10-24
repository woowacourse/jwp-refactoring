package kitchenpos.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuPrice;

public class MenuResponse {

    private Long id;
    private String name;
    private int price;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {
    }

    public MenuResponse(final Long id, final String name, final int price,
                        final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu) {
        final List<MenuProductResponse> menuProducts = menu.getMenuProducts().stream()
            .map(MenuProductResponse::from)
            .collect(Collectors.toList());
        final MenuPrice menuPrice = menu.getPrice();

        return new MenuResponse(menu.getId(), menu.getName(), menuPrice.getValue().intValue(), menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
