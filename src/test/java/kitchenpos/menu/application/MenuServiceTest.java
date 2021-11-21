package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.TestFixtures;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.application.dto.MenuResponses;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.product.domain.repository.ProductRepository;
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
        final MenuGroup menuGroup = MenuGroup.builder()
                .id(1L)
                .name("메뉴그룹1")
                .build();
        menu = Menu.builder()
                .name("분짜와 스프링롤")
                .price(BigDecimal.valueOf(16000))
                .menuGroupId(menuGroup.getId())
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
                .menu(savedMenu1)
                .productId(savedProduct1.getId())
                .quantity(1L)
                .build();
        menuProduct2 = MenuProduct.builder()
                .id(2L)
                .menu(savedMenu2)
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
        final List<Product> products = Arrays.asList(savedProduct1, savedProduct2);
        final MenuRequest request = new MenuRequest(menuRequest.getName(), menuRequest.getPrice(),
                menuRequest.getMenuGroupId(), menuProductsRequest);
        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findByIdIn(any())).thenReturn(products);
        when(menuRepository.save(any())).thenReturn(savedMenu1);

        final MenuResponse actual = menuService.create(request);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(new MenuResponse(savedMenu1));
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
        final Product weirdSavedProduct = TestFixtures.createProduct(3L, "스프링롤", 2000L);
        final MenuProduct weirdMenuProduct = TestFixtures.createMenuProduct(weirdSavedProduct.getId(), savedMenu1, 1L);
        final MenuProductRequest weirdMenuProductRequest = new MenuProductRequest(
                weirdMenuProduct.getProductId(), weirdMenuProduct.getQuantity()
        );
        final MenuRequest menuRequest = new MenuRequest("메뉴이름", 16000L, 1L,
                Arrays.asList(new MenuProductRequest(menuProduct1), weirdMenuProductRequest));
        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(menuRepository.save(any())).thenReturn(savedMenu1);

        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다")
    @Test
    void list() {
        final List<Menu> menus = Arrays.asList(savedMenu1, savedMenu2);
        savedMenu1.updateMenuProducts(new MenuProducts(Collections.singletonList(menuProduct1)));
        savedMenu2.updateMenuProducts(new MenuProducts(Collections.singletonList(menuProduct2)));
        when(menuRepository.findAllJoinFetch()).thenReturn(menus);

        final MenuResponses actual = menuService.list();

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(new MenuResponses(menus));
    }
}
