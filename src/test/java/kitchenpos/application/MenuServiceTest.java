package kitchenpos.application;

import static java.lang.Integer.MAX_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sun.tools.javac.util.List;
import java.math.BigDecimal;
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
    void 메뉴를_생성한다() {
        Menu menu = createMenu(1L, 0, List.nil());

        Menu savedMenu = menuService.create(menu);

        assertThat(menuDao.findById(savedMenu.getId())).isPresent();
    }

    @Test
    void 메뉴를_생성할때_price_예외를_발생한다() {
        Menu menu = createMenu(1L, -1, List.nil());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_생성할때_메뉴그룹아이디_예외를_발생한다() {
        Menu menu = createMenu(0L, 0, List.nil());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_생성할때_product총가격보다_menu가격이높으면_예외를_발생한다() {
        Menu menu = createMenu(1L, MAX_VALUE, List.nil());

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menu.setMenuProducts(List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_리스트를_반환한다() {
        Menu menu = createMenu(2L, 0, List.nil());

        int beforeSize = menuService.list().size();
        menuService.create(menu);

        assertThat(menuService.list().size()).isEqualTo(beforeSize + 1);
    }

    private Menu createMenu(Long menuGroupId, int price, List<MenuProduct> products) {
        Menu menu = new Menu();
        menu.setName("test");
        menu.setMenuGroupId(menuGroupId);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuProducts(products);
        return menu;
    }
}
