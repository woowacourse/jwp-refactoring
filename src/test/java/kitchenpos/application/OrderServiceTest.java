package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.createMenuWithGroupId;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroupWithoutId;
import static kitchenpos.fixture.OrderFixture.createOrderWithOrderStatus;
import static kitchenpos.fixture.OrderFixture.createOrderWithoutId;
import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItemWithMenuId;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithTableGroupIdAndEmpty;
import static kitchenpos.fixture.TableGroupFixture.createTableGroupWithoutId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql(value = "/truncate.sql")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @DisplayName("Order 등록 성공")
    @Test
    void create() {
        OrderTable orderTable = saveOrderTableWithEmpty(false);
        Menu menu = saveMenu();
        OrderLineItem orderLineItem = createOrderLineItemWithMenuId(menu.getId());
        Order order = createOrderWithoutId(orderTable.getId(), orderLineItem);

        Order actual = orderService.create(order);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            assertThat(actual.getOrderLineItems())
                    .extracting(OrderLineItem::getOrderId)
                    .isEqualTo(Arrays.asList(actual.getId()));
            assertThat(actual.getOrderLineItems())
                    .extracting(OrderLineItem::getSeq)
                    .doesNotContainNull();
        });
    }

    @DisplayName("Order에 속하는 OrderLineItem이 아무것도 없는 경우 예외 반환")
    @Test
    void createEmptyOrderLineItem() {
        Order order = OrderFixture.createOrderEmptyOrderLineItem();

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order에 속하는 OrderLineItem 개수와 Menu 개수가 일치하지 않을 때 예외 반환")
    @Test
    void createNotMatchOrderLineItemCountAndMenuCount() {
        OrderLineItem orderLineItem = createOrderLineItemWithMenuId(1L);
        Order order = createOrderWithoutId(3L, orderLineItem);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order를 받은 OrderTable이 비어있는 경우 예외 반환")
    @Test
    void createEmptyOrderTable() {
        OrderTable orderTable = saveOrderTableWithEmpty(true);
        Menu menu = saveMenu();
        OrderLineItem orderLineItem = createOrderLineItemWithMenuId(menu.getId());
        Order order = createOrderWithoutId(orderTable.getId(), orderLineItem);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order 전체 조회")
    @Test
    void list() {
        OrderTable orderTable = saveOrderTableWithEmpty(false);
        Menu menu = saveMenu();
        OrderLineItem orderLineItem = createOrderLineItemWithMenuId(menu.getId());
        Order savedOrder = orderDao.save(OrderFixture.createOrderWithoutId(orderTable.getId(), OrderStatus.COOKING.name(), orderLineItem));
        orderLineItem.setOrderId(savedOrder.getId());
        orderLineItemDao.save(orderLineItem);

        List<Order> orders = orderService.list();

        assertThat(orders).hasSize(1);
    }

    @DisplayName("Order 상태 바꾸기 성공")
    @Test
    void changeOrderStatus() {
        OrderTable orderTable = saveOrderTableWithEmpty(false);
        Menu menu = saveMenu();
        OrderLineItem orderLineItem = createOrderLineItemWithMenuId(menu.getId());
        Order savedOrder = orderDao.save(OrderFixture.createOrderWithoutId(orderTable.getId(), OrderStatus.COOKING.name(), orderLineItem));

        Order expect = createOrderWithOrderStatus(OrderStatus.COMPLETION.name());
        Order actual = orderService.changeOrderStatus(savedOrder.getId(), expect);

        assertThat(actual.getOrderStatus()).isEqualTo(expect.getOrderStatus());
    }

    @DisplayName("Order 원래 상태가 COMPLETION 일 때 상태 바꾸기 예외 반환")
    @Test
    void changeWrongOrderStatus() {
        OrderTable orderTable = saveOrderTableWithEmpty(false);
        Menu menu = saveMenu();
        OrderLineItem orderLineItem = createOrderLineItemWithMenuId(menu.getId());
        Order savedOrder = orderDao.save(OrderFixture.createOrderWithoutId(orderTable.getId(), OrderStatus.COMPLETION.name(), orderLineItem));

        Order expect = createOrderWithOrderStatus(OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), expect))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable saveOrderTableWithEmpty(boolean empty) {
        TableGroup tableGroup = tableGroupDao.save(createTableGroupWithoutId());
        return orderTableDao.save(createOrderTableWithTableGroupIdAndEmpty(tableGroup.getId(), empty));
    }

    private Menu saveMenu() {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroupWithoutId());
        return menuDao.save(createMenuWithGroupId(menuGroup.getId()));
    }
}
