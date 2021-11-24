import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuRequest.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.MenuResponse.MenuProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 등록")
    @Test
    void create() {
        long menuGroupId = 1L;
        long productId = 2L;
        long menuId = 3L;
        long seq = 4L;
        MenuRequest request = new MenuRequest(
            "후라이드+후라이드", BigDecimal.valueOf(19000),
            menuGroupId, Collections.singletonList(new MenuProductRequest(productId, 2))
        );
        Mockito.when(menuRepository.save(ArgumentMatchers.any(Menu.class)))
            .thenAnswer(invocation -> {
                Menu menu = invocation.getArgument(0);
                return new Menu(menuId, menu.getName(), menu.getPrice(), menu.getMenuGroupId(),
                    newMenuProductsOf(menu, seq));
            });

        MenuResponse actual = menuService.create(request);
        MenuResponse expected = new MenuResponse(menuId, request.getName(), request.getPrice(),
            request.getMenuGroupId(),
            Collections.singletonList(new MenuProductResponse(seq, menuId, productId, 2)));

        Mockito.verify(menuRepository, Mockito.times(1)).save(ArgumentMatchers.any(Menu.class));
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    private MenuProducts newMenuProductsOf(final Menu menu, final long seq) {
        return new MenuProducts(
            menu.getMenuProducts()
                .getElements()
                .stream()
                .map(menuProduct -> new MenuProduct(
                    seq,
                    menuProduct.getProductId(),
                    2L
                )).collect(Collectors.toList())
        );
    }

    @DisplayName("가격이 음수인 메뉴 등록시 예외 처리")
    @Test
    void createWithNegativePrice() {
        MenuRequest menuToCreate = new MenuRequest(
            "후라이드+후라이드",
            BigDecimal.valueOf(-1),
            1L,
            Collections.singletonList(new MenuProductRequest(1L, 2))
        );

        assertThatThrownBy(() -> menuService.create(menuToCreate)).isExactlyInstanceOf(
            IllegalArgumentException.class
        );
    }

    @DisplayName("메뉴 조회")
    @Test
    void list() {
        Long menuGroupId = 1L;
        Menu friedChicken = new Menu(1L, new Name("후라이드치킨"), new Price(BigDecimal.valueOf(16000)),
            menuGroupId,
            new MenuProducts(Collections.singletonList(
                new MenuProduct(1L, 2L)
            )));
        Menu seasonedSpicyChicken = new Menu(2L, new Name("후라이드치킨"),
            new Price(BigDecimal.valueOf(16000)),
            menuGroupId,
            new MenuProducts(Collections.singletonList(
                new MenuProduct(2L, 2L)
            )));
        List<Menu> menus = Arrays.asList(friedChicken, seasonedSpicyChicken);
        Mockito.when(menuRepository.findAll()).thenReturn(menus);

        List<MenuResponse> actual = menuService.list();

        List<MenuResponse> expected = Arrays.asList(
            MenuResponse.from(friedChicken),
            MenuResponse.from(seasonedSpicyChicken)
        );
        assertThat(actual).hasSameSizeAs(expected)
            .usingRecursiveFieldByFieldElementComparator()
            .isEqualTo(expected);
    }
}
