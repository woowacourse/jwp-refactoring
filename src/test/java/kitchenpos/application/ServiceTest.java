package kitchenpos.application;

import java.math.BigDecimal;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/init_schema.sql")
public abstract class ServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    MenuService menuService;

    protected Product 상품을_등록한다(final String name, final int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return productService.create(product);
    }

    protected MenuGroup 메뉴_그룹을_생성한다(final String name) {
        MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName(name);

        return menuGroupService.create(menuGroup1);
    }
}
