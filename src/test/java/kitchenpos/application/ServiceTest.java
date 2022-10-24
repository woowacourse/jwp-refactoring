package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
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

    protected Product 상품을_저장한다(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return productService.create(product);
    }

    protected MenuGroup 메뉴_그룹을_저장한다(String name) {
        MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName(name);

        return menuGroupService.create(menuGroup1);
    }

    protected MenuProduct 메뉴_상품을_생성한다(String productName, int productPrice, Long quantity) {
        Product product = 상품을_저장한다(productName, productPrice);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    protected Menu 메뉴를_저장한다(String menuName) {
        MenuProduct menuProduct1 = 메뉴_상품을_생성한다("상품 1", 1000, 1L);
        MenuProduct menuProduct2 = 메뉴_상품을_생성한다("상품 2", 2000, 1L);
        MenuGroup menuGroup = 메뉴_그룹을_저장한다("메뉴 그룹");

        Menu menu = new Menu();
        menu.setName(menuName);
        menu.setPrice(BigDecimal.valueOf(3000));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct1, menuProduct2));

        return menuService.create(menu);
    }
}
