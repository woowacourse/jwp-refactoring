package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
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

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    private MenuGroup menuGroup;
    private MenuProduct menuProduct;

    @BeforeEach
    private void setUp() {
        this.menuGroup = menuGroupDao.save(new MenuGroup("새로운_메뉴_그룹"));

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(4000));
        product.setName("제품_이름");
        Product savedProduct = productDao.save(product);

        this.menuProduct = new MenuProduct();
        menuProduct.setQuantity(2);
        menuProduct.setSeq(1L);
        menuProduct.setProductId(savedProduct.getId());
    }

    @DisplayName("Menu 생성을 확인한다.")
    @Test
    void createTest() {
        Menu menu = new Menu();
        menu.setName("새로운_메뉴");
        menu.setMenuProducts(Arrays.asList(menuProduct));
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuGroupId(menuGroup.getId());

        Menu result = menuService.create(menu);

        Menu savedMenu = menuDao.findById(result.getId()).get();
        assertThat(savedMenu.getId()).isEqualTo(result.getId());
        assertThat(savedMenu.getName()).isEqualTo(result.getName());
    }

    @DisplayName("생성 시 가격이 0원 미만이면 예외가 발생한다.")
    @Test
    void name() {
        Menu menu = new Menu();
        menu.setName("새로운_메뉴");
        menu.setMenuProducts(Arrays.asList(menuProduct));
        menu.setPrice(BigDecimal.valueOf(-1));
        menu.setMenuGroupId(menuGroup.getId());

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("price가 sum보다 크면 예외가 발생한다.")
    @Test
    void createSumExceptionTest() {
        Menu menu = new Menu();
        menu.setName("새로운_메뉴");
        menu.setMenuProducts(Arrays.asList(menuProduct));
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setMenuGroupId(menuGroup.getId());

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }
}