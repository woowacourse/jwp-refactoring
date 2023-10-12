package kitchenpos.application;

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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.MENU;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_생성한다() {
        //given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("중국식 메뉴 그룹");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Product product = new Product();
        product.setName("짜장면");
        product.setPrice(BigDecimal.valueOf(8000.0));
        Product savedProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(5L);

        long menuPrice = menuProduct.getQuantity() * savedProduct.getPrice().longValue();
        Menu menu = MENU("식사류", menuPrice, savedMenuGroup.getId(), List.of(menuProduct));

        //when
        Menu savedMenu = menuService.create(menu);

        //then
        assertSoftly(softly -> {
            softly.assertThat(menuDao.findById(savedMenu.getId())).isPresent();
            softly.assertThat(menuProductDao.findAllByMenuId(savedMenu.getId()))
                    .usingRecursiveComparison()
                    .isEqualTo(savedMenu.getMenuProducts());
        });

    }

    @Test
    void 메뉴_목록을_조회한다() {
        //given1
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("중국식 메뉴 그룹");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Product product = new Product();
        product.setName("짜장면");
        product.setPrice(BigDecimal.valueOf(8000.0));
        Product savedProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(5L);

        Menu menu = new Menu();
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setName("식사류");
        menu.setMenuProducts(List.of(menuProduct));
        menu.setPrice(BigDecimal.valueOf(menuProduct.getQuantity()).multiply(savedProduct.getPrice()));
        menuService.create(menu);

        List<Menu> menus = List.of(menu);

        //when
        List<Menu> savedMenus = menuService.list();

        //then
        assertThat(savedMenus)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(menus);
    }
}
