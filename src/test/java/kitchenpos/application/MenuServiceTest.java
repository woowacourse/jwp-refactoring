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

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    private List<MenuProduct> menuProducts;
    private long totalPrice;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

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
        Menu menuWithoutId = MenuFixture.createWithoutId(1L, menuProducts, totalPrice);
        Menu menuWithId = MenuFixture.createWithId(1L, 1L, menuProducts, totalPrice);
        Product product = ProductFixture.createWithId(1L);

        when(menuGroupDao.existsById(menuWithoutId.getMenuGroupId())).thenReturn(true);
        when(menuDao.save(menuWithoutId)).thenReturn(menuWithId);
        when(productDao.findById(anyLong())).thenReturn(Optional.of(product));
        when(menuProductDao.save(any())).then(AdditionalAnswers.returnsFirstArg());

        Menu savedMenu = menuService.create(menuWithoutId);
        assertThat(savedMenu).isEqualToComparingFieldByField(menuWithId);
        assertThat(savedMenu.getMenuProducts())
            .extracting(MenuProduct::getMenuId)
            .allMatch(id -> id.equals(savedMenu.getId()));
    }

    @DisplayName("Price가 null인 Menu 생성 요청 시 예외를 반환한다")
    @Test
    void createNullPriceMenu() {
        Menu menuWithNegativePrice = MenuFixture.createNullPrice(1L, menuProducts);

        assertThatThrownBy(() -> menuService.create(menuWithNegativePrice))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Price가 음수인 Menu 생성 요청 시 예외를 반환한다")
    @Test
    void createNegativePriceMenu() {
        Menu menuWithNegativePrice = MenuFixture.createWithoutId(1L, menuProducts, -1000L);

        assertThatThrownBy(() -> menuService.create(menuWithNegativePrice))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu Group에 포함되어 있지 않은 Menu 생성 시 예외를 반환한다")
    @Test
    void createNotExistMenuGroup() {
        Menu menu = MenuFixture.createWithoutId(1L, menuProducts, totalPrice);
        when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(false);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 Product를 포함하는 Menu 생성 시 예외를 반환한다.")
    @Test
    void createNotExistProduct() {
        Menu menu = MenuFixture.createWithoutId(1L, menuProducts, totalPrice);
        when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Product price의 합보다 비싼 Menu 생성 시 예외를 반환한다.")
    @Test
    void createMoreExpensive() {
        Menu menuOverTotalPrice =
            MenuFixture.createWithoutId(1L, menuProducts, totalPrice + 1000L);
        when(menuGroupDao.existsById(menuOverTotalPrice.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(ProductFixture.createWithId(1L)));
        when(productDao.findById(2L)).thenReturn(Optional.of(ProductFixture.createWithId(2L)));

        assertThatThrownBy(() -> menuService.create(menuOverTotalPrice))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 저장된 Menu를 불러온다")
    @Test
    void list() {
        Menu menu1 = MenuFixture.createWithoutId(1L, menuProducts, totalPrice);
        Menu menu2 = MenuFixture.createWithoutId(1L, menuProducts, totalPrice);
        Menu menu3 = MenuFixture.createWithoutId(1L, menuProducts, totalPrice);
        when(menuDao.findAll()).thenReturn(Arrays.asList(menu1, menu2, menu3));

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
