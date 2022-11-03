package kitchenpos.application;

import static kitchenpos.support.MenuFixture.MENU_PRICE_10000;
import static kitchenpos.support.MenuGroupFixture.MENU_GROUP_1;
import static kitchenpos.support.MenuProductFixture.MENU_PRODUCT_1;
import static kitchenpos.support.OrderLineItemFixture.ORDER_LINE_ITEM_1;
import static kitchenpos.support.ProductFixture.PRODUCT_PRICE_10000;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderMenuProductRepository;
import kitchenpos.dao.OrderMenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderMenu;
import kitchenpos.domain.OrderMenuProduct;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:truncate.sql")
@SuppressWarnings("NonAsciiCharacters")
public class ServiceFixture {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMenuRepository orderMenuRepository;
    @Autowired
    private OrderMenuProductRepository orderMenuProductRepository;

    protected Product 제품을_저장한다(final Product product) {
        return productRepository.save(product);
    }

    protected MenuGroup 메뉴그룹을_저장한다(final MenuGroup menuGroup) {
        return menuGroupRepository.save(menuGroup);
    }

    protected Menu 메뉴를_저장한다(final Menu menu) {
        return menuRepository.save(menu);
    }

    protected Menu 상품과_함께_메뉴를_저장한다(final MenuGroup menuGroup) {
        final Product product = 제품을_저장한다(PRODUCT_PRICE_10000.생성());
        return 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroup, List.of(MENU_PRODUCT_1.생성(product))));
    }

    protected TableGroup 테이블그룹을_저장한다(final TableGroup tableGroup) {
        return tableGroupRepository.save(tableGroup);
    }

    protected OrderTable 주문테이블을_저장한다(final OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    protected Order 주문을_저장한다(final Order order) {
        return orderRepository.save(order);
    }

    protected Order 주문항목과_함께_주문을_저장한다(final Long orderTableId, final OrderStatus orderStatus) {
        final MenuGroup menuGroup = 메뉴그룹을_저장한다(MENU_GROUP_1.생성());
        final Product product = 제품을_저장한다(PRODUCT_PRICE_10000.생성());
        final Menu menu = 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroup, List.of(MENU_PRODUCT_1.생성(product))));

        final OrderMenuProduct orderMenuProduct = new OrderMenuProduct(null, null, product.getName(),
                product.getPrice().getValue(), 1);
        final OrderMenu orderMenu = new OrderMenu(menu.getName(), menu.getPrice().getValue(), menuGroup.getName(),
                List.of(orderMenuProduct));
        주문메뉴를_저장한다(orderMenu);

        final List<OrderLineItem> orderLineItems = List.of(ORDER_LINE_ITEM_1.생성(orderMenu));
        return 주문을_저장한다(new Order(orderTableId, orderStatus, LocalDateTime.now(), orderLineItems));
    }

    protected OrderMenu 주문메뉴를_저장한다(final OrderMenu orderMenu) {
        return orderMenuRepository.save(orderMenu);
    }
}
