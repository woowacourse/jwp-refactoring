package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

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

    public static Menu 후라이드치킨(
        final MenuGroupDto savedMenuGroupDto,
        final List<MenuProduct> menuProduct
    ) {
        final Menu menu = new Menu();
        menu.setName("후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(16000));
        menu.setMenuGroupId(savedMenuGroupDto.getId());
        menu.setMenuProducts(menuProduct);
        return menu;
    }

    public static MenuProduct createMenuProduct(final ProductDto savedProductDto,
        final Long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(quantity);
        menuProduct.setProductId(savedProductDto.getId());
        return menuProduct;
    }
}
