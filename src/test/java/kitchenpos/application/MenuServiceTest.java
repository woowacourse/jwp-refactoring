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
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

class MenuServiceTest extends IntegrationTest {

    @Nested
    class 메뉴_가격이_올바르지_않은경우 {

        @Test
        void 메뉴의_가격이_null이면_예외가_발생한다() {
            // given
            Menu menu = new Menu(null, null, null, null);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_가격이_0보다_작으면_예외가_발생한다() {
            // given
            Menu menu = new Menu(null, BigDecimal.valueOf(-1), null, null);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 메뉴_가격이_올바른_경우 {

        @Nested
        class 메뉴_그룹이_없는경우 {

            @Test
            void 메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
                // given
                Menu menu = new Menu(null, BigDecimal.ZERO, new MenuGroup(1L, "추천메뉴"), null);

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
                MenuGroup menuGroup = new MenuGroup("추천메뉴");
                this.menuGroup = menuGroupRepository.save(menuGroup);
            }

            @Test
            void 메뉴_상품들이_null이면_예외가_발생한다() {
                // given
                Menu menu = new Menu(null, BigDecimal.ZERO, menuGroup, null);

                // when & then
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(NullPointerException.class);
            }

            @Test
            void 메뉴_이름이_없으면_DB에서_예외가_발생한다() {
                Menu menu = new Menu(null, BigDecimal.ZERO, menuGroup, List.of());

                // when & then
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(DataIntegrityViolationException.class);
            }

            @Nested
            class 메뉴_이름이_있는경우 {

                @Test
                void 메뉴_상품이_없어도_예외가_발생하지_않는다() {
                    // given
                    Menu menu = new Menu("메뉴", BigDecimal.ZERO, menuGroup, List.of());

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
                        Product product1 = new Product("상품1", new Price(BigDecimal.valueOf(1)));
                        this.product1 = productDao.save(product1);

                        Product product2 = new Product("상품2", new Price(BigDecimal.valueOf(3)));
                        this.product2 = productDao.save(product2);

                        menuProduct1 = new MenuProduct(this.product1, 2);
                        menuProduct2 = new MenuProduct(this.product2, 3);
                    }

                    @Test
                    void 메뉴의_가격이_메뉴_상품_가격들의_합보다_크면_예외가_발생한다() {
                        // given
                        Menu menu = new Menu("메뉴", BigDecimal.valueOf(12), menuGroup, List.of(
                                menuProduct1, menuProduct2
                        ));

                        // when & then
                        assertThatThrownBy(() -> menuService.create(menu))
                                .isInstanceOf(IllegalArgumentException.class);
                    }

                    @Test
                    void 메뉴를_저장한다() {
                        // given
                        Menu menu = new Menu("메뉴", BigDecimal.valueOf(11), menuGroup);
                        menu.addMenuProduct(menuProduct1);
                        menu.addMenuProduct(menuProduct2);

                        // when
                        Menu result = menuService.create(menu);

                        // then
                        assertAll(
                                () -> assertThat(result.id()).isPositive(),
                                () -> assertThat(result.name()).isEqualTo("메뉴"),
                                () -> assertThat(result.price()).isEqualByComparingTo(BigDecimal.valueOf(11)),
                                () -> assertThat(result.menuGroup().id()).isEqualByComparingTo(menuGroup.id()),
                                () -> assertThat(result.menuProducts()).hasSize(2),
                                () -> assertThat(result.menuProducts().get(0).menu().id()).isEqualTo(result.id()),
                                () -> assertThat(result.menuProducts().get(1).menu().id()).isEqualTo(result.id())
                        );
                    }

                    @Test
                    void 메뉴들을_조회한다() {
                        // given
                        Menu menu = new Menu("메뉴", BigDecimal.valueOf(11), menuGroup);
                        menu.addMenuProduct(menuProduct1);
                        menu.addMenuProduct(menuProduct2);

                        MenuProduct menuProduct1 = new MenuProduct(product1, 3);
                        MenuProduct menuProduct2 = new MenuProduct(product2, 2);

                        Menu menu2 = new Menu("메뉴2", BigDecimal.valueOf(9), menuGroup);
                        menu2.addMenuProduct(menuProduct1);
                        menu2.addMenuProduct(menuProduct2);

                        menuRepository.save(menu);
                        menuRepository.save(menu2);

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
