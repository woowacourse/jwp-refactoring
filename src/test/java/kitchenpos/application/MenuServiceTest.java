package kitchenpos.application;

import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    void 메뉴를_생성한다() {
        // given
        String menuName = "menu";
        BigDecimal menuPrice = BigDecimal.TEN;

        BigDecimal productPrice = BigDecimal.TEN;
        String productName = "치킨";
        Product product = new Product(productName, productPrice);
        MenuProduct menuProduct = new MenuProduct(product, 1);

        MenuGroup menuGroup = new MenuGroup(1L, "한식");
        Menu menu = new Menu(1L, menuName, menuPrice, menuGroup, List.of(menuProduct));

        given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(new MenuGroup(1L, "한식")));
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));
        given(menuRepository.save(any(Menu.class)))
                .willReturn(menu);

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

        given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(new MenuGroup(1L, "한식")));

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

        given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(new MenuGroup(1L, "한식")));
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

        // when, then
        assertThatThrownBy(() -> menuService.create(nullMenuGroupMenu))
                .isInstanceOf(IllegalArgumentException.class);
        then(productRepository).should(never()).findById(anyLong());
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

        Product product = new Product("치킨", BigDecimal.TEN);

        given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(new MenuGroup(1L, "한식")));

        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));

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

        Product product = new Product("치킨", BigDecimal.TEN);

        given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(new MenuGroup(1L, "한식")));

        // 예외 상황: 존재하지 않는 상품
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuRepository).should(never()).save(any());
    }
}
