package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    private List<MenuProduct> menuProducts;
    private long totalPrice;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, menuProductRepository, productRepository);

        Product product1 = ProductFixture.createWithId(ProductFixture.ID1);
        Product product2 = ProductFixture.createWithId(ProductFixture.ID2);

        MenuProduct menuProduct1 = MenuProductFixture.create(1L, product1.getId());
        MenuProduct menuProduct2 = MenuProductFixture.create(1L, product2.getId());

        List<Product> products = Arrays.asList(product1, product2);
        menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        totalPrice = getTotalPrice(products, menuProducts);
    }

    @DisplayName("Menu 생성")
    @Test
    void create() {
        MenuCreateRequest request = MenuFixture.createRequest(totalPrice, 1L,
            MenuProductFixture.createRequest(1L, 1), MenuProductFixture.createRequest(2L, 1));
        Menu menuWithoutId = MenuFixture.createWithoutId(1L, totalPrice);
        Menu menuWithId = MenuFixture.createWithId(1L, 1L, totalPrice);
        Product product = ProductFixture.createWithId(1L);

        when(menuGroupRepository.existsById(menuWithoutId.getMenuGroupId())).thenReturn(true);
        when(menuRepository.save(any(Menu.class))).thenReturn(menuWithId);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        MenuResponse savedMenu = menuService.create(request);
        assertThat(savedMenu).isEqualToComparingFieldByField(menuWithId);
    }

    @DisplayName("Price가 null인 Menu 생성 요청 시 예외를 반환한다")
    @Test
    void createNullPriceMenu() {
        MenuCreateRequest request = MenuFixture.createRequest(totalPrice, 1L,
            MenuProductFixture.createRequest(1L, 1));
        Menu menuWithNegativePrice = MenuFixture.createNullPrice(1L);

        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Price가 음수인 Menu 생성 요청 시 예외를 반환한다")
    @Test
    void createNegativePriceMenu() {
        MenuCreateRequest request = MenuFixture.createRequest(totalPrice, 1L,
            MenuProductFixture.createRequest(1L, 1));
        Menu menuWithNegativePrice = MenuFixture.createWithoutId(1L, -1000L);

        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu Group에 포함되어 있지 않은 Menu 생성 시 예외를 반환한다")
    @Test
    void createNotExistMenuGroup() {
        MenuCreateRequest request = MenuFixture.createRequest(totalPrice, 1L,
            MenuProductFixture.createRequest(1L, 1));
        Menu menu = MenuFixture.createWithoutId(1L, totalPrice);
        when(menuGroupRepository.existsById(menu.getMenuGroupId())).thenReturn(false);

        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 Product를 포함하는 Menu 생성 시 예외를 반환한다.")
    @Test
    void createNotExistProduct() {
        MenuCreateRequest request = MenuFixture.createRequest(totalPrice, 1L,
            MenuProductFixture.createRequest(1L, 1));
        Menu menu = MenuFixture.createWithoutId(1L, totalPrice);
        when(menuGroupRepository.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Product price의 합보다 비싼 Menu 생성 시 예외를 반환한다.")
    @Test
    void createMoreExpensive() {
        MenuCreateRequest request = MenuFixture.createRequest(totalPrice, 1L,
            MenuProductFixture.createRequest(1L, 1));
        Menu menuOverTotalPrice =
            MenuFixture.createWithoutId(1L, totalPrice + 1000L);
        when(menuGroupRepository.existsById(menuOverTotalPrice.getMenuGroupId())).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(ProductFixture.createWithId(1L)));

        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 저장된 Menu를 불러온다")
    @Test
    void list() {
        Menu menu1 = MenuFixture.createWithoutId(1L, totalPrice);
        Menu menu2 = MenuFixture.createWithoutId(1L, totalPrice);
        Menu menu3 = MenuFixture.createWithoutId(1L, totalPrice);
        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu1, menu2, menu3));

        assertThat(menuService.list())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(menu1, menu2, menu3));
    }

    private long getTotalPrice(List<Product> products, List<MenuProduct> menuProducts) {
        Map<Long, BigDecimal> collect = products.stream()
            .collect(Collectors.toMap(Product::getId, Product::getPrice));
        return menuProducts.stream()
            .mapToLong(p -> collect.getOrDefault(p.getProductId(), BigDecimal.ZERO).longValue())
            .sum();
    }
}
