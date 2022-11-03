package kitchenpos.menu.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuResponse {

    private Long id;
    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductResponseDto> menuProducts;

    private MenuResponse() {
    }

    public MenuResponse(final Long id, final String name, final Long price, final Long menuGroupId,
                        final List<MenuProductResponseDto> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(final Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().longValue(), menu.getMenuGroupId(),
                toDtos(menu.getMenuProducts()));
    }

    private static List<MenuProductResponseDto> toDtos(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductResponseDto::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponseDto> getMenuProducts() {
        return menuProducts;
    }
}
