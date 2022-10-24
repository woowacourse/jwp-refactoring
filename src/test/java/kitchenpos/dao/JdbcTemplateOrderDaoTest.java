package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Order;
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

            assertThat(orders).hasSize(2);
        }
    }
}
