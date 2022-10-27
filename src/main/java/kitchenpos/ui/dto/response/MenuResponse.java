package kitchenpos.ui.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuDto;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductResponse> menuProducts;

    private MenuResponse(final Long id,
                         final String name,
                         final BigDecimal price,
                         final Long menuGroupId,
                         final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }


    public static MenuResponse from(final MenuDto menuDto) {
        List<MenuProductResponse> menuProductResponses = menuDto.getMenuProducts()
                .stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());

        return new MenuResponse(menuDto.getId(),
                menuDto.getName(),
                menuDto.getPrice(),
                menuDto.getMenuGroupId(),
                menuProductResponses);
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
