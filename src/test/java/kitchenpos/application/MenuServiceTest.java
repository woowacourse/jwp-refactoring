package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

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

    private Menu menu1;
    private Menu menu2;
    private Menu menu3;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        product1 = ProductFixture.createWithId(ProductFixture.ID1, 9000L);
        product2 = ProductFixture.createWithId(ProductFixture.ID2, 1000L);

        MenuProduct menuProduct1 = MenuProductFixture.create(1L,1L, 2);
        MenuProduct menuProduct2 = MenuProductFixture.create(1L, 2L, 2);

        menu1 = MenuFixture.createWithoutId(1L, Arrays.asList(menuProduct1, menuProduct2), 18000L);
        menu2 = MenuFixture.createWithId(1L, 1L, Arrays.asList(menuProduct1, menuProduct2), 22000L);
        menu3 = MenuFixture.createWithoutId(3L, Arrays.asList(menuProduct1, menuProduct2), -1500L);
    }

    @DisplayName("정상적으로 Menu를 생성한다")
    @Test
    void create() {
        when(menuGroupDao.existsById(menu1.getMenuGroupId())).thenReturn(true);
        when(menuDao.save(menu1)).thenReturn(menu2);
        when(productDao.findById(anyLong())).thenReturn(Optional.of(product1));
        when(menuProductDao.save(any())).then(AdditionalAnswers.returnsFirstArg());

        Menu savedMenu = menuService.create(menu1);
        assertThat(savedMenu).isEqualToComparingFieldByField(menu2);
        assertThat(savedMenu.getMenuProducts())
            .extracting(MenuProduct::getMenuId)
            .allMatch(id -> id.equals(savedMenu.getId()));
    }

    @DisplayName("Price가 null이거나 음수인 Menu 생성 요청 시 예외를 반환한다")
    @Test
    void createIllegalPriceMenu() {
        assertThatThrownBy(() -> menuService.create(menu3))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu Group이 등록되어있지 않은 Menu 생성 시 예외를 반환한다")
    @Test
    void createNotExistMenuGroup() {
        when(menuGroupDao.existsById(menu1.getMenuGroupId())).thenReturn(false);

        assertThatThrownBy(() -> menuService.create(menu1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 Product를 포함하는 Menu 생성 시 예외를 반환한다.")
    @Test
    void createNotExistProduct() {
        when(menuGroupDao.existsById(menu1.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(menu1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Product price의 합보다 비싼 Menu 생성 시 예외를 반환한다.")
    @Test
    void createMoreExpensive() {
        when(menuGroupDao.existsById(menu1.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(product1));
        when(productDao.findById(2L)).thenReturn(Optional.of(product2));

        assertThatThrownBy(() -> menuService.create(menu2))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 저장된 Menu를 불러온다")
    @Test
    void list() {
        when(menuDao.findAll()).thenReturn(Arrays.asList(menu1, menu2, menu3));

        assertThat(menuService.list())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(menu1, menu2, menu3));
    }
}
