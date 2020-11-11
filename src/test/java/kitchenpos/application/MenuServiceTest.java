package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    private static Stream<List<MenuProduct>> noMenuProducts() {
        return Stream.of(null, new ArrayList<>());
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        Product savedChicken1 = productDao.save(new Product("간장치킨", 10000L));
        Product savedChicken2 = productDao.save(new Product("허니콤보치킨", 15000L));

        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("한마리 메뉴"));

        MenuProduct menuProduct1 = new MenuProduct(savedChicken1.getId(), 2);
        MenuProduct menuProduct2 = new MenuProduct(savedChicken2.getId(), 3);

        Menu menu = new Menu("간장+허니", 60000L, savedMenuGroup.getId(), Arrays.asList(menuProduct1, menuProduct2));
        Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(60000L));
    }

    @DisplayName("메뉴에 속한 상품 금액의 합은 메뉴의 가격보다 크거나 같아야 한다.")
    @Test
    void createException3() {
        Product savedChicken1 = productDao.save(new Product("간장치킨", 10000L));
        Product savedChicken2 = productDao.save(new Product("허니콤보치킨", 15000L));

        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("한마리 메뉴"));

        MenuProduct menuProduct1 = new MenuProduct(savedChicken1.getId(), 1);
        MenuProduct menuProduct2 = new MenuProduct(savedChicken2.getId(), 1);

        Menu menu = new Menu("간장+허니", 30000L, savedMenuGroup.getId(), Arrays.asList(menuProduct1, menuProduct2));
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 금액의 합(%d)이 메뉴의 가격(%d)보다 작습니다.", 25000L, 30000L);
    }

    @DisplayName("메뉴 그룹 없이 메뉴를 생성할 수 없다.")
    @Test
    void createException4() {
        Product savedChicken1 = productDao.save(new Product("간장치킨", 10000L));
        Product savedChicken2 = productDao.save(new Product("허니콤보치킨", 15000L));

        MenuProduct menuProduct1 = new MenuProduct(savedChicken1.getId(), 1);
        MenuProduct menuProduct2 = new MenuProduct(savedChicken2.getId(), 1);

        Menu menu = new Menu("간장+허니", 30000L, null, Arrays.asList(menuProduct1, menuProduct2));
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹을 선택해주세요.");
    }


    @Test
    void list() {
        Product savedChicken1 = productDao.save(new Product("간장치킨", 10000L));
        Product savedChicken2 = productDao.save(new Product("허니콤보치킨", 15000L));

        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("한마리 메뉴"));

        MenuProduct menuProduct1 = new MenuProduct(savedChicken1.getId(), 2);
        MenuProduct menuProduct2 = new MenuProduct(savedChicken2.getId(), 3);

        Menu menu = new Menu("간장+허니", 60000L, savedMenuGroup.getId(), Arrays.asList(menuProduct1, menuProduct2));
        menuService.create(menu);
        menuService.create(menu);

        List<Menu> menus = menuDao.findAll();

        assertThat(menus).hasSize(2);
    }

    @AfterEach
    void tearDown() {
        menuProductDao.deleteAll();
        menuDao.deleteAll();
        menuGroupDao.deleteAll();
        productDao.deleteAll();
    }
}