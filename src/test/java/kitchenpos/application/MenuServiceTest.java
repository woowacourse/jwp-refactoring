package kitchenpos.application;

import static kitchenpos.support.fixture.domain.MenuFixture.CHICKEN_1000;
import static kitchenpos.support.fixture.domain.MenuFixture.PIZZA_2000;
import static kitchenpos.support.fixture.domain.MenuGroupFixture.KOREAN;
import static kitchenpos.support.fixture.domain.MenuProductFixture.ONE;
import static kitchenpos.support.fixture.domain.ProductFixture.APPLE_1000;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.NestedApplicationTest;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.dao.JdbcTemplateMenuDao;
import kitchenpos.menu.domain.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.product.domain.dao.JdbcTemplateProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.support.fixture.domain.MenuGroupFixture;
import kitchenpos.support.fixture.dto.MenuDtoFixture;
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
            List<MenuProduct> menuProducts = List.of(
                ONE.getMenuProduct(product1.getId()),
                ONE.getMenuProduct(product2.getId()),
                ONE.getMenuProduct(product3.getId()));

            MenuResponse response = menuService.create(MenuDtoFixture.메뉴_생성_요청(menu, menuProducts));

            assertThat(response).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(MenuDtoFixture.메뉴_생성_응답(menu));
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
            List<MenuResponse> responses = menuService.list();

            assertThat(responses).hasSize(2);
        }
    }
}
