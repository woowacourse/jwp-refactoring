package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.request.MenuRequest.MenuProductRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.MenuResponse.MenuProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MenuServiceTest extends ServiceTest {

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
        when(menuRepository.save(any(Menu.class))).thenAnswer(invocation -> {
            Menu menu = invocation.getArgument(0);
            return new Menu(menuId, menu.getName(), menu.getPrice(), menu.getMenuGroup(),
                newMenuProductsOf(menu, seq, menuId));
        });
        when(menuGroupRepository.findById(request.getMenuGroupId())).thenReturn(
            Optional.of(new MenuGroup(menuGroupId, "두마리메뉴"))
        );
        when(productRepository.findById(productId)).thenAnswer(invocation ->
            Optional.of(new Product(productId, "후라이드", BigDecimal.valueOf(16000)))
        );

        MenuResponse actual = menuService.create(request);
        MenuResponse expected = new MenuResponse(menuId, request.getName(), request.getPrice(),
            request.getMenuGroupId(),
            Collections.singletonList(new MenuProductResponse(seq, menuId, productId, 2)));

        verify(menuRepository, times(1)).save(any(Menu.class));
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    private MenuProducts newMenuProductsOf(final Menu menu, final long seq, final long menuId) {
        return new MenuProducts(
            menu.getMenuProducts()
                .getElements()
                .stream()
                .map(menuProduct -> new MenuProduct(
                    seq,
                    new Menu(menuId, menu.getName(), menu.getPrice(), menu.getMenuGroup()),
                    new Product(
                        menuProduct.getProduct().getId(),
                        "후라이드",
                        BigDecimal.valueOf(16000)
                    ),
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

    @DisplayName("메뉴 가격이 메뉴 상품 가격 합보다 크면 예외 처리")
    @Test
    void createWhenPriceOfMenuIsGreaterThanTotalPriceOfMenuProduct() {
        MenuRequest menuToCreate = new MenuRequest(
            "후라이드+후라이드",
            BigDecimal.valueOf(33000),
            1L,
            Collections.singletonList(new MenuProductRequest(1L, 2))
        );
        when(menuGroupRepository.findById(menuToCreate.getMenuGroupId())).thenReturn(
            Optional.of(new MenuGroup(1L, "두마리메뉴"))
        );
        when(productRepository.findById(1L)).thenAnswer(
            invocation -> Optional.of(new Product(1L, "후라이드", BigDecimal.valueOf(16000)))
        );

        assertThatThrownBy(() -> menuService.create(menuToCreate)).isExactlyInstanceOf(
            IllegalArgumentException.class
        );
    }

    @DisplayName("등록되지 않은 메뉴 그룹으로 메뉴 등록시 예외 처리")
    @Test
    void createWithNotFoundMenuGroup() {
        MenuRequest menuToCreate = new MenuRequest(
            "후라이드+후라이드",
            BigDecimal.valueOf(19000),
            1L,
            Collections.singletonList(new MenuProductRequest(1L, 2)));
        when(menuGroupRepository.findById(menuToCreate.getMenuGroupId())).thenReturn(
            Optional.empty()
        );

        assertThatThrownBy(() -> menuService.create(menuToCreate)).isExactlyInstanceOf(
            IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 상품으로 메뉴 등록시 예외 처리")
    @Test
    void createWithNotFoundProduct() {
        MenuRequest menuToCreate = new MenuRequest(
            "후라이드+후라이드",
            BigDecimal.valueOf(19000),
            1L,
            Collections.singletonList(new MenuProductRequest(1L, 2)));
        when(menuGroupRepository.findById(menuToCreate.getMenuGroupId())).thenReturn(
            Optional.of(new MenuGroup(1L, "두마리메뉴"))
        );
        when(productRepository.findById(1L)).thenAnswer(invocation -> Optional.empty());

        assertThatThrownBy(() -> menuService.create(menuToCreate)).isExactlyInstanceOf(
            IllegalArgumentException.class);
    }

    @DisplayName("메뉴 조회")
    @Test
    void list() {
        MenuGroup menuGroup = new MenuGroup(1L, "두마리메뉴");
        Menu friedChicken = new Menu(
            1L,
            "후라이드치킨",
            BigDecimal.valueOf(16000),
            menuGroup
        );
        friedChicken.addProduct(new Product(1L, "후라이드", BigDecimal.valueOf(16000)), 2);
        Menu seasonedSpicyChicken = new Menu(
            2L,
            "양념치킨",
            BigDecimal.valueOf(16000),
            menuGroup
        );
        seasonedSpicyChicken.addProduct(new Product(2L, "양념치킨", BigDecimal.valueOf(16000)), 2);
        List<Menu> menus = Arrays.asList(friedChicken, seasonedSpicyChicken);
        when(menuRepository.findAll()).thenReturn(menus);

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
