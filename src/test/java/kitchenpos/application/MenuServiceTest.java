package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.createMenuWithGroupId;
import static kitchenpos.fixture.MenuFixture.createMenuWithPrice;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroupWithoutId;
import static kitchenpos.fixture.MenuProductFixture.createMenuProductWithoutId;
import static kitchenpos.fixture.ProductFixture.createProductWithPrice;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql(value = "/truncate.sql")
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @DisplayName("Menu 등록 성공")
    @Test
    void create() {
        BigDecimal price = new BigDecimal(10000);
        Product savedProduct = productDao.save(createProductWithPrice(price));
        MenuProduct menuProduct = createMenuProductWithoutId(savedProduct.getId(), 1L);
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroupWithoutId());
        Menu menu = MenuFixture.createMenuWithoutId(savedMenuGroup.getId(), price, menuProduct);

        Menu actual = menuService.create(menu);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getMenuProducts()).extracting("menuId")
                    .containsOnly(actual.getId());
        });
    }

    @DisplayName("Menu 가격이 0보다 작은 경우 예외 테스트")
    @Test
    void createPriceLessThanZero() {
        Menu menu = createMenuWithPrice(BigDecimal.ZERO);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu GruopId가 DB에 존재하지 않는 경우 예외 테스트")
    @Test
    void createNotExistMenuGroupId() {
        Menu menu = createMenuWithGroupId(1L);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 속하는 MenuProducts의 총 금액이 메뉴의 가격보다 작은 경우 예외 테스트")
    @Test
    void createMenuProductsAmountLessThanMenuPrice() {
        Product savedProduct = productDao.save(createProductWithPrice(new BigDecimal(9999)));
        MenuProduct menuProduct = createMenuProductWithoutId(savedProduct.getId(), 1L);
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroupWithoutId());
        Menu menu = MenuFixture.createMenuWithoutId(savedMenuGroup.getId(), new BigDecimal(10000), menuProduct);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu 전체조회 테스트")
    @Test
    void list() {
        BigDecimal price = new BigDecimal(10000);
        Product savedProduct = productDao.save(createProductWithPrice(price));
        MenuProduct menuProduct = createMenuProductWithoutId(savedProduct.getId(), 1L);
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroupWithoutId());
        Menu menu = MenuFixture.createMenuWithoutId(savedMenuGroup.getId(), price, menuProduct);
        menuDao.save(menu);

        List<Menu> actual = menuService.list();

        assertThat(actual).hasSize(1);
    }
}