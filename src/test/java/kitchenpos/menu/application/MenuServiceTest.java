package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.request.MenuProductRequest;
import kitchenpos.menu.application.dto.request.MenuRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    @Test
    void 메뉴를_생성한다() {
        // given
        String menuName = "menu";
        BigDecimal menuPrice = BigDecimal.TEN;

        BigDecimal productPrice = BigDecimal.TEN;
        String productName = "치킨";

        Menu menu = new Menu(1L, menuName, menuPrice, 1L);
        MenuProduct menuProduct = new MenuProduct(1L, menu, 1L, 1);

        willDoNothing()
                .given(menuValidator).validate(any(), anyList());

        given(menuRepository.save(any(Menu.class)))
                .willReturn(menu);

        given(menuProductRepository.save(any(MenuProduct.class)))
                .willReturn(menuProduct);

        // when
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1);
        MenuRequest menuRequest = new MenuRequest(menuName, menuPrice, 1L, List.of(menuProductRequest));
        menuService.create(menuRequest);

        // then
        then(menuRepository).should(times(1)).save(any());
    }

    @Test
    void 메뉴의_가격은_0원_이상이다() {
        // given
        MenuRequest minusPriceMenu = new MenuRequest(
                "치킨",
                BigDecimal.valueOf(-1),
                1L,
                List.of(new MenuProductRequest(1L, 1))
        );
        willThrow(IllegalArgumentException.class)
                .given(menuValidator).validate(any(), anyList());

        // when, then
        assertThatThrownBy(() -> menuService.create(minusPriceMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_null_이면_예외발생() {
        // given
        MenuRequest menu = new MenuRequest(
                "치킨",
                null,
                1L,
                List.of(new MenuProductRequest(1L, 1))
        );

        willThrow(IllegalArgumentException.class)
                .given(menuValidator).validate(any(), anyList());
        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성하려는_메뉴의_메뉴그룹이_존재하지_않으면_예외발생() {
        // given
        MenuRequest nullMenuGroupMenu = new MenuRequest(
                "치킨",
                BigDecimal.valueOf(100),
                null,
                List.of(new MenuProductRequest(1L, 1))
        );

        willThrow(IllegalArgumentException.class)
                .given(menuValidator).validate(any(), anyList());

        // when, then
        assertThatThrownBy(() -> menuService.create(nullMenuGroupMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_구성_상품의_합보다_크면_예외발생() {
        // given
        MenuRequest expensiveMenu = new MenuRequest(
                "치킨",
                BigDecimal.valueOf(11),
                1L,
                List.of(new MenuProductRequest(1L, 1))
        );
        willThrow(IllegalArgumentException.class)
                .given(menuValidator).validate(any(), anyList());

        // when, then
        assertThatThrownBy(() -> menuService.create(expensiveMenu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuRepository).should(never()).save(any());
    }

    @Test
    void 메뉴_구성_상품은_이미_존재하는_상품이어야_한다() {
        // given
        MenuRequest menu = new MenuRequest(
                "치킨",
                BigDecimal.valueOf(9),
                1L,
                List.of(new MenuProductRequest(1L, 1))
        );

        willThrow(IllegalArgumentException.class)
                .given(menuValidator).validate(any(), anyList());

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuRepository).should(never()).save(any());
    }
}
