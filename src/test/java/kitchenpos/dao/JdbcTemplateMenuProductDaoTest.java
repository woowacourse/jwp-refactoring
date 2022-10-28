package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
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
class JdbcTemplateMenuProductDaoTest {

    private final JdbcTemplateMenuProductDao menuProductDao;

    @Autowired
    public JdbcTemplateMenuProductDaoTest(final DataSource dataSource) {
        this.menuProductDao = new JdbcTemplateMenuProductDao(dataSource);
    }

    @DisplayName("Insert 쿼리 테스트")
    @Nested
    class InsertTest {

        @Test
        void save() {
            final var menuProduct = new MenuProduct(1L, 2L, 30);
            final var actual = menuProductDao.save(menuProduct);

            assertThat(actual.getSeq()).isPositive();
            assertThat(actual.getMenuId()).isEqualTo(menuProduct.getMenuId());
            assertThat(actual.getProductId()).isEqualTo(menuProduct.getProductId());
            assertThat(actual.getQuantity()).isEqualTo(menuProduct.getQuantity());
        }
    }

    @DisplayName("Select 쿼리 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class SelectTest {

        private final Map<Long, MenuProduct> savedMenuProducts = saveAll(
                new MenuProduct(1L, 2L, 30),
                new MenuProduct(1L, 3L, 40),
                new MenuProduct(2L, 4L, 0)
        );

        private Map<Long, MenuProduct> saveAll(final MenuProduct... menuProducts) {
            return Stream.of(menuProducts)
                    .map(menuProductDao::save)
                    .collect(Collectors.toMap(MenuProduct::getSeq, menuProduct -> menuProduct));
        }

        @Test
        void findAll() {
            final var actual = menuProductDao.findAll();
            final var expected = asList(savedMenuProducts);
            DaoUtils.assertAllEquals(actual, expected, JdbcTemplateMenuProductDaoTest.this::assertEquals);
        }

        @Nested
        class findAllByMenuId {

            @ParameterizedTest(name = "result is not empty")
            @ValueSource(longs = 1L)
            void resultExist(final long menuId) {
                assert existByMenuId(savedMenuProducts, menuId);

                final var actual = menuProductDao.findAllByMenuId(menuId);
                final var expected = asListByMenuId(savedMenuProducts, menuId);
                DaoUtils.assertAllEquals(actual, expected, JdbcTemplateMenuProductDaoTest.this::assertEquals);
            }

            @ParameterizedTest(name = "result is empty")
            @ValueSource(longs = 10L)
            void resultEmpty(final long menuId) {
                assert !existByMenuId(savedMenuProducts, menuId);

                final var actual = menuProductDao.findAllByMenuId(menuId);
                assertThat(actual).isEmpty();
            }

            private List<MenuProduct> asListByMenuId(final Map<Long, MenuProduct> menuProducts, final long menuId) {
                return menuProducts.values()
                        .stream()
                        .filter(menuProduct -> menuProduct.getMenuId().equals(menuId))
                        .collect(Collectors.toUnmodifiableList());
            }

            private boolean existByMenuId(final Map<Long, MenuProduct> menuProducts, final long menuId) {
                return menuProducts.values()
                        .stream()
                        .anyMatch(menuProduct -> menuProduct.getMenuId().equals(menuId));
            }
        }

        @Nested
        class findById {

            @ParameterizedTest(name = "success")
            @ValueSource(longs = 1L)
            void success(final long id) {
                assert savedMenuProducts.containsKey(id);

                final var actual = menuProductDao.findById(id);
                final var expected = savedMenuProducts.get(id);

                assertThat(actual).hasValueSatisfying(menuProduct ->
                        assertEquals(menuProduct, expected)
                );
            }

            @ParameterizedTest(name = "fail")
            @ValueSource(longs = 10L)
            void fail(final long id) {
                assert !savedMenuProducts.containsKey(id);

                final var actual = menuProductDao.findById(id);
                assertThat(actual).isEmpty();
            }
        }
    }

    private void assertEquals(final MenuProduct actual, final MenuProduct expected) {
        assertThat(actual.getSeq()).isEqualTo(expected.getSeq());
        assertThat(actual.getMenuId()).isEqualTo(expected.getMenuId());
        assertThat(actual.getProductId()).isEqualTo(expected.getProductId());
        assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity());
    }
}
