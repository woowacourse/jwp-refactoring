package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.generateMenu;
import static kitchenpos.fixture.MenuGroupFixture.generateMenuGroup;
import static kitchenpos.fixture.MenuGroupFixture.generateMenuGroupWithId;
import static kitchenpos.fixture.MenuProductFixture.generateMemberProduct;
import static kitchenpos.fixture.ProductFixture.generateProduct;
import static kitchenpos.fixture.ProductFixture.generateProductWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.FakeMenuDao;
import kitchenpos.dao.FakeMenuGroupDao;
import kitchenpos.dao.FakeMenuProductDao;
import kitchenpos.dao.FakeProductDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MenuServiceTest {

    private MenuService menuService;

    private MenuGroupDao menuGroupDao;
    private MenuProductDao menuProductDao;
    private ProductDao productDao;

    @BeforeEach
    void beforeEach() {
        MenuDao menuDao = new FakeMenuDao();
        this.menuGroupDao = new FakeMenuGroupDao();
        this.menuProductDao = new FakeMenuProductDao();
        this.productDao = new FakeProductDao();
        this.menuService = new MenuService(menuDao, this.menuGroupDao, this.menuProductDao, this.productDao);
    }


    @Test
    @DisplayName("메뉴를 생성한다.")
    void create() {
        // given
        Product 사이다 = generateProduct("사이다", 1000);
        Product 뿌링클 = generateProduct("뿌링클", 19000);
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 세트메뉴_1L = menuGroupDao.save(세트메뉴);

        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 뿌링클_한개 = menuProductDao.save(generateMemberProduct(뿌링클_2L, 1));
        menuProducts.add(사이다_두개);
        menuProducts.add(뿌링클_한개);

        Menu 뿌링클_음료두개_세트 = generateMenu("뿌링클 음료두개 세트", BigDecimal.valueOf(21000), 세트메뉴_1L, menuProducts);
        // when
        Menu createdMenu = menuService.create(뿌링클_음료두개_세트);

        // then
        assertThat(createdMenu.getId()).isNotNull();
    }

    @Test
    @DisplayName("메뉴를 생성 시 가격이 null이라면 예외를 반환한다.")
    void create_WhenNullPrice() {
        // given
        Product 사이다 = generateProduct("사이다", 1000);
        Product 뿌링클 = generateProduct("뿌링클", 19000);
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 세트메뉴_1L = menuGroupDao.save(세트메뉴);

        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 뿌링클_한개 = menuProductDao.save(generateMemberProduct(뿌링클_2L, 1));
        menuProducts.add(사이다_두개);
        menuProducts.add(뿌링클_한개);

        Menu 뿌링클_음료두개_세트 = generateMenu("뿌링클 음료두개 세트", null, 세트메뉴_1L, menuProducts);
        // when & then
        assertThatThrownBy(() -> menuService.create(뿌링클_음료두개_세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴를 생성 시 가격이 0보다 작다면 예외를 반환한다.")
    void create_WhenPriceUnderZero() {
        // given
        Product 사이다 = generateProduct("사이다", 1000);
        Product 뿌링클 = generateProduct("뿌링클", 19000);
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 세트메뉴_1L = menuGroupDao.save(세트메뉴);

        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 뿌링클_한개 = menuProductDao.save(generateMemberProduct(뿌링클_2L, 1));
        menuProducts.add(사이다_두개);
        menuProducts.add(뿌링클_한개);

        Menu 뿌링클_음료두개_세트 = generateMenu("뿌링클 음료두개 세트", BigDecimal.valueOf(-1), 세트메뉴_1L, menuProducts);
        // when & then
        assertThatThrownBy(() -> menuService.create(뿌링클_음료두개_세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 시 존재하지 않는 MenuGroup이라면 예외를 반환한다.")
    void create_WhenNotExistMenuGroup() {
        // given
        Product 사이다 = generateProduct("사이다", 1000);
        Product 뿌링클 = generateProduct("뿌링클", 19000);
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 할인메뉴 = generateMenuGroupWithId("할인메뉴", -1L);
        menuGroupDao.save(세트메뉴);

        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 뿌링클_한개 = menuProductDao.save(generateMemberProduct(뿌링클_2L, 1));
        menuProducts.add(사이다_두개);
        menuProducts.add(뿌링클_한개);

        Menu 뿌링클_음료두개_세트 = generateMenu("뿌링클 음료두개 세트", BigDecimal.valueOf(-1), 할인메뉴, menuProducts);
        // when & then
        assertThatThrownBy(() -> menuService.create(뿌링클_음료두개_세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 시 존재하지 않는 Product라면 예외를 반환한다.")
    void create_WhenNotExistProduct() {
        // given
        Product 사이다 = generateProduct("사이다", 1000);
        Product 뿌링클 = generateProduct("뿌링클", 19000);
        Product 맛초킹_저장안됨 = generateProductWithId("맛초킹", 19000, -1L);
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 세트메뉴_1L = menuGroupDao.save(세트메뉴);

        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 맛초킹_저장안됨_한개 = menuProductDao.save(generateMemberProduct(맛초킹_저장안됨, 1));
        menuProducts.add(사이다_두개);
        menuProducts.add(맛초킹_저장안됨_한개);

        Menu 맛초킹_음료두개_세트 = generateMenu("뿌링클 음료두개 세트", BigDecimal.valueOf(21000), 세트메뉴_1L, menuProducts);
        // when & then
        assertThatThrownBy(() -> menuService.create(맛초킹_음료두개_세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 시 Product의 수량 * 합보다 가격이 비싸면 예외를 반환한다.")
    void create_WhenMoreThanSumPrice() {
        // given
        Product 사이다 = generateProduct("사이다", 1000);
        Product 뿌링클 = generateProduct("뿌링클", 19000);
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 세트메뉴_1L = menuGroupDao.save(세트메뉴);

        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 뿌링클_한개 = menuProductDao.save(generateMemberProduct(뿌링클_2L, 1));
        menuProducts.add(사이다_두개);
        menuProducts.add(뿌링클_한개);

        Menu 뿌링클_음료두개_세트 = generateMenu("뿌링클 음료두개 세트", BigDecimal.valueOf(22000), 세트메뉴_1L, menuProducts);
        // when & then
        assertThatThrownBy(() -> menuService.create(뿌링클_음료두개_세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        // given
        Product 사이다 = generateProduct("사이다", 1000);
        Product 뿌링클 = generateProduct("뿌링클", 19000);
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 세트메뉴_1L = menuGroupDao.save(세트메뉴);

        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 뿌링클_한개 = menuProductDao.save(generateMemberProduct(뿌링클_2L, 1));
        menuProducts.add(사이다_두개);
        menuProducts.add(뿌링클_한개);

        Product 맛초킹 = generateProduct("맛초킹", 19000);
        Product 맛초킹_3L = productDao.save(맛초킹);

        List<MenuProduct> menuProducts2 = new ArrayList<>();
        MenuProduct 사이다_세개 = menuProductDao.save(generateMemberProduct(사이다_1L, 3));
        MenuProduct 맛초킹_한개 = menuProductDao.save(generateMemberProduct(맛초킹_3L, 1));

        menuProducts2.add(사이다_세개);
        menuProducts2.add(맛초킹_한개);

        Menu 뿌링클_음료두개_세트 = generateMenu("뿌링클 음료두개 세트", BigDecimal.valueOf(21000), 세트메뉴_1L, menuProducts);
        Menu 맛초킹_음료세개_세트 = generateMenu("맛쵸킹 음료세개 세트", BigDecimal.valueOf(21000), 세트메뉴_1L, menuProducts2);
        menuService.create(뿌링클_음료두개_세트);
        menuService.create(맛초킹_음료세개_세트);

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus.size()).isEqualTo(2);
    }
}
