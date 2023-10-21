package kitchenpos.application.dto;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;

public class MenuDto {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductDto> menuProducts;

    public MenuDto(
        final Long id,
        final String name,
        final BigDecimal price,
        final Long menuGroupId,
        final List<MenuProductDto> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuDto from(final Menu savedMenu) {
        final List<MenuProductDto> menuProductDtos = savedMenu.getMenuProducts()
            .stream()
            .map(MenuProductDto::from)
            .collect(toUnmodifiableList());
        return new MenuDto(
            savedMenu.getId(),
            savedMenu.getName(),
            savedMenu.getPrice().getValue(),
            savedMenu.getMenuGroupId(),
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
