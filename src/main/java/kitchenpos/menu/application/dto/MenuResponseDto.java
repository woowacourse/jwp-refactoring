package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.model.Menu;
import kitchenpos.menuproduct.application.dto.MenuProductResponseDto;
import kitchenpos.menuproduct.model.MenuProduct;

public class MenuResponseDto {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductResponseDto> menuProductResponses;

    public static MenuResponseDto from(Menu savedMenu, List<MenuProduct> savedMenuProducts) {
        List<MenuProductResponseDto> menuProductResponses = MenuProductResponseDto.listOf(savedMenuProducts);

        return new MenuResponseDto(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(),
            savedMenu.getMenuGroupId(), menuProductResponses);
    }

    public MenuResponseDto(Long id, String name, BigDecimal price, Long menuGroupId,
        List<MenuProductResponseDto> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductResponses = menuProductResponses;
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

    public List<MenuProductResponseDto> getMenuProductResponses() {
        return menuProductResponses;
    }
}
