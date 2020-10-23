package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.fixture.TestFixture;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest extends TestFixture {

    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    public void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @DisplayName("Menu 생성 예외 테스트: Menu의 가격이 음수일때")
    @Test
    void createFailByNegativePriceTest() {
        Menu negativePriceMenu = new Menu();
        negativePriceMenu.setId(MENU_ID_1);
        negativePriceMenu.setName(MENU_NAME_1);
        negativePriceMenu.setPrice(new BigDecimal(-1));
        negativePriceMenu.setMenuGroupId(MENU_GROUP_ID_1);
        negativePriceMenu.setMenuProducts(MENU_PRODUCTS_1);

        assertThatThrownBy(() -> menuService.create(negativePriceMenu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu 생성 예외 테스트: MenuGroup이 존재하지 않을때")
    @Test
    void createFailByNotExistMenuGroupTest() {
        Menu notExistMenuGroupMenu = new Menu();
        notExistMenuGroupMenu.setId(MENU_ID_1);
        notExistMenuGroupMenu.setName(MENU_NAME_1);
        notExistMenuGroupMenu.setPrice(MENU_PRICE_1);
        notExistMenuGroupMenu.setMenuGroupId(-1L);
        notExistMenuGroupMenu.setMenuProducts(MENU_PRODUCTS_1);

        assertThatThrownBy(() -> menuService.create(notExistMenuGroupMenu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu 생성 예외 테스트: MenuProduct들의 가격 합보다 Menu의 가격이 클때")
    @Test
    void createFailByOverPriceMenuTest() {
        Menu overPriceMenu = new Menu();
        overPriceMenu.setId(MENU_ID_1);
        overPriceMenu.setName(MENU_NAME_1);
        overPriceMenu.setPrice(MENU_PRICE_1.plus(MathContext.DECIMAL32));
        overPriceMenu.setMenuGroupId(MENU_GROUP_ID_1);
        overPriceMenu.setMenuProducts(MENU_PRODUCTS_1);

        assertThatThrownBy(() -> menuService.create(overPriceMenu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu 생성 성공 테스트")
    @Test
    void createTest() {
        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(PRODUCT_1));
        given(menuDao.save(any())).willReturn(MENU_1);

        Menu savedMenu = menuService.create(MENU_1);
        assertThat(savedMenu).usingRecursiveComparison().isEqualTo(MENU_1);
    }

    @DisplayName("Menu 목록 조회 테스트")
    @Test
    void listTest() {
        given(menuDao.findAll()).willReturn(Arrays.asList(MENU_1, MENU_2));
        given(menuProductDao.findAllByMenuId(MENU_ID_1)).willReturn(MENU_PRODUCTS_1);
        given(menuProductDao.findAllByMenuId(MENU_ID_2)).willReturn(MENU_PRODUCTS_2);

        List<Menu> menus = menuService.list();
        assertAll(
            () -> assertThat(menus).hasSize(2),
            () -> assertThat(menus.get(0)).usingRecursiveComparison().isEqualTo(MENU_1),
            () -> assertThat(menus.get(1)).usingRecursiveComparison().isEqualTo(MENU_2)
        );
    }
}