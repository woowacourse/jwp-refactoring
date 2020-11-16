package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.FixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql(value = "/truncate.sql")
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        Product savedProduct = productDao.save(createProduct(null, "터틀치킨", BigDecimal.valueOf(16_000L)));
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroup(null, "치킨"));
        Menu menuRequest = createMenu(null, savedProduct.getName(), savedProduct.getPrice(), savedMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(null, savedProduct.getId(), 1L)));

        Menu savedMenu = menuService.create(menuRequest);

        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo(menuRequest.getName()),
                () -> assertThat(savedMenu.getPrice()).isEqualTo(menuRequest.getPrice()),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuRequest.getMenuGroupId()),
                () -> assertThat(savedMenu.getMenuProducts().size()).isEqualTo(menuRequest.getMenuProducts().size())
        );
    }

    @DisplayName("메뉴의 가격이 올바르지 않은 경우 예외 발생")
    @Test
    void create_exception1() {
        BigDecimal invalidPrice = BigDecimal.valueOf(-1L);
        Product savedProduct = productDao.save(createProduct(null, "터틀치킨", invalidPrice));
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroup(null, "치킨"));
        Menu menuRequest = createMenu(null, savedProduct.getName(), savedProduct.getPrice(), savedMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(null, savedProduct.getId(), 1L)));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 존재하지 않은 경우 예외 발생")
    @Test
    void create_exception2() {
        Long invalidMenuGroupId = 0L;
        Product savedProduct = productDao.save(createProduct(null, "터틀치킨", BigDecimal.valueOf(16_000L)));
        Menu menuRequest = createMenu(null, savedProduct.getName(), savedProduct.getPrice(), invalidMenuGroupId,
                Collections.singletonList(createMenuProduct(null, savedProduct.getId(), 1L)));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void list() {
        Product savedProduct = productDao.save(createProduct(null, "터틀치킨", BigDecimal.valueOf(16_000L)));
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroup(null, "치킨"));
        Menu menuRequest = createMenu(null, savedProduct.getName(), savedProduct.getPrice(), savedMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(null, savedProduct.getId(), 1L)));
        menuService.create(menuRequest);

        List<Menu> menus = menuService.list();

        assertThat(menus.size()).isEqualTo(1);
    }
}