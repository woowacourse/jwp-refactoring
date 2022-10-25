package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.NestedApplicationTest;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.fixture.domain.MenuFixture;
import kitchenpos.support.fixture.domain.MenuGroupFixture;
import kitchenpos.support.fixture.domain.OrderFixture;
import kitchenpos.support.fixture.domain.OrderLineItemFixture;
import kitchenpos.support.fixture.domain.OrderTableFixture;
import kitchenpos.support.fixture.domain.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JdbcTemplateOrderDao jdbcTemplateOrderDao;

    @Autowired
    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @Autowired
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    @Autowired
    private JdbcTemplateMenuDao jdbcTemplateMenuDao;

    @Autowired
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @NestedApplicationTest
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("주문을 저장한다.")
        void success() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE_EMPTY_FALSE.getOrderTable(tableGroup.getId()));
            Order order = OrderFixture.COMPLETION.getOrder(orderTable.getId());
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroupFixture.KOREAN.getMenuGroup());
            Menu menu = jdbcTemplateMenuDao.save(MenuFixture.CHICKEN_1000.getMenu(menuGroup.getId()));
            order.setOrderLineItems(List.of(OrderLineItemFixture.ONE.getOrderLineItem(menu.getId())));

            Order actual = orderService.create(order);

            assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id", "orderLineItems")
                .isEqualTo(order);
        }
    }

    @NestedApplicationTest
    @DisplayName("list 메서드는")
    class ListTest {

        @BeforeEach
        void setUp() {
            TableGroup tableGroup1 = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable1 = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup1.getId()));
            jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable1.getId()));

            TableGroup tableGroup2 = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable2 = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup2.getId()));
            jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable2.getId()));
        }

        @Test
        @DisplayName("주문 전체 목록을 조회한다.")
        void success() {
            List<Order> orders = orderService.list();

            assertThat(orders).hasSize(2);
        }
    }

    @NestedApplicationTest
    @DisplayName("changeOrderStatus 메서드는")
    class ChangeOrderStatus {

        private Order order;

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE_EMPTY_FALSE.getOrderTable(tableGroup.getId()));
            Order order1 = OrderFixture.COMPLETION.getOrder(orderTable.getId());
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroupFixture.KOREAN.getMenuGroup());
            Menu menu = jdbcTemplateMenuDao.save(MenuFixture.CHICKEN_1000.getMenu(menuGroup.getId()));
            order1.setOrderLineItems(List.of(OrderLineItemFixture.ONE.getOrderLineItem(menu.getId())));
            order = orderService.create(order1);
        }

        @Test
        @DisplayName("주문상태를 변경한다.")
        void success() {
            Order actual = orderService.changeOrderStatus(order.getId(), OrderFixture.MEAL.getOrder());

            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }
    }
}
