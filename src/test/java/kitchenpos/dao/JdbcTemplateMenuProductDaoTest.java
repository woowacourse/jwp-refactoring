package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

@DisplayName("MenuProduct Dao 테스트")
class JdbcTemplateMenuProductDaoTest extends JdbcTemplateDaoTest {

    private JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;
    private JdbcTemplateMenuDao jdbcTemplateMenuDao;
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;
    private JdbcTemplateProductDao jdbcTemplateProductDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateMenuProductDao = new JdbcTemplateMenuProductDao(dataSource);
        jdbcTemplateMenuDao = new JdbcTemplateMenuDao(dataSource);
        jdbcTemplateMenuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
        jdbcTemplateProductDao = new JdbcTemplateProductDao(dataSource);
    }

    @DisplayName("MenuProduct를 저장할 때")
    @Nested
    class Save {

        @DisplayName("정상적인 MenuProduct는 저장에 성공한다.")
        @Test
        void success() {
            // given
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("추천 메뉴"));
            Menu menu = jdbcTemplateMenuDao.save(Menu를_생성한다("더블 새우 버거 세트", 5_600, menuGroup.getId()));
            Product product = jdbcTemplateProductDao.save(Product를_생성한다("더블 새우 버거", 4_000));
            MenuProduct menuProduct = MenuProduct를_생성한다(menu.getId(), product.getId(), 5);

            // when
            MenuProduct savedMenuProduct = jdbcTemplateMenuProductDao.save(menuProduct);

            // then
            assertThat(jdbcTemplateMenuProductDao.findById(savedMenuProduct.getSeq())).isPresent();
            assertThat(savedMenuProduct).usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(menuProduct);
        }

        @DisplayName("menuId가 Null인 경우 예외가 발생한다.")
        @Test
        void menuIdNullException() {
            // given
            Product product = jdbcTemplateProductDao.save(Product를_생성한다("더블 새우 버거", 4_000));
            MenuProduct menuProduct = MenuProduct를_생성한다(null, product.getId(), 5);

            // when, then
            assertThatThrownBy(() -> jdbcTemplateMenuProductDao.save(menuProduct))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }

        @DisplayName("productId가 Null인 경우 예외가 발생한다.")
        @Test
        void productIdNullException() {
            // given
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("추천 메뉴"));
            Menu menu = jdbcTemplateMenuDao.save(Menu를_생성한다("더블 새우 버거 세트", 5_600, menuGroup.getId()));
            MenuProduct menuProduct = MenuProduct를_생성한다(menu.getId(), null, 5);

            // when, then
            assertThatThrownBy(() -> jdbcTemplateMenuProductDao.save(menuProduct))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @DisplayName("ID를 통해 MenuProduct를 조회할 때")
    @Nested
    class FindById {

        @DisplayName("ID가 존재한다면 MenuProduct 조회에 성공한다.")
        @Test
        void present() {
            // given
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("추천 메뉴"));
            Menu menu = jdbcTemplateMenuDao.save(Menu를_생성한다("더블 새우 버거 세트", 5_600, menuGroup.getId()));
            Product product = jdbcTemplateProductDao.save(Product를_생성한다("더블 새우 버거", 4_000));
            MenuProduct savedMenuProduct = jdbcTemplateMenuProductDao.save(MenuProduct를_생성한다(menu.getId(), product.getId(), 5));

            // when
            Optional<MenuProduct> foundMenuProduct = jdbcTemplateMenuProductDao.findById(savedMenuProduct.getSeq());

            // then
            assertThat(foundMenuProduct).isPresent();
            assertThat(foundMenuProduct.get()).usingRecursiveComparison()
                .isEqualTo(savedMenuProduct);
        }

        @DisplayName("ID가 존재하지 않는다면 MenuProduct 조회에 실패한다.")
        @Test
        void isNotPresent() {
            // when
            Optional<MenuProduct> foundProduct = jdbcTemplateMenuProductDao.findById(Long.MAX_VALUE);

            // then
            assertThat(foundProduct).isNotPresent();
        }
    }

    @DisplayName("모든 MenuProduct를 조회한다.")
    @Test
    void findAll() {
        // given
        List<MenuProduct> beforeSavedMenuProducts = jdbcTemplateMenuProductDao.findAll();

        MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("추천 메뉴"));
        Menu menu = jdbcTemplateMenuDao.save(Menu를_생성한다("더블 새우 버거 세트", 5_600, menuGroup.getId()));
        Product product = jdbcTemplateProductDao.save(Product를_생성한다("더블 새우 버거", 4_000));

        beforeSavedMenuProducts.add(jdbcTemplateMenuProductDao.save(MenuProduct를_생성한다(menu.getId(), product.getId(), 5)));
        beforeSavedMenuProducts.add(jdbcTemplateMenuProductDao.save(MenuProduct를_생성한다(menu.getId(), product.getId(), 5)));
        beforeSavedMenuProducts.add(jdbcTemplateMenuProductDao.save(MenuProduct를_생성한다(menu.getId(), product.getId(), 5)));

        // when
        List<MenuProduct> afterSavedMenuProducts = jdbcTemplateMenuProductDao.findAll();

        // then
        assertThat(afterSavedMenuProducts).hasSize(beforeSavedMenuProducts.size());
        assertThat(afterSavedMenuProducts).usingRecursiveComparison()
            .isEqualTo(beforeSavedMenuProducts);
    }

    @DisplayName("menuId가 일치하는 모든 MenuProduct를 조회한다.")
    @Test
    void findAllByMenuId() {
        // given
        MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("추천 메뉴"));
        Menu menu = jdbcTemplateMenuDao.save(Menu를_생성한다("더블 치즈 버거 세트", 6_500, menuGroup.getId()));
        Product product = jdbcTemplateProductDao.save(Product를_생성한다("더블 치즈 버거", 5_000));

        MenuProduct menuProduct1 = jdbcTemplateMenuProductDao.save(MenuProduct를_생성한다(menu.getId(), product.getId(), 5));
        MenuProduct menuProduct2 = jdbcTemplateMenuProductDao.save(MenuProduct를_생성한다(menu.getId(), product.getId(), 15));
        MenuProduct menuProduct3 = jdbcTemplateMenuProductDao.save(MenuProduct를_생성한다(menu.getId(), product.getId(), 10));

        // when
        List<MenuProduct> foundMenuProducts = jdbcTemplateMenuProductDao.findAllByMenuId(menu.getId());

        // then
        assertThat(foundMenuProducts).extracting("seq")
            .containsExactly(menuProduct1.getSeq(), menuProduct2.getSeq(), menuProduct3.getSeq());
    }

    private MenuProduct MenuProduct를_생성한다(Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    private Menu Menu를_생성한다(String name, int price, Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);

        return menu;
    }

    private Product Product를_생성한다(String name, int price) {
        return new Product(name, BigDecimal.valueOf(price));
    }

    private MenuGroup MenuGroup을_생성한다(String name) {
        return new MenuGroup(name);
    }
}