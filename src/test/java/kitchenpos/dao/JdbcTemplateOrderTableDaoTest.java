package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class JdbcTemplateOrderTableDaoTest {

    private final OrderTableDao orderTableDao;

    @Autowired
    JdbcTemplateOrderTableDaoTest(final DataSource dataSource) {
        this.orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
    }

    @Test
    void 저장한다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);
        orderTable.setEmpty(true);

        // when
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        // then
        Assertions.assertAll(
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable).usingRecursiveComparison()
                        .ignoringFields("id", "tableGroupId")
                        .isEqualTo(new OrderTable(null, null, 3, true))
        );
    }

    @Test
    void 이미_ID가_있으면_저장시_update를_진행한다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);
        orderTable.setEmpty(true);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        savedOrderTable.setNumberOfGuests(5);
        savedOrderTable.setEmpty(false);

        // when
        OrderTable updatedOrderTable = orderTableDao.save(savedOrderTable);

        // then
        Assertions.assertAll(
                () -> assertThat(updatedOrderTable.getId()).isEqualTo(savedOrderTable.getId()),
                () -> assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(5),
                () -> assertThat(updatedOrderTable.isEmpty()).isEqualTo(false)
        );
    }

    @Test
    void 목록을_조회한다() {
        // given & when
        List<OrderTable> orderTables = orderTableDao.findAll();

        // then
        assertThat(orderTables).hasSize(8)
                .usingRecursiveComparison()
                .ignoringFields("tableGroupId")
                .isEqualTo(
                        Arrays.asList(
                                new OrderTable(1L, 1L, 0, true),
                                new OrderTable(2L, 1L, 0, true),
                                new OrderTable(3L, 1L, 0, true),
                                new OrderTable(4L, 1L, 0, true),
                                new OrderTable(5L, 1L, 0, true),
                                new OrderTable(6L, 1L, 0, true),
                                new OrderTable(7L, 1L, 0, true),
                                new OrderTable(8L, 1L, 0, true)
                        )
                );
    }

    @Test
    void 포함된_ID를_조회할_수_있다() {
        // given
        List<Long> ids = Arrays.asList(1L, 8L, 9L);

        // when
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(ids);

        // then
        assertThat(orderTables).hasSize(2)
                .usingRecursiveComparison()
                .ignoringFields("tableGroupId")
                .isEqualTo(
                        Arrays.asList(
                                new OrderTable(1L, 1L, 0, true),
                                new OrderTable(8L, 1L, 0, true)
                        )
                );
    }

    @Test
    void table_group_id로_조회할_수_있다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);
        orderTable.setEmpty(true);
        orderTable.setTableGroupId(1L);
        orderTableDao.save(orderTable);

        // when
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(1L);

        // then
        assertThat(orderTables).hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(Arrays.asList(new OrderTable(null, 1L, 3, true)));
    }
}
