package kitchenpos.application;

import static kitchenpos.support.fixture.domain.MenuFixture.CHICKEN_1000;
import static kitchenpos.support.fixture.domain.MenuFixture.PIZZA_2000;
import static kitchenpos.support.fixture.domain.MenuGroupFixture.KOREAN;
import static kitchenpos.support.fixture.domain.MenuProductFixture.ONE;
import static kitchenpos.support.fixture.domain.ProductFixture.APPLE_1000;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.NestedApplicationTest;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.support.fixture.domain.MenuGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private JdbcTemplateMenuDao jdbcTemplateMenuDao;

    @Autowired
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @Autowired
    private JdbcTemplateProductDao jdbcTemplateProductDao;

    @NestedApplicationTest
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("메뉴를 생성한다.")
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

            Menu actual = menuService.create(menu);

            assertThat(actual).extracting(Menu::getName, it -> it.getPrice().longValue(), Menu::getMenuGroupId)
                    .contains(menu.getName(), menu.getPrice().longValue(), menu.getMenuGroupId());
        }
    }

    @NestedApplicationTest
    @DisplayName("list 메서드는")
    class ListTest {

        @BeforeEach
        void setUp() {
            MenuGroup menuGroup1 = jdbcTemplateMenuGroupDao.save(MenuGroupFixture.KOREAN.getMenuGroup());
            Menu menu1 = CHICKEN_1000.getMenu(menuGroup1.getId());
            jdbcTemplateMenuDao.save(menu1);

            MenuGroup menuGroup2 = jdbcTemplateMenuGroupDao.save(MenuGroupFixture.KOREAN.getMenuGroup());
            Menu menu2 = CHICKEN_1000.getMenu(menuGroup2.getId());
            jdbcTemplateMenuDao.save(menu2);
        }

        @Test
        @DisplayName("메뉴 전체 목록을 조회한다.")
        void success() {
            List<Menu> menus = menuService.list();

            assertThat(menus).hasSize(2);
        }
    }
}
