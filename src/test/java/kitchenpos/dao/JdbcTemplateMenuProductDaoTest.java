package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuFixture.createMenuProduct;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DaoTest
class JdbcTemplateMenuProductDaoTest {
    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private Long productId;
    private Long menuId;
    private Long menuGroupId;

    @BeforeEach
    void setUp() {
        productId = productDao.save(createProduct(null, "강정치킨", BigDecimal.ONE)).getId();
        menuGroupId = menuGroupDao.save(createMenuGroup(null, "추천메뉴")).getId();
        menuId = menuDao.save(createMenu(null, "후라이드+후라이드", BigDecimal.ONE, menuGroupId)).getId();
    }

    @Test
    @DisplayName("메뉴 상품 엔티티를 저장하면 seq가 부여되며 저장된다")
    void insert() {
        MenuProduct menuProduct = createMenuProduct(null, productId, 1, menuId);

        MenuProduct result = menuProductDao.save(menuProduct);

        assertAll(
                () -> assertThat(result).isEqualToIgnoringGivenFields(menuProduct, "seq"),
                () -> assertThat(result.getSeq()).isNotNull()
        );
    }


    @Test
    @DisplayName("존재하는 seq로 엔티티를 조회하면 저장되어있는 엔티티가 조회된다")
    void findExist() {
        MenuProduct menuProduct = createMenuProduct(null, productId, 1, menuId);
        MenuProduct persisted = menuProductDao.save(menuProduct);

        MenuProduct result = menuProductDao.findById(persisted.getSeq()).get();

        assertThat(result).isEqualToComparingFieldByField(persisted);
    }

    @Test
    @DisplayName("저장되어있지 않은 엔티티를 조회하면 빈 optional 객체가 반환된다")
    void findNotExist() {
        assertThat(menuProductDao.findById(0L)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("모든 엔티티를 조회하면 저장되어 있는 엔티티들이 반환된다")
    void findAll() {
        menuProductDao.save(createMenuProduct(null, productId, 1, menuId));
        menuProductDao.save(createMenuProduct(null, productId, 2, menuId));
        menuProductDao.save(createMenuProduct(null, productId, 3, menuId));

        assertThat(menuProductDao.findAll()).hasSize(3);
    }

    @Test
    @DisplayName("menuId로 엔티티를 조회하면 저장되어있는 엔티티가 조회된다")
    void findByMenuId() {
        Long otherMenuId = menuDao.save(createMenu(null, "볼케이노+케이준감자", BigDecimal.ONE, menuGroupId)).getId();

        menuProductDao.save(createMenuProduct(null, productId, 1, menuId));
        menuProductDao.save(createMenuProduct(null, productId, 2, menuId));
        menuProductDao.save(createMenuProduct(null, productId, 2, otherMenuId));

        List<MenuProduct> result = menuProductDao.findAllByMenuId(menuId);

        assertThat(result).hasSize(2);
    }
}