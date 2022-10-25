package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends ServiceTest {

    @Nested
    class 메뉴_등록_메소드는 {

        @Test
        void 입력받은_메뉴를_저장한다() {
            // given
            MenuProduct menuProduct1 = 메뉴_상품을_생성한다("상품 1", 1000, 1L);
            MenuProduct menuProduct2 = 메뉴_상품을_생성한다("상품 2", 2000, 1L);
            MenuGroup menuGroup = 메뉴_그룹을_저장한다("메뉴 그룹");

            Menu newMenu = new Menu();
            newMenu.setName("메뉴");
            newMenu.setPrice(BigDecimal.valueOf(3000));
            newMenu.setMenuGroupId(menuGroup.getId());
            newMenu.setMenuProducts(List.of(menuProduct1, menuProduct2));

            // when
            Menu savedMenu = menuService.create(newMenu);

            // then
            List<Menu> menus = menuService.list();
            assertThat(menus)
                    .extracting(Menu::getId, Menu::getName, (menu) -> menu.getPrice().intValue(), Menu::getMenuGroupId)
                    .contains(tuple(savedMenu.getId(), "메뉴", 3000, menuGroup.getId()));
        }

        @Test
        void 메뉴_가격이_음수면_예외가_발생한다() {
            // given
            MenuProduct menuProduct1 = 메뉴_상품을_생성한다("상품 1", 1000, 1L);
            MenuProduct menuProduct2 = 메뉴_상품을_생성한다("상품 2", 2000, 1L);
            MenuGroup menuGroup = 메뉴_그룹을_저장한다("메뉴 그룹");

            Menu newMenu = new Menu();
            newMenu.setName("메뉴");
            newMenu.setPrice(BigDecimal.valueOf(-1));
            newMenu.setMenuGroupId(menuGroup.getId());
            newMenu.setMenuProducts(List.of(menuProduct1, menuProduct2));

            // when & then
            assertThatThrownBy(() -> menuService.create(newMenu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_등록_메소드는_존재하지_않는_메뉴_그룹_id로_요청하면_예외가_발생한다() {
            // given
            MenuProduct menuProduct1 = 메뉴_상품을_생성한다("상품 1", 1000, 1L);
            MenuProduct menuProduct2 = 메뉴_상품을_생성한다("상품 2", 2000, 1L);

            Menu newMenu = new Menu();
            newMenu.setName("메뉴");
            newMenu.setPrice(BigDecimal.valueOf(3000));
            newMenu.setMenuGroupId(1000L);
            newMenu.setMenuProducts(List.of(menuProduct1, menuProduct2));

            // when & then
            assertThatThrownBy(() -> menuService.create(newMenu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_상품을_포함하여_요청하면_예외가_발생한다() {
            // given
            MenuProduct menuProduct1 = 메뉴_상품을_생성한다("상품 1", 1000, 1L);
            MenuProduct menuProduct2 = new MenuProduct();
            menuProduct2.setProductId(1000L);
            menuProduct2.setQuantity(1L);

            MenuGroup menuGroup = 메뉴_그룹을_저장한다("메뉴 그룹");

            Menu newMenu = new Menu();
            newMenu.setName("메뉴");
            newMenu.setPrice(BigDecimal.valueOf(3000));
            newMenu.setMenuGroupId(menuGroup.getId());
            newMenu.setMenuProducts(List.of(menuProduct1, menuProduct2));

            // when & then
            assertThatThrownBy(() -> menuService.create(newMenu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품들의_가격의_합이_메뉴_가격과_맞지_않으면_예외가_발생한다() {
            // given
            MenuProduct menuProduct1 = 메뉴_상품을_생성한다("상품 1", 1000, 1L);
            MenuProduct menuProduct2 = 메뉴_상품을_생성한다("상품 2", 2000, 1L);
            MenuGroup menuGroup = 메뉴_그룹을_저장한다("메뉴 그룹");

            Menu newMenu = new Menu();
            newMenu.setName("메뉴");
            newMenu.setPrice(BigDecimal.valueOf(10000));
            newMenu.setMenuGroupId(menuGroup.getId());
            newMenu.setMenuProducts(List.of(menuProduct1, menuProduct2));

            // when & then
            assertThatThrownBy(() -> menuService.create(newMenu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 메뉴_목록_조회_메소드는_모든_메뉴를_조회한다() {
        // given
        Menu menu1 = 메뉴를_저장한다("메뉴 1");
        Menu menu2 = 메뉴를_저장한다("메뉴 2");

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus)
                .extracting(Menu::getId, Menu::getName, (menu) -> menu.getPrice().intValue(), Menu::getMenuGroupId)
                .contains(tuple(menu1.getId(), "메뉴 1", 3000, 1L),
                        tuple(menu2.getId(), "메뉴 2", 3000, 2L));
    }
}
