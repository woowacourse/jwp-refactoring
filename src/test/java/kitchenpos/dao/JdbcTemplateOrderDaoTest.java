package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.fixture.domain.OrderFixture;
import kitchenpos.support.fixture.domain.OrderTableFixture;
import kitchenpos.support.fixture.domain.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
class JdbcTemplateOrderDaoTest extends JdbcTemplateTest{

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("Order를 저장한다.")
        void success() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE.getOrderTable(tableGroup.getId()));
            Order order = OrderFixture.COMPLETION.getOrder(orderTable.getId());

            Order savedOrder = jdbcTemplateOrderDao.save(order);

            Long actual = savedOrder.getId();
            assertThat(actual).isNotNull();
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        private Order order;

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE.getOrderTable(tableGroup.getId()));
            order = jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable.getId()));
        }

        @Test
        @DisplayName("Order ID로 Order를 단일 조회한다.")
        void success() {
            Long id = order.getId();

            Order actual = jdbcTemplateOrderDao.findById(id)
                .orElseThrow();

            assertThat(actual).usingRecursiveComparison()
                .isEqualTo(order);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE.getOrderTable(tableGroup.getId()));
            jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable.getId()));
            jdbcTemplateOrderDao.save(OrderFixture.COOKING.getOrder(orderTable.getId()));
        }

        @Test
        @DisplayName("OrderTable 전체 목록을 조회한다.")
        void success() {
            List<Order> orders = jdbcTemplateOrderDao.findAll();

            assertThat(orders).hasSize(8);
        }
    }

    @Nested
    @DisplayName("existsByOrderTableIdAndOrderStatusIn 메서드는")
    class ExistsByOrderTableIdAndOrderStatusIndById {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            orderTable = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE.getOrderTable(tableGroup.getId()));
            jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable.getId()));
        }

        @Test
        @DisplayName("OrderTable ID와 orderStatus 목록을 받아 일치하는 Order가 있으면 true를 반환한다.")
        void success_true() {
            Long orderTableId = orderTable.getId();
            OrderStatus orderStatus = OrderStatus.COMPLETION;

            Boolean actual = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                List.of(orderStatus.name()));

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("OrderTable ID와 orderStatus 목록을 받아 일치하는 Order가 없으면 false를 반환한다.")
        void success_false() {
            Long orderTableId = orderTable.getId();
            OrderStatus orderStatus = OrderStatus.COOKING;

            Boolean actual = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                List.of(orderStatus.name()));

            assertThat(actual).isFalse();
        }
    }

    @Nested
    @DisplayName("existsByOrderTableIdInAndOrderStatusIn 메서드는")
    class ExistsByOrderTableIdInAndOrderStatusIn {

        private OrderTable orderTable1;
        private OrderTable orderTable2;

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            orderTable1 = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE.getOrderTable(tableGroup.getId()));
            orderTable2 = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE.getOrderTable(tableGroup.getId()));
            jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable1.getId()));
            jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable1.getId()));
        }

        @Test
        @DisplayName("OrderTable ID 목록과 orderStatus 목록을 받아 일치하는 Order가 있으면 true를 반환한다.")
        void success_true() {
            List<Long> orderTableIds = List.of(orderTable1.getId(), orderTable2.getId());
            OrderStatus orderStatus = OrderStatus.COMPLETION;

            Boolean actual = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                List.of(orderStatus.name()));

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("OrderTable ID와 orderStatus 목록을 받아 일치하는 Order가 없으면 false를 반환한다.")
        void success_false() {
            List<Long> orderTableIds = List.of(orderTable2.getId());
            OrderStatus orderStatus = OrderStatus.COMPLETION;

            Boolean actual = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                List.of(orderStatus.name()));

            assertThat(actual).isFalse();
        }
    }
}
