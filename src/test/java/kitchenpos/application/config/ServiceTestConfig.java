package kitchenpos.application.config;

import kitchenpos.common.DataTestExecutionListener;
import kitchenpos.common.vo.Price;
import kitchenpos.config.JpaConfig;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.vo.NumberOfGuests;
import kitchenpos.product.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@Import(JpaConfig.class)
@TestExecutionListeners(value = DataTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class ServiceTestConfig {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Autowired
    protected ApplicationEventPublisher eventPublisher;

    protected Product saveProduct() {
        final Product product = new Product("여우가 좋아하는 피자", new Price(BigDecimal.valueOf(10000)));
        return productRepository.save(product);
    }

    protected MenuGroup saveMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup("여우가 좋아하는 메뉴 그룹");
        return menuGroupRepository.save(menuGroup);
    }

    protected Menu saveMenu(final MenuGroup menuGroup, final Product product) {
        final Menu menu = new Menu("여우 메뉴", new Price(BigDecimal.valueOf(2000)), menuGroup);
        menu.addMenuProducts(List.of(new MenuProduct(2L, menu, product)));
        return menuRepository.save(menu);
    }

    protected Order saveOrder(final OrderTable orderTable) {
        final Order order = new Order(OrderStatus.COOKING, orderTable);
        final Menu menu = saveMenu(saveMenuGroup(), saveProduct());
        final OrderLineItem orderLineItem = new OrderLineItem(1L, order, menu);
        order.addOrderLineItems(List.of(orderLineItem));
        return orderRepository.save(order);
    }

    protected OrderTable saveOccupiedOrderTable() {
        return orderTableRepository.save(new OrderTable(new NumberOfGuests(2), false));
    }

    protected OrderTable saveEmptyOrderTable() {
        return orderTableRepository.save(new OrderTable(new NumberOfGuests(2), true));
    }

    protected TableGroup saveTableGroup() {
        return tableGroupRepository.save(new TableGroup());
    }

    protected TableGroup saveTableGroup(List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();
        orderTables.forEach(orderTable -> orderTable.makeGroup(tableGroup));
        return tableGroupRepository.save(tableGroup);
    }
}
