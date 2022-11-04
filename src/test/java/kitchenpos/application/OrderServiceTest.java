package kitchenpos.application;

import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.NestedApplicationTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.infrastructure.JdbcTemplateMenuDao;
import kitchenpos.menu.infrastructure.JdbcTemplateMenuGroupDao;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.infrastructure.JdbcTemplateOrderDao;
import kitchenpos.support.fixture.domain.MenuFixture;
import kitchenpos.support.fixture.domain.MenuGroupFixture;
import kitchenpos.support.fixture.domain.OrderFixture;
import kitchenpos.support.fixture.domain.OrderLineItemFixture;
import kitchenpos.support.fixture.domain.OrderTableFixture;
import kitchenpos.support.fixture.domain.TableGroupFixture;
import kitchenpos.support.fixture.dto.OrderDtoFixture;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.infrastructure.JdbcTemplateOrderTableDao;
import kitchenpos.table.infrastructure.JdbcTemplateTableGroupDao;
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
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(
                OrderTableFixture.GUEST_ONE_EMPTY_FALSE.getOrderTable(tableGroup.getId()));
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroupFixture.KOREAN.getMenuGroup());
            Menu menu = jdbcTemplateMenuDao.save(MenuFixture.CHICKEN_1000.getMenu(menuGroup.getId()));
            Order order = OrderFixture.COMPLETION.getOrder(orderTable.getId());
            List<OrderLineItem> orderLineItems = List.of(OrderLineItemFixture.ONE.getOrderLineItem(menu.getId(), order.getId()));
            OrderResponse actual = orderService.create(OrderDtoFixture.주문_생성_요청(order, orderLineItems));

            assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id", "orderedTime", "orderLineItems.id", "orderLineItems.orderId", "orderLineItems.menuName")
                .isEqualTo(OrderDtoFixture.주문_생성_응답(order, orderLineItems));
        }
    }

    @NestedApplicationTest
    @DisplayName("list 메서드는")
    class ListTest {

        @BeforeEach
        void setUp() {
            TableGroup tableGroup1 = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable1 = jdbcTemplateOrderTableDao.save(
                OrderTableFixture.GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup1.getId()));
            jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable1.getId()));

            TableGroup tableGroup2 = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable2 = jdbcTemplateOrderTableDao.save(
                OrderTableFixture.GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup2.getId()));
            jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable2.getId()));
        }

        @Test
        @DisplayName("주문 전체 목록을 조회한다.")
        void success() {
            List<OrderResponse> responses = orderService.list();

            assertThat(responses).hasSize(2);
        }
    }

    @NestedApplicationTest
    @DisplayName("changeOrderStatus 메서드는")
    class ChangeOrderStatus {

        private Order order;

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(
                OrderTableFixture.GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
            order = jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable.getId()));
        }

        @Test
        @DisplayName("주문상태를 변경한다.")
        void success() {
            OrderResponse response = orderService.changeOrderStatus(order.getId(), MEAL.name());

            assertThat(response.getOrderStatus()).isEqualTo(MEAL.name());
        }
    }
}
