package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.ui.dto.request.MenuCreationRequest;

public class MenuCreationDto {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductCreationDto> menuProducts;

    private MenuCreationDto(final String name,
                            final BigDecimal price,
                            final Long menuGroupId,
                            final List<MenuProductCreationDto> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuCreationDto from(final MenuCreationRequest menuCreationRequest) {
        List<MenuProductCreationDto> menuProductCreationDtos = menuCreationRequest.getMenuProducts()
                .stream()
                .map(MenuProductCreationDto::from)
                .collect(Collectors.toList());

        return new MenuCreationDto(menuCreationRequest.getName(),
                menuCreationRequest.getPrice(),
                menuCreationRequest.getMenuGroupId(),
                menuProductCreationDtos);
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

    public List<MenuProductCreationDto> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public String toString() {
        return "MenuCreationDto{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", menuGroupId=" + menuGroupId +
                ", menuProducts=" + menuProducts +
                '}';
    }
}
