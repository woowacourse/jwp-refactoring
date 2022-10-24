package kitchenpos.application;

import java.math.BigDecimal;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
public abstract class ServiceTestBase {

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected MenuProductDao menuProductDao;

    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected MenuDao menuDao;

    @Autowired
    protected OrderLineItemDao orderLineItemDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected TableGroupDao tableGroupDao;


    protected Product 상품_등록(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }

    protected Menu 메뉴_등록(final String name, final BigDecimal price, final Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);

        return menu;
    }

    protected MenuProduct 메뉴_상품_생성(Menu menu, Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    protected MenuProduct 메뉴_상품_생성(Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    protected OrderTable 주문_테이블_생성() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(2);

        return orderTable;
    }

    protected OrderTable 빈_주문_테이블_생성() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);

        return orderTable;
    }
}
