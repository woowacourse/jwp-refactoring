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
import java.util.Collections;

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
        this.menuGroup = menuGroupDao.save(new MenuGroup("피자"));

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(4000));
        product.setName("감자");
        Product savedProduct = productDao.save(product);

        this.menuProduct = new MenuProduct();
        menuProduct.setQuantity(2);
        menuProduct.setSeq(1L);
        menuProduct.setProduct(savedProduct);
    }

    @DisplayName("Menu 생성을 확인한다.")
    @Test
    void createTest() {
        Menu menu = create("감자_피자", 1000L, menuGroup);

        Menu result = menuService.create(menu, Collections.singletonList(menuProduct));

        Menu savedMenu = menuDao.findById(result.getId()).get();
        assertThat(savedMenu.getName()).isEqualTo(menu.getName());
    }

    @DisplayName("생성 시 menu group를 보유해야 한다.")
    @Test
    void createExceptionTest_noGroupId() {
        Menu menu = create("감자_피자", 10000L, null);

        assertThatThrownBy(() -> menuService.create(menu, Collections.singletonList(menuProduct)))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("생성 시 가격이 0원 미만이면 예외가 발생한다.")
    @Test
    void createExceptionTest_priceIsZero() {
        Menu menu = create("감자_피자", -1L, menuGroup);

        assertThatThrownBy(() -> menuService.create(menu, Collections.singletonList(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("price가 제품의 sum보다 크면 예외가 발생한다.")
    @Test
    void createSumExceptionTest() {
        Menu menu = create("감자_피자", 10000L, menuGroup);

        assertThatThrownBy(() -> menuService.create(menu, Collections.singletonList(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public Menu create(String name, Long price, MenuGroup menuGroup) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroup(menuGroup);
        return menu;
    }
}