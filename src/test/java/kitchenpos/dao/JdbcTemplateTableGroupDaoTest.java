package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DaoTest
class JdbcTemplateTableGroupDaoTest {

    private final JdbcTemplateTableGroupDao tableGroupDao;

    @Autowired
    public JdbcTemplateTableGroupDaoTest(final DataSource dataSource) {
        this.tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
    }

    @DisplayName("Insert 쿼리 테스트")
    @Nested
    class InsertTest {

        @Test
        void save() {
            final var tableGroup = newTableGroup();
            final var actual = tableGroupDao.save(tableGroup);

            assertThat(actual.getId()).isPositive();
            assertThat(actual.getCreatedDate()).isEqualTo(tableGroup.getCreatedDate());
        }
    }

    @DisplayName("Select 쿼리 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class SelectTest {

        private final Map<Long, TableGroup> savedTableGroups = saveAll(
                newTableGroup(),
                newTableGroup()
        );

        private Map<Long, TableGroup> saveAll(final TableGroup... tableGroups) {
            return Stream.of(tableGroups)
                    .map(tableGroupDao::save)
                    .collect(Collectors.toMap(TableGroup::getId, tableGroup -> tableGroup));
        }

        @Test
        void findAll() {
            final var actual = tableGroupDao.findAll();
            final var expected = DaoUtils.asList(savedTableGroups);

            DaoUtils.assertAllEquals(actual, expected, JdbcTemplateTableGroupDaoTest.this::assertEquals);
        }

        @Nested
        class findById {

            @ParameterizedTest(name = "success")
            @ValueSource(longs = 1L)
            void success(final long id) {
                assert savedTableGroups.containsKey(id);

                final var actual = tableGroupDao.findById(id);
                final var expected = savedTableGroups.get(id);

                assertThat(actual).hasValueSatisfying(menuGroups ->
                        assertEquals(menuGroups, expected)
                );
            }

            @ParameterizedTest(name = "fail")
            @ValueSource(longs = 10L)
            void fail(final long id) {
                assert !savedTableGroups.containsKey(id);

                final var actual = tableGroupDao.findById(id);
                assertThat(actual).isEmpty();
            }
        }
    }

    private void assertEquals(final TableGroup actual, final TableGroup expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getCreatedDate()).isEqualTo(expected.getCreatedDate());
    }

    private static TableGroup newTableGroup(final LocalDateTime createdDate, final OrderTable... orderTables) {
        final var tableGroup = new TableGroup();
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(List.of(orderTables));
        return tableGroup;
    }

    private static TableGroup newTableGroup(final OrderTable... orderTables) {
        return newTableGroup(LocalDateTime.now(), orderTables);
    }
}
