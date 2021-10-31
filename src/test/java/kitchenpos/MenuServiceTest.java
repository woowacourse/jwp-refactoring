package kitchenpos;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.application.MenuService;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
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

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(ProductFixture.product()));
        Menu menu = new Menu();
        menu.setName("메뉴 1");
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setMenuProducts(Arrays.asList(new MenuProduct()));

        menuService.create(menu);
    }

}
