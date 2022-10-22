package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    MenuDao menuDao;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    MenuProductDao menuProductDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    MenuService sut;

    @Test
    void throwException_WhenPriceNull() {
        // given
        Menu menu = new Menu();
        menu.setName("강정치킨");
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(new ArrayList<>());

        // when && then
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwException_WhenPriceNegative() {
        // given
        Menu menu = new Menu();
        menu.setName("강정치킨");
        menu.setPrice(BigDecimal.valueOf(-1L));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(new ArrayList<>());

        // when && then
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwException_WhenGivenNonExistMenuGroupId() {
        // given
        Menu menu = new Menu();
        menu.setName("강정치킨");
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(1000L));
        menu.setMenuProducts(new ArrayList<>());

        // when && when
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwException_WhenGivenNonExistMenuProductId() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("치킨");
        Long menuGroupId = menuGroupDao.save(menuGroup).getId();

        Menu menu = new Menu();
        menu.setName("강정치킨");
        menu.setMenuGroupId(menuGroupId);
        menu.setPrice(BigDecimal.valueOf(1000L));

        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(0L);
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);

        // when && when
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwException_WhenMenuPriceAndSumOfMenuProductPrice_NotMatch() {
        // given
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(1000L));
        Long productId = productDao.save(product).getId();

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(2);
        List<MenuProduct> menuProducts = List.of(menuProduct);

        Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(3000L));
        menu.setMenuProducts(menuProducts);

        // when && then
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void saveMenu() {
        // given
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(1000L));
        Long productId = productDao.save(product).getId();

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("치킨");
        Long menuGroupId = menuGroupDao.save(menuGroup).getId();

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(2);
        List<MenuProduct> menuProducts = List.of(menuProduct);

        Menu menu = new Menu();
        menu.setName("강정치킨");
        menu.setMenuGroupId(menuGroupId);
        menu.setPrice(BigDecimal.valueOf(2000L));
        menu.setMenuProducts(menuProducts);

        // when
        Menu actual = sut.create(menu);

        // then
        assertThat(actual).isNotNull();
    }
}
