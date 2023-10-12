package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

class MenuServiceTest extends IntegrationTest {

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = new Menu();
    }

    @Nested
    class 메뉴_가격이_올바르지_않은경우 {

        @Test
        void 메뉴의_가격이_null이면_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_가격이_0보다_작으면_예외가_발생한다() {
            // given
            menu.setPrice(BigDecimal.valueOf(-1));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 메뉴_가격이_올바른_경우 {

        @BeforeEach
        void setUp() {
            menu.setPrice(BigDecimal.ZERO);
        }

        @Nested
        class 메뉴_그룹이_없는경우 {

            @Test
            void 메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
                // given
                menu.setMenuGroupId(1L);

                // when & then
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 메뉴_그룹이_있는경우 {

            private MenuGroup menuGroup;

            @BeforeEach
            void setUp() {
                MenuGroup menuGroup = new MenuGroup();
                menuGroup.setName("추천메뉴");
                this.menuGroup = menuGroupDao.save(menuGroup);
                menu.setMenuGroupId(this.menuGroup.getId());
            }

            @Test
            void 메뉴_상품들이_null이면_예외가_발생한다() {
                // when & then
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(NullPointerException.class);
            }

            @Test
            void 메뉴_이름이_없으면_DB에서_예외가_발생한다() {
                menu.setMenuProducts(List.of());

                // when & then
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(DataIntegrityViolationException.class);
            }

            @Nested
            class 메뉴_이름이_있는경우 {

                @BeforeEach
                void setUp() {
                    menu.setName("메뉴");
                }

                @Test
                void 메뉴_상품이_없어도_예외가_발생하지_않는다() {
                    // given
                    menu.setMenuProducts(List.of());

                    // when
                    assertThatCode(() -> menuService.create(menu))
                            .doesNotThrowAnyException();
                }

                @Nested
                class 메뉴_상품들이_있는경우 {

                    private Product product1;
                    private Product product2;
                    private MenuProduct menuProduct1;
                    private MenuProduct menuProduct2;

                    @BeforeEach
                    void setUp() {
                        Product product1 = new Product();
                        product1.setName("상품1");
                        product1.setPrice(BigDecimal.valueOf(1));
                        this.product1 = productDao.save(product1);

                        Product product2 = new Product();
                        product2.setName("상품2");
                        product2.setPrice(BigDecimal.valueOf(3));
                        this.product2 = productDao.save(product2);

                        menuProduct1 = new MenuProduct();
                        menuProduct1.setProductId(this.product1.getId());
                        menuProduct1.setQuantity(2);

                        menuProduct2 = new MenuProduct();
                        menuProduct2.setProductId(this.product2.getId());
                        menuProduct2.setQuantity(3);
                    }

                    @Test
                    void 메뉴의_가격이_메뉴_상품_가격들의_합보다_크면_예외가_발생한다() {
                        // given
                        menu.setPrice(BigDecimal.valueOf(12));
                        menu.setMenuProducts(List.of(menuProduct1, menuProduct2));

                        // when & then
                        assertThatThrownBy(() -> menuService.create(menu))
                                .isInstanceOf(IllegalArgumentException.class);
                    }

                    @Test
                    void 메뉴를_저장한다() {
                        // given
                        menu.setPrice(BigDecimal.valueOf(11));
                        menu.setMenuProducts(List.of(menuProduct1, menuProduct2));

                        // when
                        Menu result = menuService.create(menu);

                        // then
                        assertAll(
                                () -> assertThat(result.getId()).isPositive(),
                                () -> assertThat(result.getName()).isEqualTo("메뉴"),
                                () -> assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(11)),
                                () -> assertThat(result.getMenuGroupId()).isEqualByComparingTo(menuGroup.getId()),
                                () -> assertThat(result.getMenuProducts()).hasSize(2),
                                () -> assertThat(result.getMenuProducts().get(0).getMenuId()).isEqualTo(result.getId()),
                                () -> assertThat(result.getMenuProducts().get(1).getMenuId()).isEqualTo(result.getId())
                        );
                    }

                    @Test
                    void 메뉴들을_조회한다() {
                        // given
                        menu.setPrice(BigDecimal.valueOf(11));
                        menu.setMenuProducts(List.of(menuProduct1, menuProduct2));

                        MenuProduct menuProduct1 = new MenuProduct();
                        menuProduct1.setProductId(product1.getId());
                        menuProduct1.setQuantity(3);

                        MenuProduct menuProduct2 = new MenuProduct();
                        menuProduct2.setProductId(product2.getId());
                        menuProduct1.setQuantity(2);

                        Menu menu2 = new Menu();
                        menu2.setPrice(BigDecimal.valueOf(9));
                        menu2.setMenuGroupId(menuGroup.getId());
                        menu2.setName("메뉴2");
                        menu2.setMenuProducts(List.of(menuProduct1, menuProduct2));

                        menuDao.save(menu);
                        menuDao.save(menu2);

                        // when
                        List<Menu> result = menuService.list();

                        // then
                        assertThat(result).hasSize(2);
                    }
                }
            }
        }
    }
}
