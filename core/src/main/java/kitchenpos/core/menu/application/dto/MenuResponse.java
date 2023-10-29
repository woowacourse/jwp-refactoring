package kitchenpos.core.menu.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.core.menu.domain.Menu;
import kitchenpos.core.product.domain.Price;

public class MenuResponse {
    @JsonProperty
    private Long id;
    @JsonProperty
    private String name;
    @JsonUnwrapped
    private Price price;
    @JsonProperty
    private Long menuGroupId;
    @JsonProperty
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(final Long id, final String name, final Price price, final Long menuGroupId,
                        final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                MenuProductResponse.from(menu.getMenuProducts())
        );
    }

    public static List<MenuResponse> from(final List<Menu> menus) {
        return menus
                .stream().map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
