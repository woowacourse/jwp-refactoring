package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.dtos.MenuProductRequest;
import kitchenpos.application.dtos.MenuRequest;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴")
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

    private Menu menu;
    private MenuRequest menuRequest;
    private Menu savedMenu1;
    private Menu savedMenu2;
    private Product savedProduct1;
    private Product savedProduct2;
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;

    @BeforeEach
    void setUp() {
        menu = Menu.builder()
                .name("분짜와 스프링롤")
                .price(BigDecimal.valueOf(16000))
                .menuGroupId(1L)
                .build();
        menuRequest = new MenuRequest("분짜와 스프링롤", 16000L, 1L, Collections.emptyList());
        savedMenu1 = Menu.builder()
                .of(menu)
                .id(1L)
                .build();
        savedMenu2 = Menu.builder()
                .of(menu)
                .id(2L)
                .build();
        savedProduct1 = Product.builder()
                .name("분짜")
                .price(BigDecimal.valueOf(13000))
                .id(1L)
                .build();
        savedProduct2 = Product.builder()
                .name("스프링롤")
                .price(BigDecimal.valueOf(3000))
                .id(2L)
                .build();
        menuProduct1 = MenuProduct.builder()
                .id(1L)
                .menuId(savedMenu1.getId())
                .productId(savedProduct1.getId())
                .quantity(1L)
                .build();
        menuProduct2 = MenuProduct.builder()
                .id(2L)
                .menuId(savedMenu2.getId())
                .productId(savedProduct2.getId())
                .quantity(1L)
                .build();
    }

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void create() {
        final List<MenuProductRequest> menuProductsRequest = Arrays.asList(
                new MenuProductRequest(menuProduct1),
                new MenuProductRequest(menuProduct2)
        );
        final MenuRequest request = new MenuRequest(menuRequest.getName(), menuRequest.getPrice(),
                menuRequest.getMenuGroupId(), menuProductsRequest);

        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findById(savedProduct1.getId())).thenReturn(Optional.of(savedProduct1));
        when(productRepository.findById(savedProduct2.getId())).thenReturn(Optional.of(savedProduct2));
        when(menuRepository.save(any())).thenReturn(savedMenu1);

        final Menu actual = menuService.create(request);
        assertThat(actual).isEqualTo(savedMenu1);
    }

    @DisplayName("메뉴의 가격은 0 원 이상이어야 한다")
    @Test
    void createExceptionPriceUnderZero() {
        final MenuRequest request = new MenuRequest("분짜와 스프링롤", -1L, 1L, Collections.emptyList());

        assertThatThrownBy(() -> menuService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 속한 메뉴 그룹이 존재해야 한다")
    @Test
    void createExceptionMenuGroup() {
        when(menuGroupRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 상품 목록에서 (상품의 가격 * 메뉴 상품의 갯수) 의 합과 메뉴 그룹의 가격을 비교했을 때, 메뉴 그룹의 가격이 더 크면 안 된다")
    @Test
    void createExceptionSum() {
        final Product weirdSavedProduct = Product.builder()
                .name("스프링롤")
                .price(BigDecimal.valueOf(2000))
                .id(3L)
                .build();
        final MenuProduct weirdMenuProduct = MenuProduct.builder()
                .of(menuProduct2)
                .productId(weirdSavedProduct.getId())
                .build();
        final MenuProductRequest weirdMenuProductRequest = new MenuProductRequest(weirdMenuProduct.getProductId(),
                weirdMenuProduct.getQuantity());
        final MenuRequest menuRequest = new MenuRequest("메뉴이름", 16000L, 1L,
                Arrays.asList(new MenuProductRequest(menuProduct1), weirdMenuProductRequest));

        when(menuGroupRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(savedProduct1.getId())).thenReturn(Optional.of(savedProduct1));
        when(productRepository.findById(weirdSavedProduct.getId())).thenReturn(Optional.of(weirdSavedProduct));

        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다")
    @Test
    void list() {
        final List<Menu> menus = Arrays.asList(savedMenu1, savedMenu2);
        final List<MenuProduct> menuProducts1 = Arrays.asList(menuProduct1, menuProduct2);
        final List<MenuProduct> menuProducts2 = Collections.singletonList(new MenuProduct());

        when(menuRepository.findAll()).thenReturn(menus);
        when(menuProductRepository.findAllByMenuId(1L)).thenReturn(menuProducts1);
        when(menuProductRepository.findAllByMenuId(2L)).thenReturn(menuProducts2);

        final List<Menu> actual = menuService.list();
        assertThat(actual).isEqualTo(menus);
    }
}
