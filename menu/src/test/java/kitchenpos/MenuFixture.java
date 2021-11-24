package kitchenpos;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuFixture {

    public static MenuGroup createMenuGroup() {
        return MenuGroup.builder()
                .id(1L)
                .name("메뉴그룹이름")
                .build();
    }

    public static MenuProduct createMenuProduct() {
        return MenuProduct.builder()
                .id(1L)
                .menuId(createMenu().getId())
                .productId(1L)
                .quantity(1L)
                .build();
    }

    public static MenuProduct createMenuProduct(Long id, Menu menu, Long quantity) {
        return MenuProduct.builder()
                .menuId(menu.getId())
                .productId(id)
                .quantity(quantity)
                .build();
    }

    public static Menu createMenu() {
        return Menu.builder()
                .id(1L)
                .name("메뉴이름")
                .price(BigDecimal.valueOf(1000))
                .menuGroupId(createMenuGroup().getId())
                .build();
    }

    public static Menu createMenu(Long id) {
        return Menu.builder()
                .id(id)
                .name("메뉴이름")
                .price(BigDecimal.valueOf(1000))
                .menuGroupId(createMenuGroup().getId())
                .build();
    }

    public static MenuRequest createMenuRequest(Menu menu) {
        final List<MenuProductRequest> menuProductRequests = Stream.of(createMenuProduct())
                .map(MenuProductRequest::new)
                .collect(Collectors.toList());
        return new MenuRequest(menu.getName(), menu.getPrice().longValue(), menu.getMenuGroupId(),
                menuProductRequests);
    }
}
