package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.utils.DomainFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @DisplayName("Menu 등록 성공")
    @Test
    void create() {
        BigDecimal price = new BigDecimal(10000);
        MenuProduct menuProduct = DomainFactory.createMenuProduct(null, 1L, 2L);
        Menu menu = DomainFactory.createMenu(null, price, 1L, menuProduct);
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(DomainFactory.createProduct(1L, price)));
        given(menuDao.save(menu)).willReturn(DomainFactory.createMenu(1L, price, 1L, menuProduct));
        given(menuProductDao.save(menuProduct)).willReturn(DomainFactory.createMenuProduct(1L, 1L, 2L));

        Menu actual = menuService.create(menu);
        MenuProduct expectMenuProduct = DomainFactory.createMenuProduct(1L, 1L, 2L);
        Menu expectMenu = DomainFactory.createMenu(1L, price, 1L, expectMenuProduct);

        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(expectMenu.getId());
            assertThat(actual.getMenuGroupId()).isEqualTo(expectMenu.getMenuGroupId());
            assertThat(actual.getPrice()).isEqualTo(expectMenu.getPrice());
            assertThat(actual.getMenuProducts().get(0)).isEqualToComparingFieldByField(expectMenuProduct);
        });
    }

    @DisplayName("Menu 가격이 0보다 작은 경우 예외 테스트")
    @Test
    void createPriceLessThanZero() {
        Menu menu = DomainFactory.createMenu(null, BigDecimal.ZERO, null, null);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu GruopId가 존재하지 않는 경우 예외 테스트")
    @Test
    void createNotExistMenuGroupId() {
        Menu menu = DomainFactory.createMenu(null, BigDecimal.ONE, 1L, null);
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(false);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 속하는 MenuProducts의 총 금액이 메뉴의 가격보다 작은 경우 예외 테스트")
    @Test
    void createMenuProductsAmountLessThanMenuPrice() {
        MenuProduct menuProduct = DomainFactory.createMenuProduct(null, 1L, 2L);
        Menu menu = DomainFactory.createMenu(null, new BigDecimal(20000), 1L, menuProduct);
        Product product = DomainFactory.createProduct(null, new BigDecimal(9999));
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu 전체조회 테스트")
    @Test
    void list() {
        Menu menu = new Menu();
        menu.setId(1L);
        given(menuDao.findAll()).willReturn(Arrays.asList(menu));
        MenuProduct menuProduct = DomainFactory.createMenuProduct(1L, 1L, 2L);
        given(menuProductDao.findAllByMenuId(menu.getId())).willReturn(Arrays.asList(menuProduct));

        Menu actual = menuService.list().get(0);

        assertThat(actual.getMenuProducts().get(0)).isEqualToComparingFieldByField(menuProduct);
    }
}