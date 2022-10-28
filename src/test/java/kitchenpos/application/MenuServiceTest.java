package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends ServiceTest {

    @Nested
    class 메뉴_등록_메소드는 extends ServiceTest {

        @Test
        void 입력받은_메뉴를_저장한다() {
            // given
            MenuProductCreateRequest menuProduct1 = new MenuProductCreateRequest(상품을_저장한다("상품 1", 1000).getId(), 1L);
            MenuProductCreateRequest menuProduct2 = new MenuProductCreateRequest(상품을_저장한다("상품 2", 2000).getId(), 1L);
            MenuGroup menuGroup = 메뉴_그룹을_저장한다("메뉴 그룹");

            MenuCreateRequest menu = new MenuCreateRequest(
                    "메뉴", BigDecimal.valueOf(3000), menuGroup.getId(), List.of(menuProduct1, menuProduct2));

            // when
            MenuResponse response = menuService.create(menu);

            // then
            assertAll(() -> {
                assertThat(response.getId()).isNotNull();
                assertThat(response)
                        .extracting(MenuResponse::getName, menuRes -> menuRes.getPrice().intValue(),
                                MenuResponse::getMenuGroupId)
                        .containsExactly(menu.getName(), menu.getPrice().intValue(), menu.getMenuGroupId());
            });
        }

        @Test
        void 메뉴_가격이_음수면_예외가_발생한다() {
            // given
            MenuProductCreateRequest menuProduct1 = new MenuProductCreateRequest(상품을_저장한다("상품 1", 1000).getId(), 1L);
            MenuProductCreateRequest menuProduct2 = new MenuProductCreateRequest(상품을_저장한다("상품 2", 2000).getId(), 1L);
            MenuGroup menuGroup = 메뉴_그룹을_저장한다("메뉴 그룹");

            MenuCreateRequest menu = new MenuCreateRequest(
                    "메뉴", BigDecimal.valueOf(-1), menuGroup.getId(), List.of(menuProduct1, menuProduct2));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_등록_메소드는_존재하지_않는_메뉴_그룹_id로_요청하면_예외가_발생한다() {
            // given
            MenuProductCreateRequest menuProduct1 = new MenuProductCreateRequest(상품을_저장한다("상품 1", 1000).getId(), 1L);
            MenuProductCreateRequest menuProduct2 = new MenuProductCreateRequest(상품을_저장한다("상품 2", 2000).getId(), 1L);

            MenuCreateRequest menu = new MenuCreateRequest(
                    "메뉴", BigDecimal.valueOf(3000), 0L, List.of(menuProduct1, menuProduct2));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_상품을_포함하여_요청하면_예외가_발생한다() {
            // given
            MenuProductCreateRequest menuProduct1 = new MenuProductCreateRequest(상품을_저장한다("상품 1", 1000).getId(), 1L);
            MenuProductCreateRequest menuProduct2 = new MenuProductCreateRequest(0L, 1L);
            MenuGroup menuGroup = 메뉴_그룹을_저장한다("메뉴 그룹");

            MenuCreateRequest menu = new MenuCreateRequest(
                    "메뉴", BigDecimal.valueOf(3000), menuGroup.getId(), List.of(menuProduct1, menuProduct2));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품들의_가격의_합이_메뉴_가격보다_크면_예외가_발생한다() {
            // given
            MenuProductCreateRequest menuProduct1 = new MenuProductCreateRequest(상품을_저장한다("상품 1", 1000).getId(), 1L);
            MenuProductCreateRequest menuProduct2 = new MenuProductCreateRequest(상품을_저장한다("상품 2", 2000).getId(), 3L);
            MenuGroup menuGroup = 메뉴_그룹을_저장한다("메뉴 그룹");

            MenuCreateRequest menu = new MenuCreateRequest(
                    "메뉴", BigDecimal.valueOf(8000), menuGroup.getId(), List.of(menuProduct1, menuProduct2));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 메뉴_목록_조회_메소드는_모든_메뉴를_조회한다() {
        // given
        Menu menu1 = 메뉴를_저장한다("메뉴 1");
        Menu menu2 = 메뉴를_저장한다("메뉴 2");

        // when
        List<MenuResponse> menus = menuService.list();

        // then
        assertThat(menus)
                .extracting(MenuResponse::getId, MenuResponse::getName, MenuResponse::getPrice,
                        MenuResponse::getMenuGroupId)
                .contains(tuple(menu1.getId(), menu1.getName(), menu1.getPrice(), menu1.getMenuGroupId()),
                        tuple(menu2.getId(), menu2.getName(), menu2.getPrice(), menu2.getMenuGroupId()));
    }
}
