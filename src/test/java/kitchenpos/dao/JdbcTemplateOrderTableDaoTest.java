package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;

@DaoTest
class JdbcTemplateOrderTableDaoTest {

    private final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;
    private final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    public JdbcTemplateOrderTableDaoTest(
            final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao,
            final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao
    ) {
        this.jdbcTemplateOrderTableDao = jdbcTemplateOrderTableDao;
        this.jdbcTemplateTableGroupDao = jdbcTemplateTableGroupDao;
    }

    @Test
    void save_order_table() {
        // given
        final OrderTable orderTable = orderTableFixture(3);

        // when
        final OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(orderTable);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @Test
    void update_when_save_order_table_id_exist() {
        // given
        final OrderTable orderTable = orderTableFixture(3);
        final OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(orderTable);
        savedOrderTable.setNumberOfGuests(5);

        // when
        final OrderTable updatedOrderTable = jdbcTemplateOrderTableDao.save(savedOrderTable);

        // then
        assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void find_by_id() {
        // given
        final OrderTable orderTable = orderTableFixture(3);
        final OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(orderTable);
        final Long savedOrderTableId = savedOrderTable.getId();

        // when
        final Optional<OrderTable> menuGroupDaoById = jdbcTemplateOrderTableDao.findById(savedOrderTableId);

        // then
        assertThat(menuGroupDaoById).isPresent();
        assertThat(menuGroupDaoById.get().getId()).isEqualTo(savedOrderTableId);
    }

    @Test
    void find_by_id_return_empty_when_result_doesnt_exist() {
        // given
        final long doesntExistId = 10000L;

        // when
        final Optional<OrderTable> menuGroupDaoById = jdbcTemplateOrderTableDao.findById(doesntExistId);

        // then
        assertThat(menuGroupDaoById).isEmpty();
    }

    @Test
    void find_all() {
        // given
        jdbcTemplateOrderTableDao.save(orderTableFixture(3));
        jdbcTemplateOrderTableDao.save(orderTableFixture(4));

        // when
        final List<OrderTable> findAll = jdbcTemplateOrderTableDao.findAll();

        // then
        assertThat(findAll).hasSize(2);
    }

    @Test
    void find_all_by_id_in() {
        // given
        final Long firstSavedOrderTableId = jdbcTemplateOrderTableDao.save(orderTableFixture(3)).getId();
        final Long secondSavedOrderTableId = jdbcTemplateOrderTableDao.save(orderTableFixture(4)).getId();

        // when
        final List<OrderTable> findAll = jdbcTemplateOrderTableDao.findAllByIdIn(List.of(firstSavedOrderTableId));

        // then
        assertThat(findAll).hasSize(1);
    }

    @Test
    void find_all_by_table_group_id_in() {
        // given
        final OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(orderTableFixture(3));
        jdbcTemplateOrderTableDao.save(orderTableFixture(4));

        // when
        final List<OrderTable> findAll = jdbcTemplateOrderTableDao.findAllByTableGroupId(
                savedOrderTable.getTableGroupId()
        );

        // then
        assertThat(findAll).hasSize(1);
    }

    private OrderTable orderTableFixture(final int numberOfGuests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(getTableGroupFixtureId());
        return orderTable;
    }

    private Long getTableGroupFixtureId() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return jdbcTemplateTableGroupDao.save(tableGroup).getId();
    }
}
