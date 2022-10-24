package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.fixture.domain.OrderTableFixture;
import kitchenpos.support.fixture.domain.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
class JdbcTemplateOrderTableDaoTest extends JdbcTemplateTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("OrderTable를 저장한다.")
        void success() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            OrderTable orderTable = OrderTableFixture.GUEST_ONE.getOrderTable(tableGroup.getId());

            OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(orderTable);

            Long actual = savedOrderTable.getId();
            assertThat(actual).isNotNull();
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            orderTable = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE.getOrderTable(tableGroup.getId()));
        }

        @Test
        @DisplayName("OrderTable ID로 OrderTable를 단일 조회한다.")
        void success() {
            Long id = orderTable.getId();

            OrderTable actual = jdbcTemplateOrderTableDao.findById(id)
                .orElseThrow();

            assertThat(actual).usingRecursiveComparison()
                .isEqualTo(orderTable);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("OrderTable 전체 목록을 조회한다.")
        void success() {
            List<OrderTable> orderTables = jdbcTemplateOrderTableDao.findAll();

            assertThat(orderTables).hasSize(8);
        }
    }

    @Nested
    @DisplayName("FindAllByIdIn 메서드는")
    class FindAllByIdIn {

        private OrderTable orderTable1;
        private OrderTable orderTable2;

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            orderTable1 = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE.getOrderTable(tableGroup.getId()));
            orderTable2 = jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_TWO.getOrderTable(tableGroup.getId()));
        }

        @Test
        @DisplayName("OrderTable ID들을 받으면 해당 아이디의 OrderTable 목록을 조회한다.")
        void success() {
            List<OrderTable> orderTables = jdbcTemplateOrderTableDao.findAllByIdIn(
                List.of(orderTable1.getId(), orderTable2.getId()));

            assertThat(orderTables).hasSize(2);
        }
    }

    @Nested
    @DisplayName("findAllByTableGroupId 메서드는")
    class FindAllByTableGroupId {

        private TableGroup tableGroup;

        @BeforeEach
        void setUp() {
            tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_ONE.getOrderTable(tableGroup.getId()));
            jdbcTemplateOrderTableDao.save(OrderTableFixture.GUEST_TWO.getOrderTable(tableGroup.getId()));
        }

        @Test
        @DisplayName("TableGroupId ID를 받으면 포함된 OrderTable 목록을 조회한다.")
        void success() {
            List<OrderTable> orderTables = jdbcTemplateOrderTableDao.findAllByTableGroupId(tableGroup.getId());

            assertThat(orderTables).hasSize(2);
        }
    }
}
