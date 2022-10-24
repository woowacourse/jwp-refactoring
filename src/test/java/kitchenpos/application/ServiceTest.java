package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;
    @Autowired
    private ProductService productService;


    protected void 주문_항목을_추가한다(Order order) {
        Menu ramen = 메뉴를_생성한다("라면");
        Menu chapagetti = 메뉴를_생성한다("짜파게티");
        order.addOrderLineItem(new OrderLineItem(order.getId(), ramen.getId(), 1));
        order.addOrderLineItem(new OrderLineItem(order.getId(), chapagetti.getId(), 1));
    }

    protected Menu 메뉴를_생성한다(String name) {
        Product product = productService.create(new Product("맛있는 라면", new BigDecimal(1300)));
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(null, product.getId(), 1));

        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("면"));
        Menu ramen = menuService.create(new Menu(name, new BigDecimal(1200), menuGroup.getId(), menuProducts));
        menuProducts.get(0).setMenuId(ramen.getId());

        return ramen;
    }
}
