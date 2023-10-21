package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.application.dto.MenuProductDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.domain.MenuGroup;

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
