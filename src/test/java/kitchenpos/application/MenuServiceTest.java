package kitchenpos.application;

import static kitchenpos.exception.MenuExceptionType.NEGATIVE_PRICE_EXCEPTION;
import static kitchenpos.exception.MenuExceptionType.NULL_PRICE_EXCEPTION;
import static kitchenpos.exception.MenuExceptionType.PRICE_IS_UNDER_TOTAL_PRODUCT_AMOUNT;
import static kitchenpos.exception.MenuGroupExceptionType.NOT_EXIST_MENU_GROUP_EXCEPTION;
import static kitchenpos.exception.ProductExceptionType.NOT_EXIST_PRODUCT_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.exception.MenuException;
import kitchenpos.exception.MenuExceptionType;
import kitchenpos.exception.MenuGroupException;
import kitchenpos.exception.MenuGroupExceptionType;
import kitchenpos.exception.ProductException;
import kitchenpos.exception.ProductExceptionType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;

    @Nested
    class 메뉴_저장 {

        @Test
        void 메뉴를_저장한다() {
            // given
            List<MenuProductCreateRequest> menuProductCreateRequests = List.of(
                    new MenuProductCreateRequest(1L, 1L),
                    new MenuProductCreateRequest(2L, 1L)
            );
            MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "수원왕갈비통닭",
                    BigDecimal.valueOf(1000),
                    1L,
                    menuProductCreateRequests
            );

            // when
            assertThatNoException().isThrownBy(
                    () -> menuService.create(menuCreateRequest)
            );
        }

        @Test
        void 메뉴_저장시_메뉴_상품도_함께_저장한다() {
            // given
            List<MenuProductCreateRequest> menuProductCreateRequests = List.of(
                    new MenuProductCreateRequest(1L, 1L),
                    new MenuProductCreateRequest(2L, 1L)
            );
            MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "수원왕갈비통닭",
                    BigDecimal.valueOf(1000),
                    1L,
                    menuProductCreateRequests
            );

            // when
            Menu menu = menuService.create(menuCreateRequest);

            // then
            assertThat(menu.menuProducts().size()).isEqualTo(2);
        }

        @Test
        void 메뉴_가격이_Null이면_예외_발생() {
            // given
            List<MenuProductCreateRequest> menuProductCreateRequests = List.of(
                    new MenuProductCreateRequest(1L, 1L),
                    new MenuProductCreateRequest(2L, 1L)
            );
            MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "수원왕갈비통닭",
                    null,
                    1L,
                    menuProductCreateRequests
            );

            // when
            MenuExceptionType exceptionType = assertThrows(MenuException.class, () ->
                    menuService.create(menuCreateRequest)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(NULL_PRICE_EXCEPTION);
        }

        @Test
        void 메뉴_가격이_음수이면_예외_발생() {
            // given
            List<MenuProductCreateRequest> menuProductCreateRequests = List.of(
                    new MenuProductCreateRequest(1L, 1L),
                    new MenuProductCreateRequest(2L, 1L)
            );
            MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "수원왕갈비통닭",
                    BigDecimal.valueOf(-1),
                    1L,
                    menuProductCreateRequests
            );

            // when
            MenuExceptionType exceptionType = assertThrows(MenuException.class, () ->
                    menuService.create(menuCreateRequest)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(NEGATIVE_PRICE_EXCEPTION);
        }

        @Test
        void 메뉴_가격이_메뉴_상품_총합보다_크면_예외_발생() {
            // given
            List<MenuProductCreateRequest> menuProductCreateRequests = List.of(
                    new MenuProductCreateRequest(1L, 1L),
                    new MenuProductCreateRequest(2L, 1L)
            );
            MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "수원왕갈비통닭",
                    BigDecimal.valueOf(999999999),
                    1L,
                    menuProductCreateRequests
            );

            // when
            MenuExceptionType exceptionType = assertThrows(MenuException.class, () ->
                    menuService.create(menuCreateRequest)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(PRICE_IS_UNDER_TOTAL_PRODUCT_AMOUNT);
        }

        @Test
        void 메뉴_그룹이_존재하지_않으면_예외_발생() {
            // given
            List<MenuProductCreateRequest> menuProductCreateRequests = List.of(
                    new MenuProductCreateRequest(1L, 1L),
                    new MenuProductCreateRequest(2L, 1L)
            );
            MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "수원왕갈비통닭",
                    BigDecimal.valueOf(-1),
                    99L,
                    menuProductCreateRequests
            );

            // when
            MenuGroupExceptionType exceptionType = assertThrows(MenuGroupException.class, () ->
                    menuService.create(menuCreateRequest)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(NOT_EXIST_MENU_GROUP_EXCEPTION);
        }

        @Test
        void 상품이_존재하지_않으면_예외_발생() {
            // given
            List<MenuProductCreateRequest> menuProductCreateRequests = List.of(
                    new MenuProductCreateRequest(99L, 1L),
                    new MenuProductCreateRequest(999L, 1L)
            );
            MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    "수원왕갈비통닭",
                    BigDecimal.valueOf(1000),
                    1L,
                    menuProductCreateRequests
            );

            // when
            ProductExceptionType exceptionType = assertThrows(ProductException.class, () ->
                    menuService.create(menuCreateRequest)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(NOT_EXIST_PRODUCT_EXCEPTION);
        }
    }

    @Nested
    class 모든_메뉴_조회 {

        @Test
        void 모든_메뉴를_조회한다() {
            // when
            List<Menu> menus = menuService.list();

            // then
            assertThat(menus).isNotNull();
            assertThat(menus.size()).isGreaterThanOrEqualTo(0);
        }
    }
}
