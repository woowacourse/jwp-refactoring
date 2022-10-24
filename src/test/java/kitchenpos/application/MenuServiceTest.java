package kitchenpos.application;

import static kitchenpos.support.fixture.domain.MenuFixture.PIZZA_2000;
import static kitchenpos.support.fixture.domain.MenuGroupFixture.KOREAN;
import static kitchenpos.support.fixture.domain.MenuProductFixture.ONE;
import static kitchenpos.support.fixture.domain.ProductFixture.APPLE_1000;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private JdbcTemplateMenuDao jdbcTemplateMenuDao;

    @Autowired
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @Autowired
    private JdbcTemplateProductDao jdbcTemplateProductDao;

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("Menu를 생성한다.")
        void success() {
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());

            Menu menu = PIZZA_2000.getMenu(menuGroup.getId());
            Product product1 = jdbcTemplateProductDao.save(APPLE_1000.getProduct());
            Product product2 = jdbcTemplateProductDao.save(APPLE_1000.getProduct());
            Product product3 = jdbcTemplateProductDao.save(APPLE_1000.getProduct());
            menu.setMenuProducts(
                List.of(
                ONE.getMenuProduct(product1.getId()),
                ONE.getMenuProduct(product2.getId()),
                ONE.getMenuProduct(product3.getId())));

            Menu savedMenu = menuService.create(menu);

            Menu actual = jdbcTemplateMenuDao.findById(savedMenu.getId())
                .orElseThrow();
            assertThat(actual).usingRecursiveComparison()
                .ignoringFields("menuProducts")
                .isEqualTo(savedMenu);
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class ListTest {

        @Test
        @DisplayName("Menu 전체 목록을 조회한다.")
        void success() {
            List<Menu> menus = menuService.list();

            assertThat(menus).hasSize(6);
        }
    }
}
