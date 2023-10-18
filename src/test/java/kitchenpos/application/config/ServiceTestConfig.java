package kitchenpos.application.config;

import kitchenpos.common.DataTestExecutionListener;
import kitchenpos.config.JpaConfig;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.vo.Price;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Import({DaoConfig.class, JpaConfig.class}) // TODO: jpa로 다 변환하고 dao config 제거하기
@TestExecutionListeners(value = DataTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class ServiceTestConfig {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderLineItemDao orderLineItemDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected TableGroupDao tableGroupDao;

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
        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now().minusMinutes(10));
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(new ArrayList<>());
        return orderDao.save(order);
    }

    protected OrderTable saveOrderTable(final TableGroup tableGroup) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(tableGroup.getId());
        return orderTableDao.save(orderTable);
    }

    protected OrderTable saveOrderTable() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        return orderTableDao.save(orderTable);
    }

    protected TableGroup saveTableGroup() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now().minusMinutes(30));
        return tableGroupDao.save(tableGroup);
    }

    // TODO: 사용하지 않으면 삭제
    protected OrderLineItem saveOrderLineItem(final Order order, final Menu menu) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(1L);
        orderLineItem.setOrderId(order.getId());
        orderLineItem.setMenuId(menu.getId());
        return orderLineItemDao.save(orderLineItem);
    }
}
