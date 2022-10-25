package kitchenpos.dao;

import static kitchenpos.support.fixture.domain.MenuFixture.CHICKEN_1000;
import static kitchenpos.support.fixture.domain.MenuGroupFixture.KOREAN;
import static kitchenpos.support.fixture.domain.MenuProductFixture.ONE;
import static kitchenpos.support.fixture.domain.MenuProductFixture.TWO;
import static kitchenpos.support.fixture.domain.ProductFixture.APPLE_1000;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.support.fixture.domain.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JdbcTemplateMenuProductDaoTest extends JdbcTemplateTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("메뉴 상품을 저장한다.")
        void success() {
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            Menu menu = jdbcTemplateMenuDao.save(CHICKEN_1000.getMenu(menuGroup.getId()));
            Product product = jdbcTemplateProductDao.save(APPLE_1000.getProduct());
            MenuProduct menuProduct = ONE.getMenuProduct(menu.getId(), product.getId());

            MenuProduct savedMenuProduct = jdbcTemplateMenuProductDao.save(menuProduct);

            Long actual = savedMenuProduct.getSeq();
            assertThat(actual).isNotNull();
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        private MenuProduct menuProduct;

        @BeforeEach
        void setUp() {
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            Menu menu = jdbcTemplateMenuDao.save(CHICKEN_1000.getMenu(menuGroup.getId()));
            Product product = jdbcTemplateProductDao.save(APPLE_1000.getProduct());
            menuProduct = jdbcTemplateMenuProductDao.save(ONE.getMenuProduct(menu.getId(), product.getId()));
        }

        @Test
        @DisplayName("아이디로 메뉴 상품을 단일 조회한다.")
        void success() {
            Long seq = menuProduct.getSeq();

            MenuProduct foundMenuProduct = jdbcTemplateMenuProductDao.findById(seq)
                .orElseThrow();

            assertThat(foundMenuProduct).usingRecursiveComparison()
                .isEqualTo(menuProduct);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @BeforeEach
        void setUp() {
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            Menu menu = jdbcTemplateMenuDao.save(CHICKEN_1000.getMenu(menuGroup.getId()));
            Product product = jdbcTemplateProductDao.save(APPLE_1000.getProduct());
            jdbcTemplateMenuProductDao.save(ONE.getMenuProduct(menu.getId(), product.getId()));
            jdbcTemplateMenuProductDao.save(ONE.getMenuProduct(menu.getId(), product.getId()));
        }

        @Test
        @DisplayName("메뉴 상품 전체 목록을 조회한다.")
        void success() {
            List<MenuProduct> menuProducts = jdbcTemplateMenuProductDao.findAll();

            assertThat(menuProducts).hasSize(2);
        }
    }

    @Nested
    @DisplayName("findAllByMenuId 메서드는")
    class FindAllByMenuId {

        private Menu menu;

        @BeforeEach
        void setUp() {
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            menu = jdbcTemplateMenuDao.save(CHICKEN_1000.getMenu(menuGroup.getId()));
            Product product1 = jdbcTemplateProductDao.save(APPLE_1000.getProduct());
            Product product2 = jdbcTemplateProductDao.save(ProductFixture.PEAR_2000.getProduct());
            jdbcTemplateMenuProductDao.save(ONE.getMenuProduct(menu.getId(), product1.getId()));
            jdbcTemplateMenuProductDao.save(TWO.getMenuProduct(menu.getId(), product2.getId()));
        }

        @Test
        @DisplayName("메뉴 아이디를 받으면 포함한 메뉴 상품 목록을 조회한다.")
        void success() {
            List<MenuProduct> menuProducts = jdbcTemplateMenuProductDao.findAllByMenuId(menu.getId());

            assertThat(menuProducts).hasSize(2);
        }
    }
}
