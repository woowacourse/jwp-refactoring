package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("MenuProduct 테스트")
@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class MenuProductDaoTest {

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    private final Product product = new Product("치킨", BigDecimal.valueOf(1_000L));
    private final MenuGroup menuGroup = new MenuGroup("한마리치킨");
    private final Menu menu = new Menu("후라이드", BigDecimal.valueOf(1_000L), 1L, null);

    @BeforeEach
    void setUp() {
        productDao.save(product);
        menuGroupDao.save(menuGroup);
        menuDao.save(menu);
    }

    @Nested
    class save_메서드는 {

        private final MenuProduct menuProduct = new MenuProduct(1L, 1L, 5);

        @Nested
        class 메뉴_상품이_주어지면 {

            @Test
            void 저장한다() {
                final MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

                assertThat(savedMenuProduct.getSeq()).isNotNull();
            }
        }
    }

    @Nested
    class findById_메서드는 {

        private final MenuProduct menuProduct = new MenuProduct(1L, 1L, 5);
        private MenuProduct savedMenuProduct;

        @BeforeEach
        void setUp() {
            savedMenuProduct = menuProductDao.save(menuProduct);
        }

        @Nested
        class id가_주어지면 {

            @Test
            void 해당하는_메뉴_상품을_반환한다() {
                final Optional<MenuProduct> foundMenuProduct = menuProductDao.findById(savedMenuProduct.getSeq());

                assertAll(
                        () -> assertThat(foundMenuProduct).isPresent(),
                        () -> assertThat(foundMenuProduct.get()).usingRecursiveComparison()
                                .isEqualTo(savedMenuProduct)
                );
            }
        }
    }

    @Nested
    class findAll_메서드는 {

        @Nested
        class 호출되면 {

            private final MenuProduct menuProduct = new MenuProduct(1L, 1L, 5);
            private MenuProduct savedMenuProduct;

            @BeforeEach
            void setUp() {
                savedMenuProduct = menuProductDao.save(menuProduct);
            }

            @Test
            void 모든_메뉴_상품들을_반환한다() {
                final List<MenuProduct> menuProducts = menuProductDao.findAll();

                assertThat(menuProducts).usingFieldByFieldElementComparator()
                        .containsAll(List.of(savedMenuProduct));
            }
        }
    }

    @Nested
    class findAllByMenuId_메서드는 {

        @Nested
        class 메뉴_id가_주어지면 {

            private final MenuProduct menuProduct = new MenuProduct(1L, 1L, 5);
            private MenuProduct savedMenuProduct;

            @BeforeEach
            void setUp() {
                savedMenuProduct = menuProductDao.save(menuProduct);
            }

            @Test
            void 해당하는_메뉴_상품을_반환한다() {

                final List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(1L);

                assertThat(menuProducts).usingFieldByFieldElementComparator()
                        .containsAll(List.of(savedMenuProduct));
            }
        }
    }
}
