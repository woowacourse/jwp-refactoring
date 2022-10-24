package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixture.메뉴_등록;
import static kitchenpos.application.fixture.MenuFixture.양념_치킨;
import static kitchenpos.application.fixture.MenuFixture.포테이토_피자;
import static kitchenpos.application.fixture.MenuFixture.후라이드_치킨;
import static kitchenpos.application.fixture.MenuGroupFixture.치킨;
import static kitchenpos.application.fixture.MenuGroupFixture.피자;
import static kitchenpos.application.fixture.MenuProductFixture.메뉴_상품_생성;
import static kitchenpos.application.fixture.ProductFixture.상품_등록;
import static kitchenpos.application.fixture.ProductFixture.양념_치킨;
import static kitchenpos.application.fixture.ProductFixture.포테이토_피자;
import static kitchenpos.application.fixture.ProductFixture.후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private MenuService menuService;


    @DisplayName("전체 메뉴를 조회한다")
    @Test
    void findAll() {
        // given
        Product productChicken1 = productDao.save(후라이드_치킨());
        Product productChicken2 = productDao.save(양념_치킨());
        MenuGroup chickenMenuGroup = menuGroupDao.save(치킨());

        Menu menuChicken1 = menuDao.save(후라이드_치킨(chickenMenuGroup));
        Menu menuChicken2 = menuDao.save(양념_치킨(chickenMenuGroup));

        MenuProduct menuProductChicken1 = 메뉴_상품_생성(menuChicken1, productChicken1, 1);
        MenuProduct menuProductChicken2 = 메뉴_상품_생성(menuChicken2, productChicken2, 1);

        Product productPizza = productDao.save(포테이토_피자());
        MenuGroup pizzaMenuGroup = menuGroupDao.save(피자());

        Menu menuPizza = menuDao.save(포테이토_피자(pizzaMenuGroup));
        MenuProduct menuProductPizza = 메뉴_상품_생성(menuPizza, productPizza, 1);

        // when
        List<Menu> menus = menuService.list();

        //then
        assertAll(
                () -> assertThat(menus).hasSize(3),
                () -> assertThat(menus).extracting("menuProducts").isNotNull()
        );
    }


    @DisplayName("메뉴의 가격이 null이면 예외가 발생한다.")
    @Test
    void createMenePriceNull() {
        // given
        MenuGroup chickenMenuGroup = menuGroupDao.save(치킨());
        Menu menu = 메뉴_등록("후라이드치킨", null, chickenMenuGroup.getId());

        // when & then
        assertThatThrownBy(
                () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void createMenuPrice0() {
        // given
        MenuGroup chickenMenuGroup = menuGroupDao.save(치킨());
        Menu menu = 메뉴_등록("후라이드치킨", BigDecimal.valueOf(-1000), chickenMenuGroup.getId());

        // when & then
        assertThatThrownBy(
                () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹의 아이디가 존재하지 않으면 예외를 발생한다.")
    @Test
    void createNoProduct() {
        // given
        Menu menu = 메뉴_등록("후라이드치킨", BigDecimal.valueOf(-1000), 0L);

        // when & then
        assertThatThrownBy(
                () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("MenuProduct의 product가 존재하지 않으면 예외를 발생한다.")
    @Test
    void createMenuGroupId() {
        // given
        MenuGroup pizzaMenuGroup = menuGroupDao.save(피자());
        Menu menuPizza = 포테이토_피자(pizzaMenuGroup);
        MenuProduct menuProductPizza = 메뉴_상품_생성(menuPizza.getId(), 0L, 1);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProductPizza);
        menuPizza.setMenuProducts(menuProducts);

        // when & then
        assertThatThrownBy(
                () -> menuService.create(menuPizza)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 구매할 수 있는 product의 금액보다 크면 예외를 발생한다.")
    @Test
    void createInvalidMenuPrice() {
        // given
        MenuGroup pizzaMenuGroup = menuGroupDao.save(피자());
        Product productPizza = productDao.save(상품_등록("포테이토피자", 5000));
        Menu menuPizza = 메뉴_등록("포테이토피자", BigDecimal.valueOf(16000), pizzaMenuGroup.getId());
        MenuProduct menuProductPizza = 메뉴_상품_생성(menuPizza.getId(), productPizza.getId(), 3);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProductPizza);
        menuPizza.setMenuProducts(menuProducts);

        // when & then
        assertThatThrownBy(
                () -> menuService.create(menuPizza)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 구매할 수 있는 product의 금액보다 같거나 작으면 예외를 발생하지 않는다.")
    @ParameterizedTest
    @ValueSource(ints = {15000, 14000})
    void createInvalidMenuPrice(int price) {
        // given
        MenuGroup pizzaMenuGroup = menuGroupDao.save(피자());
        Product productPizza = productDao.save(상품_등록("포테이토피자", 5000));
        Menu menuPizza = 메뉴_등록("포테이토피자", BigDecimal.valueOf(price), pizzaMenuGroup.getId());
        MenuProduct menuProductPizza = 메뉴_상품_생성(menuPizza.getId(), productPizza.getId(), 3);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProductPizza);
        menuPizza.setMenuProducts(menuProducts);

        // when & then
        assertDoesNotThrow(
                () -> menuService.create(menuPizza)
        );
    }

    @DisplayName("Menu를 올바르게 저장한다.")
    @Test
    void createMenu() {
        // given
        MenuGroup pizzaMenuGroup = menuGroupDao.save(피자());
        Product productPizza = productDao.save(상품_등록("포테이토피자", 5000));
        Menu menuPizza = 메뉴_등록("포테이토피자", BigDecimal.valueOf(15000), pizzaMenuGroup.getId());
        MenuProduct menuProductPizza = 메뉴_상품_생성(menuPizza.getId(), productPizza.getId(), 3);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProductPizza);
        menuPizza.setMenuProducts(menuProducts);

        // when
        Menu menu = menuService.create(menuPizza);

        List<Menu> menus = menuDao.findAll();
        Optional<Menu> foundMenu = menuDao.findById(menu.getId());
        //then
        assertAll(
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(foundMenu).isPresent()
        );
    }

    @DisplayName("Menu를 올바르게 저장할 때 menuProduct도 올바르게 저장됨을 확인한다.")
    @Test
    void createMenuAndCheckMenuProduct() {
        // given
        MenuGroup pizzaMenuGroup = menuGroupDao.save(피자());
        Product productPizza = productDao.save(상품_등록("포테이토피자", 5000));
        Menu menuPizza = 메뉴_등록("포테이토피자", BigDecimal.valueOf(15000), pizzaMenuGroup.getId());
        MenuProduct menuProductPizza = 메뉴_상품_생성(menuPizza.getId(), productPizza.getId(), 3);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProductPizza);
        menuPizza.setMenuProducts(menuProducts);

        // when
        Menu menu = menuService.create(menuPizza);

        Optional<Menu> foundMenu = menuDao.findById(menu.getId());
        List<MenuProduct> foundMenuProducts = menuProductDao.findAllByMenuId(menu.getId());
        //then
        assertAll(
                () -> assertThat(foundMenu).isPresent(),
                () -> assertThat(foundMenuProducts).hasSize(1),
                () -> assertThat(foundMenuProducts.get(0).getProductId()).isEqualTo(productPizza.getId())
        );
    }
}
