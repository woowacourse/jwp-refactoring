package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sun.tools.javac.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Test
    void create() {
        Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(0));
        menu.setMenuProducts(new ArrayList<>());

        Menu savedMenu = menuService.create(menu);

        assertThat(menuDao.findById(savedMenu.getId())).isPresent();
    }

    @Test
    void create_priceException() {
        Menu menu = new Menu();
        menu.setPrice(null);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_menuGroupException() {
        Menu menu = new Menu();
        menu.setMenuGroupId(0L);
        menu.setPrice(BigDecimal.valueOf(0));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_productTotalPriceException() {
        Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(Long.MAX_VALUE));

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menu.setMenuProducts(List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        Menu menu = new Menu();
        menu.setMenuGroupId(2L);
        menu.setPrice(BigDecimal.valueOf(0));
        menu.setMenuProducts(new ArrayList<>());

        int beforeSize = menuService.list().size();
        menuService.create(menu);

        assertThat(menuService.list().size()).isEqualTo(beforeSize + 1);
    }
}
