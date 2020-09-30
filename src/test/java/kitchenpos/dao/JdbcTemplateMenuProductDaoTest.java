package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/truncate.sql")
@SpringBootTest
class JdbcTemplateMenuProductDaoTest {
    private static final String INSERT_PRODUCT = "insert into product (id, name, price) values (:id, :name, :price)";
    private static final String INSERT_MENU_GROUP = "insert into menu_group (id, name) values (:id, :name)";
    private static final String INSERT_MENU = "insert into menu (id, name, price, menu_group_id) " +
            "values (:id, :name, :price, :menu_group_id)";
    private static final String DELETE_MENU_PRODUCT = "delete from menu_product where seq in (:seqs)";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;

    private List<Long> menuProductSeqs;

    @BeforeEach
    void setUp() {
        menuProductSeqs = new ArrayList<>();
        saveProducts();
        saveMenus();
    }

    private void saveProducts() {
        Map<String, Object> friedProduct = createProduct(1L, "후라이드", BigDecimal.valueOf(16000));
        Map<String, Object> sourceProduct = createProduct(2L, "양념치킨", BigDecimal.valueOf(16000));
        namedParameterJdbcTemplate.update(INSERT_PRODUCT, friedProduct);
        namedParameterJdbcTemplate.update(INSERT_PRODUCT, sourceProduct);
    }

    private Map<String, Object> createProduct(long id, String name, BigDecimal price) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("name", name);
        params.put("price", price);
        return params;
    }

    private void saveMenus() {
        Map<String, Object> menuGroupParams = new HashMap<>();
        menuGroupParams.put("id", 1L);
        menuGroupParams.put("name", "한마리메뉴");

        Map<String, Object> friedMenu = createMenu(1L, "후라이드", BigDecimal.valueOf(16000));
        Map<String, Object> sourceMenu = createMenu(2L, "양념치킨", BigDecimal.valueOf(16000));

        namedParameterJdbcTemplate.update(INSERT_MENU_GROUP, menuGroupParams);
        namedParameterJdbcTemplate.update(INSERT_MENU, friedMenu);
        namedParameterJdbcTemplate.update(INSERT_MENU, sourceMenu);
    }

    private Map<String, Object> createMenu(long id, String name, BigDecimal price) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("name", name);
        params.put("price", price);
        params.put("menu_group_id", 1L);
        return params;
    }

    @DisplayName("메뉴 상품 저장")
    @Test
    void saveTest() {
        MenuProduct menuProduct = createMenuProduct(1L, 1L, 1L);

        MenuProduct savedMenuProduct = jdbcTemplateMenuProductDao.save(menuProduct);
        menuProductSeqs.add(savedMenuProduct.getSeq());

        assertAll(
                () -> assertThat(savedMenuProduct.getSeq()).isNotNull(),
                () -> assertThat(savedMenuProduct.getMenuId()).isEqualTo(1L),
                () -> assertThat(savedMenuProduct.getProductId()).isEqualTo(1L),
                () -> assertThat(savedMenuProduct.getQuantity()).isEqualTo(1L)
        );
    }

    @DisplayName("seq 값에 해당하는 메뉴 상품 반환")
    @Test
    void findByIdTest() {
        MenuProduct menuProduct = createMenuProduct(1L, 1L, 1L);
        MenuProduct savedMenuProduct = jdbcTemplateMenuProductDao.save(menuProduct);

        MenuProduct findMenuProduct = jdbcTemplateMenuProductDao.findById(savedMenuProduct.getSeq()).get();
        menuProductSeqs.add(savedMenuProduct.getSeq());

        assertAll(
                () -> assertThat(findMenuProduct.getSeq()).isEqualTo(savedMenuProduct.getSeq()),
                () -> assertThat(findMenuProduct.getMenuId()).isEqualTo(savedMenuProduct.getMenuId()),
                () -> assertThat(findMenuProduct.getProductId()).isEqualTo(savedMenuProduct.getProductId()),
                () -> assertThat(findMenuProduct.getQuantity()).isEqualTo(savedMenuProduct.getQuantity())
        );
    }

    @DisplayName("저장된 모든 메뉴 상품 반환")
    @Test
    void findAllTest() {
        MenuProduct friedMenuProduct = createMenuProduct(1L, 1L, 1L);
        MenuProduct sourceMenuProduct = createMenuProduct(2L, 2L, 1L);
        jdbcTemplateMenuProductDao.save(friedMenuProduct);
        jdbcTemplateMenuProductDao.save(sourceMenuProduct);

        List<MenuProduct> allMenuProducts = jdbcTemplateMenuProductDao.findAll();
        allMenuProducts.forEach(menuProduct -> menuProductSeqs.add(menuProduct.getSeq()));

        assertThat(allMenuProducts).hasSize(2);
    }

    @DisplayName("메뉴 아이디에 해당하는 메뉴 상품 반환")
    @Test
    void findAllByMenuIdTest() {
        MenuProduct menuProductWithQuantityOne = createMenuProduct(1L, 1L, 1L);
        MenuProduct menuProductWithQuantityTwo = createMenuProduct(1L, 1L, 2L);
        jdbcTemplateMenuProductDao.save(menuProductWithQuantityOne);
        jdbcTemplateMenuProductDao.save(menuProductWithQuantityTwo);

        List<MenuProduct> menuProductsWithMenuId = jdbcTemplateMenuProductDao.findAllByMenuId(1L);
        menuProductsWithMenuId.forEach(menuProduct -> menuProductSeqs.add(menuProduct.getSeq()));

        assertThat(menuProductsWithMenuId).hasSize(2);
    }

    private MenuProduct createMenuProduct(Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    @AfterEach
    void tearDown() {
        Map<String, Object> params = Collections.singletonMap("seqs", menuProductSeqs);
        namedParameterJdbcTemplate.update(DELETE_MENU_PRODUCT, params);
    }
}
