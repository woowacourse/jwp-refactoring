package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.exception.MenuGroupNotExistException;
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
        MenuProductRequest menuProductRequest = new MenuProductRequest(PRODUCT_ID_1, MENU_PRODUCT_QUANTITY_1);
        MenuCreateRequest negativePriceMenuRequest =
            new MenuCreateRequest(MENU_NAME_1, -1L, MENU_GROUP_ID_1, Arrays.asList(menuProductRequest));

        assertThatThrownBy(() -> menuService.create(negativePriceMenuRequest))
            .isInstanceOf(InvalidPriceException.class);
    }

    @DisplayName("Menu 생성 예외 테스트: MenuGroup이 존재하지 않을때")
    @Test
    void createFailByNotExistMenuGroupTest() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(PRODUCT_ID_1, MENU_PRODUCT_QUANTITY_1);
        MenuCreateRequest notExistMenuGroupMenuRequest =
            new MenuCreateRequest(MENU_NAME_1, MENU_PRICE_1.getValue().longValue(), -1L, Arrays.asList(menuProductRequest));

        assertThatThrownBy(() -> menuService.create(notExistMenuGroupMenuRequest))
            .isInstanceOf(MenuGroupNotExistException.class);
    }

    @DisplayName("Menu 생성 예외 테스트: MenuProduct들의 가격 합보다 Menu의 가격이 클때")
    @Test
    void createFailByOverPriceMenuTest() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(PRODUCT_ID_1, MENU_PRODUCT_QUANTITY_1);
        MenuCreateRequest overPriceMenuRequest =
            new MenuCreateRequest(MENU_NAME_1, MENU_PRICE_1.getValue().longValue() + 1L, MENU_GROUP_ID_1, Arrays.asList(menuProductRequest));

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(PRODUCT_1));

        assertThatThrownBy(() -> menuService.create(overPriceMenuRequest))
            .isInstanceOf(InvalidPriceException.class);
    }

    @DisplayName("Menu 생성 성공 테스트")
    @Test
    void createTest() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(PRODUCT_ID_1, MENU_PRODUCT_QUANTITY_1);
        MenuCreateRequest menuCreateRequest =
            new MenuCreateRequest(MENU_NAME_1, MENU_PRICE_1.getValue().longValue(), MENU_GROUP_ID_1, Arrays.asList(menuProductRequest));

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(PRODUCT_1));
        given(menuDao.save(any())).willReturn(MENU_1);

        Menu savedMenu = menuService.create(menuCreateRequest);
        assertThat(savedMenu).usingRecursiveComparison().isEqualTo(MENU_1);
    }

    @DisplayName("Menu 목록 조회 테스트")
    @Test
    void listTest() {
        given(menuDao.findAll()).willReturn(Arrays.asList(MENU_1, MENU_2));

        List<Menu> menus = menuService.list();
        assertAll(
            () -> assertThat(menus).hasSize(2),
            () -> assertThat(menus.get(0)).usingRecursiveComparison().isEqualTo(MENU_1),
            () -> assertThat(menus.get(1)).usingRecursiveComparison().isEqualTo(MENU_2)
        );
    }
}