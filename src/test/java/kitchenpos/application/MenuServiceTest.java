package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.*;
import static kitchenpos.fixture.MenuProductFixture.*;
import static kitchenpos.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        this.menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @DisplayName("1 개 이상의 등록된 상품으로 메뉴를 등록할 수 있다.")
    @Test
    void createTest() {
        when(menuGroupDao.existsById(anyLong())).thenReturn(true);
        when(productDao.findById(anyLong())).thenReturn(Optional.of(FRIED_CHICKEN));
        when(menuDao.save(any())).thenReturn(MENU1);
        when(menuProductDao.save(any())).thenReturn(MENU_PRODUCT1);

        assertAll(
                () -> assertThat(menuService.create(MENU1).getId()).isEqualTo(1L),
                () -> assertThat(menuService.create(MENU1).getMenuGroupId()).isEqualTo(2L),
                () -> assertThat(menuService.create(MENU1).getName()).isEqualTo("후라이드치킨"),
                () -> assertThat(menuService.create(MENU1).getPrice()).isEqualTo(
                        BigDecimal.valueOf(16000)),
                () -> assertThat(menuService.create(MENU1).getMenuProducts().size()).isEqualTo(1),
                () -> assertThat(
                        menuService.create(MENU1).getMenuProducts().get(0).getMenuId()).isEqualTo(
                        1L),
                () -> assertThat(
                        menuService.create(MENU1)
                                .getMenuProducts()
                                .get(0)
                                .getProductId()).isEqualTo(
                        1L),
                () -> assertThat(
                        menuService.create(MENU1).getMenuProducts().get(0).getQuantity()).isEqualTo(
                        1)
        );
    }

    @DisplayName("가격이 null일 경우 에러.")
    @Test
    void notCreateMenu_when_priceNull() {
        assertThatThrownBy(() -> menuService.create(MENU_PRICE_NULL))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 0보다 작을 경우 에러.")
    @Test
    void notCreateMenu_when_UnderZeroPrice() {
        assertThatThrownBy(() -> menuService.create(MENU_PRICE_NEGATIVE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴는 특정 메뉴 그룹에 속하지 않을 경우 에러.")
    @Test
    void notCreateMenu_when_notExist() {
        when(menuGroupDao.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> menuService.create(MENU1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        when(menuDao.findAll()).thenReturn(MENUS);
        when(menuProductDao.findAllByMenuId(1L)).thenReturn(MENU1.getMenuProducts());
        when(menuProductDao.findAllByMenuId(2L)).thenReturn(MENU2.getMenuProducts());

        assertAll(
                () -> assertThat(menuService.list().size()).isEqualTo(2),
                () -> assertThat(menuService.list().get(0).getId()).isEqualTo(1L),
                () -> assertThat(menuService.list().get(1).getId()).isEqualTo(2L)
        );
    }
}