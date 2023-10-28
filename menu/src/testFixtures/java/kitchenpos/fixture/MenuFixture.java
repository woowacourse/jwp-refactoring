package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.menu.application.dto.MenuProductDto;
import kitchenpos.menu_group.application.MenuGroupDto;
import kitchenpos.product.application.dto.ProductDto;

public class MenuFixture {

    public static MenuDto 후라이드치킨_DTO(
        final MenuGroupDto savedMenuGroupDto,
        final List<MenuProductDto> menuProductDtos,
        final BigDecimal price
    ) {
        return new MenuDto(
            null,
            "후라이드치킨",
            price,
            savedMenuGroupDto.getId(),
            menuProductDtos
        );
    }

    public static MenuProductDto createMenuProductDto(final ProductDto savedProductDto,
        final Long quantity) {
        return new MenuProductDto(
            null,
            null,
            savedProductDto.getId(),
            quantity
        );
    }
}
