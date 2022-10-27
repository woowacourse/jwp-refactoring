package kitchenpos.ui;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class DtoFixture {

    public static ProductDto getProduct() {
        final Product product = new Product(1L, "productName", BigDecimal.valueOf(1000L));
        return ProductDto.from(product);
    }

    public static MenuGroupDto getMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup(1L, "menuGroupName");
        return MenuGroupDto.from(menuGroup);
    }

    public static MenuDto getMenuDto() {
        final Product product = new Product(1L, "productName", BigDecimal.valueOf(1000L));
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(1L, 1L, product, 1));
        final Menu menu = new Menu(1L, "menuName", BigDecimal.valueOf(1000L), 1L, menuProducts);
        return MenuDto.from(menu);
    }
}
