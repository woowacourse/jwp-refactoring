package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Test
    void 메뉴_정상_생성() {
        // given
        Menu menu = 새_메뉴();

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        Optional<Menu> actual = menuDao.findById(savedMenu.getId());
        assertThat(actual).isNotEmpty();
    }

    private Menu 새_메뉴(){
        Menu menu = new Menu();
        menu.setName("떡볶이");
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setMenuGroupId(메뉴_그룹_생성().getId());
        menu.setMenuProducts(메뉴_상품_목록_생성());
        return menu;
    }

    private MenuGroup 메뉴_그룹_생성() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("분식");
        return menuGroupDao.save(menuGroup);
    }

    private List<MenuProduct> 메뉴_상품_목록_생성() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(null);
        menuProduct.setProductId(새_상품().getId());
        menuProduct.setQuantity(3);

        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);
        return menuProducts;
    }

    private Product 새_상품() {
        Product product = new Product();
        product.setName("떡볶이");
        product.setPrice(BigDecimal.valueOf(8000));
        return productDao.save(product);
    }
}
