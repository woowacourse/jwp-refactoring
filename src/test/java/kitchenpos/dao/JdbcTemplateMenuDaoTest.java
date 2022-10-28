package kitchenpos.dao;

import kitchenpos.domain.Menu;
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

import static org.assertj.core.api.Assertions.assertThat;

@DaoTest
class JdbcTemplateMenuDaoTest {

    private final JdbcTemplateMenuDao menuDao;

    @Autowired
    public JdbcTemplateMenuDaoTest(final DataSource dataSource) {
        this.menuDao = new JdbcTemplateMenuDao(dataSource);
    }

    @DisplayName("Insert 쿼리 테스트")
    @Nested
    class InsertTest {

        @Test
        void save() {
            final var menu = new Menu("세트", 10000, 1L);
            final var actual = menuDao.save(menu);

            assertThat(actual.getId()).isPositive();
            assertThat(actual.getName()).isEqualTo(menu.getName());
            assertThat(actual.getPrice()).isEqualByComparingTo(menu.getPrice());
            assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        }
    }

    @DisplayName("Select 쿼리 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class SelectTest {

        final Map<Long, Menu> savedMenus = saveAll(
                new Menu("세트1", 10000, 1L),
                new Menu("세트2", 18000, 2L),
                new Menu("세트3", 20000, 3L)
        );

        private Map<Long, Menu> saveAll(final Menu... menus) {
            return Stream.of(menus)
                    .map(menuDao::save)
                    .collect(Collectors.toMap(Menu::getId, menu -> menu));
        }

        @Test
        void findAll() {
            final var actual = menuDao.findAll();
            final var expected = DaoUtils.asList(savedMenus);
            DaoUtils.assertAllEquals(actual, expected, JdbcTemplateMenuDaoTest.this::assertEquals);
        }

        @Nested
        class findById {

            @ParameterizedTest(name = "success")
            @ValueSource(longs = 1L)
            void success(final long id) {
                assert savedMenus.containsKey(id);

                final var actual = menuDao.findById(id);
                final var expected = savedMenus.get(id);

                assertThat(actual).hasValueSatisfying(menuProduct ->
                        assertEquals(menuProduct, expected)
                );
            }

            @ParameterizedTest(name = "fail")
            @ValueSource(longs = 10L)
            void fail(final long id) {
                assert !savedMenus.containsKey(id);

                final var actual = menuDao.findById(id);
                assertThat(actual).isEmpty();
            }
        }

        @Test
        void countByIdIn() {
            final var menuIds = List.of(1L, 2L);

            final var actual = menuDao.countByIdIn(menuIds);
            final var expected = countByIdIn(savedMenus, menuIds);
            assertThat(actual).isEqualTo(expected);
        }

        private long countByIdIn(final Map<Long, Menu> menus, final List<Long> menuIds) {
            return menuIds.stream()
                    .filter(menus::containsKey)
                    .distinct()
                    .count();
        }
    }

    private void assertEquals(final Menu actual, final Menu expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getPrice()).isEqualByComparingTo(expected.getPrice());
        assertThat(actual.getMenuGroupId()).isEqualTo(expected.getMenuGroupId());
    }
}
