package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.application.MenuService;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.dto.MenuRequest;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MenuServiceTest extends ServiceTest {

    @Mock
    private JdbcTemplateMenuDao menuDao;
    @Mock
    private JdbcTemplateMenuGroupDao menuGroupDao;
    @Mock
    private JdbcTemplateProductDao productDao;
    @Mock
    private JdbcTemplateMenuProductDao menuProductDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(ProductFixture.product()));
        when(menuDao.save(any())).thenReturn(MenuFixture.menu());
        when(menuProductDao.save(any())).thenReturn(MenuProductFixture.menuProduct());
        when(menuDao.findById(any())).thenReturn(Optional.of(MenuFixture.menu()));
        MenuRequest menuRequest = MenuFixture.menuRequest();

        menuService.create(menuRequest);
    }

    @DisplayName("메뉴 생성시 가격이 null일 수 없다")
    @Test()
    void priceNull() {
        MenuRequest menuRequest = new MenuRequest("name", null, 0L, Arrays.asList(MenuProductFixture.menuProduct()));

        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성시 가격이 0보다 작을 수 없다.")
    @Test()
    void priceMinus() {
        MenuRequest menuRequest = new MenuRequest("name", BigDecimal.valueOf(-1), 0L,
                Arrays.asList(MenuProductFixture.menuProduct()));

        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 속한 메뉴 그룹이 이미 등록되어 있을 경우 생성시 불가능하다.")
    @Test()
    void duplicateMenu() {
        when(menuGroupDao.existsById(any())).thenReturn(false);
        MenuRequest menuRequest = MenuFixture.menuRequest();

        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 적힌 가격보다 실제 메뉴에 속한 상품의 가격의 합이 적을경우 생성할 수 없다.")
    @Test()
    void invalidMenuPrice() {
        when(menuGroupDao.existsById(any())).thenReturn(false);
        MenuRequest menuRequest = new MenuRequest("name", BigDecimal.valueOf(100), 0L,
                Arrays.asList(MenuProductFixture.menuProduct()));

        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴 조회")
    @Test()
    void findAll() {
        when(menuDao.findAll()).thenReturn(Arrays.asList(MenuFixture.menu()));

        menuService.list();
    }

}
