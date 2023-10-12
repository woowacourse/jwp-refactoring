package kitchenpos.application;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
public class IntegrationTest {

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    protected Menu 맛있는_메뉴() {
        return 메뉴(메뉴_그룹(),
                BigDecimal.valueOf(5),
                "맛있는 메뉴",
                메뉴_상품(상품("상품1", BigDecimal.valueOf(1)), 3),
                메뉴_상품(상품("상품2", BigDecimal.valueOf(2)), 2)
        );
    }

    protected Menu 메뉴(MenuGroup 메뉴_그룹, BigDecimal 가격, String 이름, MenuProduct... 메뉴_상품들) {
        Menu menu = new Menu();
        menu.setPrice(가격);
        menu.setMenuGroupId(메뉴_그룹.getId());
        menu.setName(이름);
        menu.setMenuProducts(Arrays.asList(메뉴_상품들));
        return menuDao.save(menu);
    }

    protected MenuGroup 메뉴_그룹() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        return menuGroupDao.save(menuGroup);
    }

    protected Product 상품(String 이름, BigDecimal 가격) {
        Product product = new Product();
        product.setName(이름);
        product.setPrice(가격);
        return productDao.save(product);
    }

    protected MenuProduct 메뉴_상품(Product 상품, long 수량) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(상품.getId());
        menuProduct.setQuantity(수량);
        return menuProduct;
    }
}
