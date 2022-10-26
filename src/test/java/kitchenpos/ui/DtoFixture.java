package kitchenpos.ui;

import java.math.BigDecimal;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
}
