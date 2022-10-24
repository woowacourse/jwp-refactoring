package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;

@JdbcTest
class MenuProductDaoTest {

    private final MenuProductDao menuProductDao;
    private final MenuDao menuDao;
    private final ProductDao productDao;

    @Autowired
    public MenuProductDaoTest(final DataSource dataSource) {
        this.menuProductDao = new JdbcTemplateMenuProductDao(dataSource);
        this.menuDao = new JdbcTemplateMenuDao(dataSource);
        this.productDao = new JdbcTemplateProductDao(dataSource);
    }

    @Test
    @DisplayName("메뉴 상품을 저장한다")
    void save() {
        // given
        final Menu menu = new Menu();
        menu.setName("치킨치킨");
        menu.setPrice(new BigDecimal(3000));
        menu.setMenuGroupId(1L);
        final Menu savedMenu = menuDao.save(menu);

        final Product product = new Product();
        product.setName("듀오 치킨");
        product.setPrice(new BigDecimal(3000));
        final Product savedProduct = productDao.save(product);

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(savedMenu.getId());
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(2);

        // when
        final MenuProduct saved = menuProductDao.save(menuProduct);

        // then
        assertAll(
                () -> assertThat(saved.getSeq()).isNotNull(),
                () -> assertThat(saved.getMenuId()).isEqualTo(savedMenu.getId()),
                () -> assertThat(saved.getProductId()).isEqualTo(savedProduct.getId()),
                () -> assertThat(saved.getQuantity()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("존재하지 않는 메뉴로 메뉴 상품을 저장하려면 예외가 발생한다")
    void saveExceptionNotExistMenu() {
        // given
        final Product product = new Product();
        product.setName("듀오 치킨");
        product.setPrice(new BigDecimal(3000));
        final Product savedProduct = productDao.save(product);

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(-1L);
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(2);

        // when, then
        assertThatThrownBy(() -> menuProductDao.save(menuProduct))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("존재하지 않는 상품으로 메뉴 상품을 저장하려면 예외가 발생한다")
    void saveExceptionNotExistProduct() {
        // given
        final Menu menu = new Menu();
        menu.setName("치킨치킨");
        menu.setPrice(new BigDecimal(3000));
        menu.setMenuGroupId(1L);
        final Menu savedMenu = menuDao.save(menu);

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(savedMenu.getId());
        menuProduct.setProductId(-1L);
        menuProduct.setQuantity(2);

        // when, then
        assertThatThrownBy(() -> menuProductDao.save(menuProduct))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("id로 메뉴 상품을 조회한다")
    void findById() {
        // given
        final Menu menu = new Menu();
        menu.setName("치킨치킨");
        menu.setPrice(new BigDecimal(3000));
        menu.setMenuGroupId(1L);
        final Menu savedMenu = menuDao.save(menu);

        final Product product = new Product();
        product.setName("듀오 치킨");
        product.setPrice(new BigDecimal(3000));
        final Product savedProduct = productDao.save(product);

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(savedMenu.getId());
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(2);

        final MenuProduct saved = menuProductDao.save(menuProduct);

        // when
        final MenuProduct foundMenuProduct = menuProductDao.findById(saved.getSeq())
                .get();

        // then
        assertThat(foundMenuProduct).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("id로 메뉴 상품을 조회할 때 결과가 없다면 Optional.empty를 반환한다")
    void findByIdNotExist() {
        // when
        final Optional<MenuProduct> menuProduct = menuProductDao.findById(-1L);

        // then
        assertThat(menuProduct).isEmpty();
    }

    @Test
    @DisplayName("메뉴 id로 모든 메뉴 상품을 조회한다")
    void findAllByMenuId() {
        // given
        final Menu menu = new Menu();
        menu.setName("치킨치킨");
        menu.setPrice(new BigDecimal(3000));
        menu.setMenuGroupId(1L);
        final Menu savedMenu = menuDao.save(menu);

        final Product product = new Product();
        product.setName("듀오 치킨");
        product.setPrice(new BigDecimal(3000));
        final Product savedProduct = productDao.save(product);

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(savedMenu.getId());
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(2);

        final MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        // when
        final List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(savedMenu.getId());

        // then
        assertAll(
                () -> assertThat(menuProducts).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(menuProducts).extracting("seq")
                        .contains(savedMenuProduct.getSeq()),
                () -> assertThat(menuProducts).extracting("menuId")
                        .containsOnly(savedMenu.getId())
        );
    }
}
