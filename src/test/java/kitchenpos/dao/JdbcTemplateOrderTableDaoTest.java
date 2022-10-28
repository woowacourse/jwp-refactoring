package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kitchenpos.dao.DaoUtils.asList;
import static org.assertj.core.api.Assertions.assertThat;

@DaoTest
class JdbcTemplateOrderTableDaoTest {

    private final JdbcTemplateOrderTableDao orderTableDao;

    @Autowired
    public JdbcTemplateOrderTableDaoTest(final DataSource dataSource) {
        this.orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
    }

    @DisplayName("Insert 쿼리 테스트")
    @Nested
    class InsertTest {

        @Test
        void save() {
            final var orderTable = new OrderTable(1L, 3, true);
            final var actual = orderTableDao.save(orderTable);

            assertThat(actual.getId()).isPositive();
            assertThat(actual.getTableGroupId()).isEqualTo(orderTable.getTableGroupId());
            assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
            assertThat(actual.isEmpty()).isEqualTo(orderTable.isEmpty());
        }
    }

    @DisplayName("Select 쿼리 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class SelectTest {

        private final Map<Long, OrderTable> savedOrderTables = saveAll(
                new OrderTable(1L, 2, true),
                new OrderTable(1L, 34, false),
                new OrderTable(2L, 0, true)
        );

        private Map<Long, OrderTable> saveAll(final OrderTable... orderTables) {
            return Stream.of(orderTables)
                    .map(orderTableDao::save)
                    .collect(Collectors.toMap(OrderTable::getId, orderTable -> orderTable));
        }

        @Test
        void findAll() {
            final var actual = orderTableDao.findAll();
            final var expected = asList(savedOrderTables);
            DaoUtils.assertAllEquals(actual, expected, JdbcTemplateOrderTableDaoTest.this::assertEquals);
        }

        @Test
        void findAllByIdIn() {
            final var orderTableIds = List.of(1L, 2L);

            final var actual = orderTableDao.findAllByIdIn(orderTableIds);
            final var expected = asListByInIn(savedOrderTables, orderTableIds);
            DaoUtils.assertAllEquals(actual, expected, JdbcTemplateOrderTableDaoTest.this::assertEquals);
        }

        private List<OrderTable> asListByInIn(final Map<Long, OrderTable> orderTables,
                                              final List<Long> orderTableIds) {
            return orderTables.entrySet()
                    .stream()
                    .filter(entry -> orderTableIds.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toUnmodifiableList());
        }

        @Nested
        class findAllByTableGroupId {

            @ParameterizedTest(name = "exist")
            @ValueSource(longs = 1L)
            void exist(final long tableGroupId) {
                assert savedOrderTables.containsKey(tableGroupId);

                final var actual = orderTableDao.findAllByTableGroupId(tableGroupId);
                final var expected = asListByTableGroupId(savedOrderTables, tableGroupId);
                DaoUtils.assertAllEquals(actual, expected, JdbcTemplateOrderTableDaoTest.this::assertEquals);
            }

            @ParameterizedTest(name = "empty")
            @ValueSource(longs = 20L)
            void empty(final long tableGroupId) {
                assert !savedOrderTables.containsKey(tableGroupId);

                final var actual = orderTableDao.findAllByTableGroupId(tableGroupId);
                assertThat(actual).isEmpty();
            }

            private List<OrderTable> asListByTableGroupId(final Map<Long, OrderTable> orderTables,
                                                          final Long tableGroupId) {
                return orderTables.values()
                        .stream()
                        .filter(orderTable -> orderTable.getTableGroupId().equals(tableGroupId))
                        .collect(Collectors.toUnmodifiableList());
            }
        }

        @Nested
        class findById {

            @ParameterizedTest(name = "success")
            @ValueSource(longs = 1L)
            void success(final long id) {
                assert savedOrderTables.containsKey(id);

                final var actual = orderTableDao.findById(id);
                final var expected = savedOrderTables.get(id);

                assertThat(actual).hasValueSatisfying(orderTable ->
                        assertEquals(orderTable, expected)
                );
            }

            @ParameterizedTest(name = "fail")
            @ValueSource(longs = 10L)
            void fail(final long id) {
                assert !savedOrderTables.containsKey(id);

                final var actual = orderTableDao.findById(id);
                assertThat(actual).isEmpty();
            }
        }
    }

    private void assertEquals(final OrderTable actual, final OrderTable expected) {
        assertThat(actual.getId()).isPositive();
        assertThat(actual.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
        assertThat(actual.getTableGroupId()).isEqualByComparingTo(expected.getTableGroupId());
        assertThat(actual.isEmpty()).isEqualTo(expected.isEmpty());
    }
}