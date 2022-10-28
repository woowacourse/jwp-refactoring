package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
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

import static org.assertj.core.api.Assertions.assertThat;

@DaoTest
class JdbcTemplateMenuGroupDaoTest {

    private final JdbcTemplateMenuGroupDao menuGroupDao;

    @Autowired
    public JdbcTemplateMenuGroupDaoTest(final DataSource dataSource) {
        this.menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @DisplayName("Insert 쿼리 테스트")
    @Nested
    class InsertTest {

        @Test
        void save() {
            final var menuGroup = new MenuGroup("양식");
            final var actual = menuGroupDao.save(menuGroup);

            assertThat(actual.getId()).isPositive();
            assertThat(actual.getName()).isEqualTo(menuGroup.getName());
        }
    }

    @DisplayName("Select 쿼리 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class SelectTest {

        private final List<String> MENU_NAMES = List.of("한식", "양식", "중식", "일식");
        private final Map<Long, MenuGroup> savedMenuGroups = saveMenuGroups(MENU_NAMES);

        private Map<Long, MenuGroup> saveMenuGroups(final List<String> names) {
            return names.stream()
                    .map(MenuGroup::new)
                    .map(menuGroupDao::save)
                    .collect(Collectors.toMap(MenuGroup::getId, menuGroup -> menuGroup));
        }

        @Test
        void findAll() {
            final var actual = menuGroupDao.findAll();
            final var expected = DaoUtils.asList(savedMenuGroups);

            DaoUtils.assertAllEquals(actual, expected, JdbcTemplateMenuGroupDaoTest.this::assertEquals);
        }

        @Nested
        class findById {

            @ParameterizedTest(name = "success")
            @ValueSource(longs = 1L)
            void success(final long id) {
                assert savedMenuGroups.containsKey(id);

                final var actual = menuGroupDao.findById(id);
                final var expected = savedMenuGroups.get(id);

                assertThat(actual).hasValueSatisfying(menuGroups ->
                        assertEquals(menuGroups, expected)
                );
            }

            @ParameterizedTest(name = "fail")
            @ValueSource(longs = 10L)
            void fail(final long id) {
                assert !savedMenuGroups.containsKey(id);

                final var actual = menuGroupDao.findById(id);
                assertThat(actual).isEmpty();
            }
        }

        @Nested
        class existsById {

            @ParameterizedTest(name = "success")
            @ValueSource(longs = 1L)
            void success(final long id) {
                assert savedMenuGroups.containsKey(id);

                final var actual = menuGroupDao.existsById(id);
                assertThat(actual).isTrue();
            }

            @ParameterizedTest(name = "fail")
            @ValueSource(longs = 10L)
            void fail(final long id) {
                assert !savedMenuGroups.containsKey(id);

                final var actual = menuGroupDao.existsById(id);
                assertThat(actual).isFalse();
            }
        }
    }

    private void assertEquals(final MenuGroup actual, final MenuGroup expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }
}
