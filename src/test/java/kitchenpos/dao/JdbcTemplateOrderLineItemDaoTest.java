package kitchenpos.dao;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
class JdbcTemplateOrderLineItemDaoTest extends JdbcTemplateTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("OrderLineItem를 저장한다.")
        void success() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE.getOrderTable(tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable.getId()));
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroupFixture.KOREAN.getMenuGroup());
            Menu menu = jdbcTemplateMenuDao.save(MenuFixture.CHICKEN_1000.getMenu(menuGroup.getId()));
            OrderLineItem orderLineItem = OrderLineItemFixture.ONE.getOrderLineItem(order.getId(), menu.getId());

            OrderLineItem savedOrderLineItem = jdbcTemplateOrderLineItemDao.save(orderLineItem);

            Long actual = savedOrderLineItem.getSeq();
            assertThat(actual).isNotNull();
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        private OrderLineItem orderLineItem;

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE.getOrderTable(tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable.getId()));
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroupFixture.KOREAN.getMenuGroup());
            Menu menu = jdbcTemplateMenuDao.save(MenuFixture.CHICKEN_1000.getMenu(menuGroup.getId()));
            orderLineItem = jdbcTemplateOrderLineItemDao.save(OrderLineItemFixture.ONE.getOrderLineItem(order.getId(), menu.getId()));
        }

        @Test
        @DisplayName("OrderLineItem ID로 OrderLineItem를 단일 조회한다.")
        void success() {
            Long seq = orderLineItem.getSeq();

            OrderLineItem actual = jdbcTemplateOrderLineItemDao.findById(seq)
                .orElseThrow();

            assertThat(actual).usingRecursiveComparison()
                .isEqualTo(orderLineItem);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE.getOrderTable(tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable.getId()));
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroupFixture.KOREAN.getMenuGroup());
            Menu menu = jdbcTemplateMenuDao.save(MenuFixture.CHICKEN_1000.getMenu(menuGroup.getId()));
            jdbcTemplateOrderLineItemDao.save(OrderLineItemFixture.ONE.getOrderLineItem(order.getId(), menu.getId()));
            jdbcTemplateOrderLineItemDao.save(OrderLineItemFixture.ONE.getOrderLineItem(order.getId(), menu.getId()));
        }

        @Test
        @DisplayName("OrderLineItem 전체 목록을 조회한다.")
        void success() {
            List<OrderLineItem> orderLineItems = jdbcTemplateOrderLineItemDao.findAll();

            assertThat(orderLineItems).hasSize(2);
        }
    }

    @Nested
    @DisplayName("findAllByOrderId 메서드는")
    class FindAllByOrderId {

        private Order order;

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE.getOrderTable(tableGroup.getId()));
            order = jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable.getId()));
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroupFixture.KOREAN.getMenuGroup());
            Menu menu = jdbcTemplateMenuDao.save(MenuFixture.CHICKEN_1000.getMenu(menuGroup.getId()));
            jdbcTemplateOrderLineItemDao.save(OrderLineItemFixture.ONE.getOrderLineItem(order.getId(), menu.getId()));
            jdbcTemplateOrderLineItemDao.save(OrderLineItemFixture.ONE.getOrderLineItem(order.getId(), menu.getId()));
        }

        @Test
        @DisplayName("OrderId를 받으면 해당 아이디를 포함한 목록을 조회한다.")
        void success() {
            List<OrderLineItem> orderLineItems = jdbcTemplateOrderLineItemDao.findAllByOrderId(order.getId());

            assertThat(orderLineItems).hasSize(2);
        }
    }

}
