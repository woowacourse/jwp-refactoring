package kitchenpos.dao;


import static kitchenpos.support.fixture.domain.MenuFixture.CHICKEN_1000;
import static kitchenpos.support.fixture.domain.MenuGroupFixture.KOREAN;
import static kitchenpos.support.fixture.domain.OrderFixture.COMPLETION;
import static kitchenpos.support.fixture.domain.OrderLineItemFixture.ONE;
import static kitchenpos.support.fixture.domain.OrderTableFixture.GUEST_ONE_EMPTY_TRUE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.fixture.domain.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JdbcTemplateOrderLineItemDaoTest extends JdbcTemplateTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("주문 라인 아이템을 저장한다.")
        void success() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(COMPLETION.getOrder(orderTable.getId()));
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            Menu menu = jdbcTemplateMenuDao.save(CHICKEN_1000.getMenu(menuGroup.getId()));
            OrderLineItem orderLineItem = ONE.getOrderLineItem(menu.getId(), order.getId());

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
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(COMPLETION.getOrder(orderTable.getId()));
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            Menu menu = jdbcTemplateMenuDao.save(CHICKEN_1000.getMenu(menuGroup.getId()));
            orderLineItem = jdbcTemplateOrderLineItemDao.save(ONE.getOrderLineItem(menu.getId(), order.getId()));
        }

        @Test
        @DisplayName("아이디로 주문 라인 아이템을 단일 조회한다.")
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
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
            Order order = jdbcTemplateOrderDao.save(COMPLETION.getOrder(orderTable.getId()));
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            Menu menu = jdbcTemplateMenuDao.save(CHICKEN_1000.getMenu(menuGroup.getId()));
            jdbcTemplateOrderLineItemDao.save(ONE.getOrderLineItem(menu.getId(), order.getId()));
            jdbcTemplateOrderLineItemDao.save(ONE.getOrderLineItem(menu.getId(), order.getId()));
        }

        @Test
        @DisplayName("주문 라인 아이템 전체 목록을 조회한다.")
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
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
            order = jdbcTemplateOrderDao.save(COMPLETION.getOrder(orderTable.getId()));
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            Menu menu = jdbcTemplateMenuDao.save(CHICKEN_1000.getMenu(menuGroup.getId()));
            jdbcTemplateOrderLineItemDao.save(ONE.getOrderLineItem(menu.getId(), order.getId()));
            jdbcTemplateOrderLineItemDao.save(ONE.getOrderLineItem(menu.getId(), order.getId()));
        }

        @Test
        @DisplayName("주문 아이디를 받으면 포함한 목록을 조회한다.")
        void success() {
            List<OrderLineItem> orderLineItems = jdbcTemplateOrderLineItemDao.findAllByOrderId(order.getId());

            assertThat(orderLineItems).hasSize(2);
        }
    }

}
