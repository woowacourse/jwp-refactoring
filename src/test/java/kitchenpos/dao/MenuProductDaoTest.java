package kitchenpos.dao;

import static java.util.Collections.emptyList;
import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.MenuProductFixture.createMenuProduct;
import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
public class MenuProductDaoTest {
    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    private MenuGroup menuGroup;
    private Product product;
    private Menu menu;

    @BeforeEach
    void setup() {
        menuGroup = menuGroupDao.save(createMenuGroup(null, "메뉴그룹"));
        product = productDao.save(createProduct(null, "상품", 1000L));
        menu = menuDao.save(createMenu(null, "메뉴", 3000L, menuGroup.getId(), emptyList()));
    }

    @DisplayName("메뉴 상품을 저장할 수 있다.")
    @Test
    void save() {
        MenuProduct menuProduct = createMenuProduct(null, menu.getId(), product.getId(), 3L);

        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        assertAll(
            () -> assertThat(savedMenuProduct.getSeq()).isNotNull(),
            () -> assertThat(savedMenuProduct).isEqualToIgnoringGivenFields(menuProduct, "seq")
        );
    }

    @DisplayName("메뉴 상품 아이디로 메뉴 상품을 조회할 수 있다.")
    @Test
    void findById() {
        MenuProduct menuProduct = menuProductDao
            .save(createMenuProduct(null, menu.getId(), product.getId(), 3L));

        Optional<MenuProduct> foundMenuProduct = menuProductDao.findById(menuProduct.getSeq());

        assertThat(foundMenuProduct.get()).isEqualToComparingFieldByField(menuProduct);
    }

    @DisplayName("메뉴 상품 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        List<MenuProduct> savedMenuProducts = Arrays.asList(
            menuProductDao.save(createMenuProduct(null, menu.getId(), product.getId(), 3L)),
            menuProductDao.save(createMenuProduct(null, menu.getId(), product.getId(), 3L)),
            menuProductDao.save(createMenuProduct(null, menu.getId(), product.getId(), 3L))
        );

        List<MenuProduct> allMenuProducts = menuProductDao.findAll();

        assertThat(allMenuProducts).usingFieldByFieldElementComparator()
            .containsAll(savedMenuProducts);
    }

    @DisplayName("메뉴에 속하는 메뉴 상품 목록을 조회할 수 있다.")
    @Test
    void findAllByMenuId() {
        Menu secondMenu = menuDao
            .save(createMenu(null, "메뉴", 3000L, menuGroup.getId(), emptyList()));
        MenuProduct menuProduct = menuProductDao
            .save(createMenuProduct(null, secondMenu.getId(), product.getId(), 3L));
        menuProductDao.save(createMenuProduct(null, menu.getId(), product.getId(), 3L));
        menuProductDao.save(createMenuProduct(null, menu.getId(), product.getId(), 3L));

        List<MenuProduct> menuProductsByMenuId = menuProductDao.findAllByMenuId(secondMenu.getId());

        assertAll(
            () -> assertThat(menuProductsByMenuId).hasSize(1),
            () -> assertThat(menuProductsByMenuId).usingFieldByFieldElementComparator()
                .contains(menuProduct)
        );
    }
}
