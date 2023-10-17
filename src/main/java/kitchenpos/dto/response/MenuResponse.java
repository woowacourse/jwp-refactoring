package kitchenpos.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.entity.Menu;
import kitchenpos.dto.MenuProductDto;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductDto> menuProducts;

    private MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductDto> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu) {

        final List<MenuProductDto> menuProductDtos = menu.getMenuProducts().stream()
                .map(MenuProductDto::from)
                .collect(Collectors.toList());

        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroup().getId(),
                menuProductDtos
        );
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

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }
}
