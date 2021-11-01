package kitchenpos.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.generator.MenuGenerator;
import kitchenpos.generator.MenuGroupGenerator;
import kitchenpos.generator.ProductGenerator;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MenuApiTest extends ApiTest {

    private static final String BASE_URL = "/api/menus";

    @Autowired
    private JdbcTemplateMenuGroupDao menuGroupDao;

    @Autowired
    private JdbcTemplateProductDao productDao;

    @Autowired
    private JdbcTemplateMenuDao menuDao;

    @Autowired
    private JdbcTemplateMenuProductDao menuProductDao;

    private MenuGroup menuGroup;
    private List<Product> products;
    private List<Menu> menus;
    private List<MenuProduct> menuProducts;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();
        products = new ArrayList<>();
        menus = new ArrayList<>();
        menuProducts = new ArrayList<>();

        menuGroup = menuGroupDao.save(MenuGroupGenerator.newInstance("두마리메뉴"));

        products.add(productDao.save(ProductGenerator.newInstance("후라이드", 16000)));
        products.add(productDao.save(ProductGenerator.newInstance("양념치킨", 16000)));

        menus.add(menuDao.save(MenuGenerator.newInstance(
            "후라이드치킨",
            16000,
            menuGroup.getId(),
            Collections.emptyList()
        )));
        menus.add(menuDao.save(MenuGenerator.newInstance(
            "양념치킨",
            16000,
            menuGroup.getId(),
            Collections.emptyList()
        )));

        menuProducts.add(menuProductDao.save(MenuGenerator.newMenuProduct(
            menus.get(0).getId(),
            products.get(0).getId(),
            1)
        ));
        menuProducts.add(menuProductDao.save(MenuGenerator.newMenuProduct(
            menus.get(1).getId(),
            products.get(1).getId(),
            1)
        ));
    }

    @DisplayName("메뉴 등록")
    @Test
    void postMenu() {
        MenuProduct menuProduct = MenuGenerator.newMenuProduct(products.get(0).getId(), 2);
        Menu request = MenuGenerator.newInstance("후라이드+후라이드", 19000, menuGroup.getId(),
            Collections.singletonList(menuProduct));

        ResponseEntity<Menu> responseEntity = testRestTemplate.postForEntity(BASE_URL, request,
            Menu.class);
        Menu response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getId()).isEqualTo(response.getMenuProducts().get(0).getMenuId());
        assertThat(response).usingComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR,
                BigDecimal.class)
            .usingRecursiveComparison()
            .ignoringFields("id", "menuProducts.seq", "menuProducts.menuId")
            .isEqualTo(request);
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void getMenus() {
        ResponseEntity<Menu[]> responseEntity = testRestTemplate.getForEntity(
            BASE_URL,
            Menu[].class
        );
        Menu[] response = responseEntity.getBody();

        List<Menu> expected = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            Menu menu = new Menu();
            menu.setId(menus.get(i).getId());
            menu.setName(menus.get(i).getName());
            menu.setPrice(menus.get(i).getPrice());
            menu.setMenuGroupId(menuGroup.getId());
            menu.setMenuProducts(Collections.singletonList(menuProducts.get(i)));
            expected.add(menu);
        }

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).hasSize(2);
        assertThat(response).usingRecursiveFieldByFieldElementComparator()
            .containsAll(expected);
    }
}
