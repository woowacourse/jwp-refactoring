package kitchenpos.support;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class FixtureFactory {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    public Product 제품_생성() {
        return productService.create(new Product("상품", BigDecimal.valueOf(1000)));
    }

    public MenuGroup 메뉴_그룹_생성() {
        return menuGroupService.create(new MenuGroup("메뉴그룹"));
    }

    public Menu 메뉴_생성(Long menuGroupId, Product product) {
        return menuService.create(new Menu("메뉴", BigDecimal.valueOf(1000), menuGroupId, List.of(
                new MenuProduct(product, 1)
        )));
    }
}
