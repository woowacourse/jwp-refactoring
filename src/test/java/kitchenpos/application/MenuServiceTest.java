package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.menu.CreateMenuCommand;
import kitchenpos.application.dto.menu.CreateMenuResponse;
import kitchenpos.application.dto.menu.SearchMenuResponse;
import kitchenpos.application.dto.menuproduct.MenuProductCommand;
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

    @Test
    void 메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
        // given
        CreateMenuCommand command = new CreateMenuCommand(null, BigDecimal.ZERO, 1L, null);

        // when & then
        assertThatThrownBy(() -> menuService.create(command))
                .isInstanceOf(IllegalArgumentException.class);
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
        void 메뉴의_가격이_null이면_예외가_발생한다() {
            // given
            CreateMenuCommand command = new CreateMenuCommand(null, null, menuGroup.id(), null);

            // when & then
            assertThatThrownBy(() -> menuService.create(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_가격이_0보다_작으면_예외가_발생한다() {
            // given
            CreateMenuCommand command = new CreateMenuCommand(null, BigDecimal.valueOf(-1), menuGroup.id(), null);

            // when & then
            assertThatThrownBy(() -> menuService.create(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Nested
        class 메뉴_가격이_올바른_경우 {

            @Test
            void 메뉴_상품들이_null이면_예외가_발생한다() {
                // given
                CreateMenuCommand command = new CreateMenuCommand(null, BigDecimal.ZERO, menuGroup.id(), null);

                // when & then
                assertThatThrownBy(() -> menuService.create(command))
                        .isInstanceOf(NullPointerException.class);
            }

            @Test
            void 메뉴_이름이_없으면_DB에서_예외가_발생한다() {
                // given
                CreateMenuCommand command = new CreateMenuCommand(null, BigDecimal.ZERO, menuGroup.id(), List.of());

                // when & then
                assertThatThrownBy(() -> menuService.create(command))
                        .isInstanceOf(DataIntegrityViolationException.class);
            }

            @Nested
            class 메뉴_이름이_있는경우 {

                @Test
                void 메뉴_상품이_없어도_예외가_발생하지_않는다() {
                    // given
                    CreateMenuCommand command = new CreateMenuCommand("메뉴", BigDecimal.ZERO, menuGroup.id(),
                            List.of());

                    // when
                    assertThatCode(() -> menuService.create(command))
                            .doesNotThrowAnyException();
                }

                @Nested
                class 메뉴_상품들이_있는경우 {

                    private Product product1;
                    private Product product2;

                    @BeforeEach
                    void setUp() {
                        Product product1 = new Product("상품1", new Price(BigDecimal.valueOf(1)));
                        this.product1 = productRepository.save(product1);

                        Product product2 = new Product("상품2", new Price(BigDecimal.valueOf(3)));
                        this.product2 = productRepository.save(product2);
                    }

                    @Test
                    void 메뉴의_가격이_메뉴_상품_가격들의_합보다_크면_예외가_발생한다() {
                        // given
                        CreateMenuCommand command = new CreateMenuCommand("메뉴", BigDecimal.valueOf(12),
                                menuGroup.id(),
                                List.of(
                                        new MenuProductCommand(product1.id(), 2),
                                        new MenuProductCommand(product2.id(), 3)
                                ));

                        // when & then
                        assertThatThrownBy(() -> menuService.create(command))
                                .isInstanceOf(IllegalArgumentException.class);
                    }

                    @Test
                    void 메뉴를_저장한다() {
                        // given
                        CreateMenuCommand command = new CreateMenuCommand("메뉴", BigDecimal.valueOf(11),
                                menuGroup.id(),
                                List.of(
                                        new MenuProductCommand(product1.id(), 2),
                                        new MenuProductCommand(product2.id(), 3)
                                )
                        );

                        // when
                        CreateMenuResponse result = menuService.create(command);

                        // then
                        assertAll(
                                () -> assertThat(result.id()).isPositive(),
                                () -> assertThat(result.name()).isEqualTo("메뉴"),
                                () -> assertThat(result.price()).isEqualByComparingTo(BigDecimal.valueOf(11)),
                                () -> assertThat(result.menuGroupResponse().id()).isEqualByComparingTo(
                                        menuGroup.id()),
                                () -> assertThat(result.menuProductResponses()).hasSize(2),
                                () -> assertThat(result.menuProductResponses().get(0).menuId()).isEqualTo(
                                        result.id()),
                                () -> assertThat(result.menuProductResponses().get(1).menuId()).isEqualTo(
                                        result.id())
                        );
                    }

                    @Test
                    void 메뉴들을_조회한다() {
                        // given
                        Menu menu = new Menu("메뉴", new Price(BigDecimal.valueOf(11)), menuGroup);
                        MenuProduct menuProduct1 = new MenuProduct(product1, 2);
                        MenuProduct menuProduct2 = new MenuProduct(product2, 3);
                        menu.addMenuProduct(menuProduct1);
                        menu.addMenuProduct(menuProduct2);

                        Menu menu2 = new Menu("메뉴2", new Price(BigDecimal.valueOf(9)), menuGroup);
                        MenuProduct menuProduct3 = new MenuProduct(product1, 3);
                        MenuProduct menuProduct4 = new MenuProduct(product2, 2);
                        menu2.addMenuProduct(menuProduct3);
                        menu2.addMenuProduct(menuProduct4);

                        menuRepository.save(menu);
                        menuRepository.save(menu2);

                        // when
                        List<SearchMenuResponse> result = menuService.list();

                        // then
                        assertThat(result).hasSize(2);
                    }
                }
            }
        }
    }
}
