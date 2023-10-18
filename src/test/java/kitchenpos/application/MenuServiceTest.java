package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.exception.MenuServiceException.NotExistsMenuGroupException;
import kitchenpos.application.exception.MenuServiceException.NotExistsProductException;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.exception.MenuException.PriceMoreThanProductsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private static final int MENU_PRICE = 20000;
    private static final long MENU_GROUP_ID = 1L;
    private static final long PRODUCT1_ID = 1L;
    private static final int PRODUCT1_QUANTITY = 2;
    private static final long PRODUCT2_ID = 2L;
    private static final int PRODUCT2_QUANTITY = 3;
    private static final int PRODUCT1_PRICE = 1000;
    private static final int PRODUCT2_PRICE = 10000;

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

    private Menu menu = new Menu();
    private MenuProduct menuProduct1 = new MenuProduct();
    private MenuProduct menuProduct2 = new MenuProduct();
    private Product product1 = new Product();
    private Product product2 = new Product();

    @BeforeEach
    void init() {
        menu.setPrice(BigDecimal.valueOf(MENU_PRICE));
        menu.setMenuGroupId(MENU_GROUP_ID);
        menu.setMenuProducts(List.of(menuProduct1, menuProduct2));

        menuProduct1.setProductId(PRODUCT1_ID);
        menuProduct1.setQuantity(PRODUCT1_QUANTITY);
        menuProduct2.setProductId(PRODUCT2_ID);
        menuProduct2.setQuantity(PRODUCT2_QUANTITY);

        product1.setPrice(BigDecimal.valueOf(PRODUCT1_PRICE));
        product2.setPrice(BigDecimal.valueOf(PRODUCT2_PRICE));
    }

    @Test
    @DisplayName("메뉴를 생성할 수 있다. - 메뉴가 생성될 때 메뉴 상품들도 함께 저장된다.")
    void create_success1() {
        when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(menuProduct1.getProductId())).thenReturn(Optional.ofNullable(product1));
        when(productDao.findById(menuProduct2.getProductId())).thenReturn(Optional.ofNullable(product2));

        when(menuDao.save(menu)).thenReturn(new Menu());

        menuService.create(menu);

        assertAll(
                () -> verify(menuProductDao, times(1)).save(menuProduct1),
                () -> verify(menuProductDao, times(1)).save(menuProduct2)
        );
    }

    @Test
    @DisplayName("메뉴를 생성할 수 있다. - 메뉴 가격이 0원이고, 상품 가격들도 0원이면 메뉴를 생성한다.")
    void create_success2() {
        menu.setPrice(BigDecimal.valueOf(0));
        product1.setPrice(BigDecimal.valueOf(0));
        product2.setPrice(BigDecimal.valueOf(0));

        when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(menuProduct1.getProductId())).thenReturn(Optional.ofNullable(product1));
        when(productDao.findById(menuProduct2.getProductId())).thenReturn(Optional.ofNullable(product2));

        when(menuDao.save(menu)).thenReturn(new Menu());

        menuService.create(menu);

        assertAll(
                () -> verify(menuProductDao, times(1)).save(menuProduct1),
                () -> verify(menuProductDao, times(1)).save(menuProduct2)
        );
    }

    @Test
    @DisplayName("메뉴는 현재 존재하는 메뉴 그룹에 포함되어 있지 않으면 예외가 발생한다.")
    void create_fail_menuGroup() {
        when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(false);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(NotExistsMenuGroupException.class);
    }

    @Test
    @DisplayName("메뉴의 상품 목록이 현재 존재하는 상품에 포함되어 있지 않으면 예외가 발생한다.")
    void create_fail_products() {
        when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(menuProduct1.getProductId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(NotExistsProductException.class);
    }

    @Test
    @DisplayName("현재 저장된 메뉴 목록을 확인할 수 있다. - 메뉴가 0개일 때")
    void list_success1() {
        menuService.list();

        assertAll(
                () -> verify(menuDao, times(1)).findAll(),
                () -> verify(menuProductDao, never()).findAllByMenuId(any())
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4})
    @DisplayName("현재 저장된 메뉴 목록을 확인할 수 있다. - 메뉴가 n개 일 때")
    void list_success2(int n) {
        when(menuDao.findAll()).thenReturn(getMenusByNTimes(n));
        menuService.list();

        assertAll(
                () -> verify(menuDao, times(1)).findAll(),
                () -> verify(menuProductDao, times(n)).findAllByMenuId(any())
        );
    }

    private List<Menu> getMenusByNTimes(int n) {
        List<Menu> menus = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            menus.add(menu);
        }
        return menus;
    }
}
