package kitchenpos.application;


import static kitchenpos.support.MenuFixture.메뉴_생성;
import static kitchenpos.support.MenuGroupFixture.메뉴_그룹1;
import static kitchenpos.support.OrderFixture.주문_생성;
import static kitchenpos.support.ProductFixture.상품1;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ServiceTest {

    @Autowired
    protected ProductService productService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected TableService tableService;

    protected Product 상품_등록(final Product product) {
        return productService.create(product);
    }

    protected OrderTable 주문_테이블_등록(final OrderTable orderTable) {
        return tableService.create(orderTable);
    }

    protected TableGroup 테이블_그룹_등록(final TableGroup tableGroup) {
        return tableGroupService.create(tableGroup);
    }

    protected MenuGroup 메뉴_그룹_등록(final MenuGroup menuGroup) {
        return menuGroupService.create(menuGroup);
    }

    protected Order 주문_등록(final Order order) {
        return orderService.create(order);
    }

    protected Menu 메뉴_등록(final Menu menu) {
        return menuService.create(menu);
    }

}
