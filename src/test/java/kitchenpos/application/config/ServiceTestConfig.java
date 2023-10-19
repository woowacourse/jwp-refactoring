package kitchenpos.application.config;

import kitchenpos.common.DataTestExecutionListener;
import kitchenpos.config.JpaConfig;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.vo.NumberOfGuests;
import kitchenpos.domain.vo.Price;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Import({DaoConfig.class, JpaConfig.class}) // TODO: jpa로 다 변환하고 dao config 제거하기
@TestExecutionListeners(value = DataTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class ServiceTestConfig {

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderLineItemDao orderLineItemDao;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

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

    protected TableGroup saveTableGroup(List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup(orderTables);
        // TODO: em 사용해보기, setter 제거
        orderTables.forEach(orderTable -> orderTable.setTableGroup(tableGroup));
        return tableGroupRepository.save(tableGroup);
    }
}
