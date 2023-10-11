package kitchenpos.dao;

import static kitchenpos.common.OrderTableFixtures.ORDER_TABLE1_REQUEST;
import static kitchenpos.common.OrderTableFixtures.ORDER_TABLE2_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class JdbcTemplateOrderTableDaoTest {

    @Autowired
    private DataSource dataSource;

    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        this.orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
    }

    @Test
    @DisplayName("OrderTable을 영속화한다.")
    void saveOrderTable() {
        // given
        final OrderTable orderTable = ORDER_TABLE1_REQUEST();

        // when
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedOrderTable.getId()).isNotNull();
            softly.assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
            softly.assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty());
        });
    }

    @Nested
    @DisplayName("모든 OrderTable 조회 시")
    class FindAll {

        @Test
        @DisplayName("여러 개의 값이 있을 경우 모두 반환한다.")
        void success() {
            // given
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
            jdbcTemplate.execute("truncate table order_table");
            jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

            OrderTable orderTable1 = ORDER_TABLE1_REQUEST();
            OrderTable orderTable2 = ORDER_TABLE2_REQUEST();
            OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
            OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
            List<OrderTable> expected = List.of(savedOrderTable1, savedOrderTable2);

            // when
            List<OrderTable> orderTables = orderTableDao.findAll();

            // then
            assertThat(orderTables).usingRecursiveFieldByFieldElementComparator()
                    .isEqualTo(expected);
        }
    }
}
