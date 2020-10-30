package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
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

import static kitchenpos.fixture.MenuFixture.createMenuWithGroupId;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroupWithoutId;
import static kitchenpos.fixture.OrderFixture.createOrderWithOutId;
import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItemWithMenuId;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithTableGroupId;
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

    @DisplayName("Order 등록 성공")
    @Test
    void create() {
        TableGroup tableGroup = tableGroupDao.save(createTableGroupWithoutId());
        OrderTable orderTable = orderTableDao.save(createOrderTableWithTableGroupId(tableGroup.getId()));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroupWithoutId());
        Menu menu = menuDao.save(createMenuWithGroupId(menuGroup.getId()));
        OrderLineItem orderLineItem = createOrderLineItemWithMenuId(menu.getId());
        Order order = createOrderWithOutId(orderTable.getId(), orderLineItem);

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
        Order order = createOrderWithOutId(3L, orderLineItem);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order를 받은 OrderTable이 비어있는 경우 예외 반환")
    @Test
    void createEmptyOrderTable() {
        TableGroup tableGroup = tableGroupDao.save(createTableGroupWithoutId());
        OrderTable orderTable = orderTableDao.save(createOrderTableWithTableGroupIdAndEmpty(tableGroup.getId(), true));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroupWithoutId());
        Menu menu = menuDao.save(createMenuWithGroupId(menuGroup.getId()));
        OrderLineItem orderLineItem = createOrderLineItemWithMenuId(menu.getId());
        Order order = createOrderWithOutId(orderTable.getId(), orderLineItem);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }
}
