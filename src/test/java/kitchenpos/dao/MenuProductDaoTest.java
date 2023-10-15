package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class MenuProductDaoTest {

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    private Menu menu;
    private Product product;


    @BeforeEach
    void setUp() {
        MenuGroup menuGroupEntity = new MenuGroup();
        menuGroupEntity.setName("샐러드");
        MenuGroup menuGroup = menuGroupDao.save(menuGroupEntity);

        Menu menuEntity = new Menu();
        menuEntity.setName("닭가슴살");
        menuEntity.setPrice(BigDecimal.valueOf(1_000));
        menuEntity.setMenuGroupId(menuGroup.getId());
        menu = menuDao.save(menuEntity);

        Product productEntity = new Product();
        productEntity.setName("닭가슴살 볼");
        productEntity.setPrice(BigDecimal.valueOf(3_000));
        product = productDao.save(productEntity);
    }

    @Test
    void 메뉴_제품_엔티티를_저장한다() {
        MenuProduct menuProductEntity = createMenuProduct();

        MenuProduct savedMenuProduct = menuProductDao.save(menuProductEntity);

        assertThat(savedMenuProduct.getSeq()).isPositive();
    }

    @Test
    void 메뉴_제품_엔티티를_조회한다() {
        MenuProduct menuProductEntity = createMenuProduct();
        MenuProduct savedMenuProduct = menuProductDao.save(menuProductEntity);

        assertThat(menuProductDao.findById(savedMenuProduct.getSeq())).isPresent();
    }

    @Test
    void 모든_메뉴_제품_엔티티를_조회한다() {
        MenuProduct menuProductEntityA = createMenuProduct();
        MenuProduct menuProductEntityB = createMenuProduct();
        MenuProduct savedMenuProductA = menuProductDao.save(menuProductEntityA);
        MenuProduct savedMenuProductB = menuProductDao.save(menuProductEntityB);

        List<MenuProduct> menuProducts = menuProductDao.findAll();

        assertThat(menuProducts).usingRecursiveFieldByFieldElementComparatorOnFields("seq")
                .contains(savedMenuProductA, savedMenuProductB);
    }

    @Test
    void 메뉴와_일치하는_모든_메뉴_제품_엔티티를_조회한다() {
        MenuProduct menuProductEntityA = createMenuProduct();
        MenuProduct menuProductEntityB = createMenuProduct();
        MenuProduct savedMenuProductA = menuProductDao.save(menuProductEntityA);
        MenuProduct savedMenuProductB = menuProductDao.save(menuProductEntityB);

        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menu.getId());

        assertThat(menuProducts).usingRecursiveFieldByFieldElementComparatorOnFields("seq")
                .contains(savedMenuProductA, savedMenuProductB);
    }

    private MenuProduct createMenuProduct() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(10);
        return menuProduct;
    }
}
