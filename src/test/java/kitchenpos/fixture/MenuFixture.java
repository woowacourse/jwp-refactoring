package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuProductDto;

public enum MenuFixture {

    LUNCH_SPECIAL(1L, "Lunch Special", BigDecimal.valueOf(30000L), 1L,
        List.of(MenuProductFixture.FRIED_CHICKEN_MENU_PRODUCT.toDto()));

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductDto> menuProductDtos;

    MenuFixture(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductDto> menuProductDtos) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductDtos = menuProductDtos;
    }

    public static MenuDto computeDefaultMenuDto(Consumer<MenuDto> consumer) {
        MenuDto menuDto = new MenuDto();
        menuDto.setId(1L);
        menuDto.setName("후라이드치킨");
        menuDto.setPrice(BigDecimal.valueOf(0L));
        menuDto.setMenuGroupId(1L);
        menuDto.setMenuProductDtos(List.of(MenuProductFixture.FRIED_CHICKEN_MENU_PRODUCT.toDto()));
        consumer.accept(menuDto);
        return menuDto;
    }

    public MenuDto toDto() {
        MenuDto menuDto = new MenuDto();
        menuDto.setId(id);
        menuDto.setName(name);
        menuDto.setPrice(price);
        menuDto.setMenuGroupId(menuGroupId);
        menuDto.setMenuProductDtos(menuProductDtos);
        return menuDto;
    }
}
