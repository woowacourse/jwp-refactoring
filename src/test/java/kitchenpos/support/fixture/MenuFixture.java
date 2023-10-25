package kitchenpos.support.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.menu_group.application.MenuGroupDto;
import kitchenpos.menu.application.dto.MenuProductDto;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.product.application.dto.ProductDto;

public class MenuFixture {

    public static MenuGroup 두마리메뉴() {
        return new MenuGroup("두마리메뉴");
    }

    public static MenuGroup 한마리메뉴() {
        return new MenuGroup("한마리메뉴");
    }

    public static MenuGroup 순살파닭두마리메뉴() {
        return new MenuGroup("순살파닭두마리메뉴");
    }

    public static MenuGroup 신메뉴() {
        return new MenuGroup("신메뉴");
    }

    public static MenuGroupDto 두마리메뉴_DTO() {
        return new MenuGroupDto(null, "두마리메뉴");
    }

    public static MenuGroupDto 한마리메뉴_DTO() {
        return new MenuGroupDto(null, "한마리메뉴");
    }

    public static MenuGroupDto 순살파닭두마리메뉴_DTO() {
        return new MenuGroupDto(null, "순살파닭두마리메뉴");
    }

    public static MenuGroupDto 신메뉴_DTO() {
        return new MenuGroupDto(null, "신메뉴");
    }

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
