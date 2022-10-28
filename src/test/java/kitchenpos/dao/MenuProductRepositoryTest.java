package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("MenuProductRepository 테스트")
@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class MenuProductRepositoryTest {

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    private final Product product = new Product("치킨", BigDecimal.valueOf(1_000L));
    private final MenuGroup menuGroup = new MenuGroup("한마리치킨");
    private final Menu menu = new Menu("후라이드", BigDecimal.valueOf(0L), menuGroup, new ArrayList<>());

    @BeforeEach
    void setUp() {
        productRepository.save(product);
        menuRepository.save(menu);
    }

    @Nested
    class save_메서드는 {

        private final MenuProduct menuProduct = new MenuProduct(menu, product, 5);

        @Nested
        class 메뉴_상품이_주어지면 {

            @Test
            void 저장한다() {
                final MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);

                assertThat(savedMenuProduct.getSeq()).isNotNull();
            }
        }
    }

    @Nested
    class findById_메서드는 {

        private final MenuProduct menuProduct = new MenuProduct(menu, product, 5);
        private MenuProduct savedMenuProduct;

        @BeforeEach
        void setUp() {
            savedMenuProduct = menuProductRepository.save(menuProduct);
        }

        @Nested
        class id가_주어지면 {

            @Test
            void 해당하는_메뉴_상품을_반환한다() {
                final Optional<MenuProduct> foundMenuProduct = menuProductRepository.findById(
                        savedMenuProduct.getSeq());

                assertThat(foundMenuProduct).contains(savedMenuProduct);
            }
        }
    }

    @Nested
    class findAll_메서드는 {

        @Nested
        class 호출되면 {

            private final MenuProduct menuProduct = new MenuProduct(menu, product, 5);
            private MenuProduct savedMenuProduct;

            @BeforeEach
            void setUp() {
                savedMenuProduct = menuProductRepository.save(menuProduct);
            }

            @Test
            void 모든_메뉴_상품들을_반환한다() {
                final List<MenuProduct> menuProducts = menuProductRepository.findAll();

                assertThat(menuProducts).containsAll(List.of(savedMenuProduct));
            }
        }
    }

    @Nested
    class findAllByMenuId_메서드는 {

        @Nested
        class 메뉴_id가_주어지면 {

            private final MenuProduct menuProduct = new MenuProduct(menu, product, 5);
            private MenuProduct savedMenuProduct;

            @BeforeEach
            void setUp() {
                savedMenuProduct = menuProductRepository.save(menuProduct);
            }

            @Test
            void 해당하는_메뉴_상품을_반환한다() {

                final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(1L);

                assertThat(menuProducts).containsAll(List.of(savedMenuProduct));
            }
        }
    }
}
