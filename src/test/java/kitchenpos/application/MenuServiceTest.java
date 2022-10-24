package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    private Menu menu;

    @BeforeEach
    void setUp(){
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("A"));
        Product product = productDao.save(new Product("제육볶음", BigDecimal.TEN));

        menu = menuDao.save(new Menu("신메뉴", BigDecimal.ONE, menuGroup.getId(), List.of()));
        MenuProduct menuProduct = menuProductDao.save(new MenuProduct(menu.getId(), product.getId(), 1));
        menu.setMenuProducts(List.of(menuProduct));
    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void create() {
        new MenuProduct();
        Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu.getId()).isNotNull();
    }

    @Test
    void list() {

        assertThat(menuService.list()).hasSize(1);
    }
}
