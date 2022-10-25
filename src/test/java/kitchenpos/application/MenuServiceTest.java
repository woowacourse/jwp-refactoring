package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.fake.FakeMenuDao;
import kitchenpos.dao.fake.FakeMenuGroupDao;
import kitchenpos.dao.fake.FakeMenuProductDao;
import kitchenpos.dao.fake.FakeProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.fixture.MenuFixture;
import kitchenpos.domain.fixture.MenuGroupFixture;
import kitchenpos.domain.fixture.MenuProductFixture;
import kitchenpos.domain.fixture.ProductFixture;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("Menu 서비스 테스트")
class MenuServiceTest {

    private MenuService menuService;

    private MenuDao menuDao;

    private MenuGroup savedMenuGroup;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        final MenuGroupDao menuGroupDao = new FakeMenuGroupDao();
        final ProductDao productDao = new FakeProductDao();

        menuDao = new FakeMenuDao();
        menuService = new MenuService(menuDao, menuGroupDao, new FakeMenuProductDao(), productDao);

        final MenuGroup menuGroup = MenuGroupFixture.치킨_세트().build();
        savedMenuGroup = menuGroupDao.save(menuGroup);

        final Product product = ProductFixture.후라이드_치킨()
            .가격(new BigDecimal(15_000))
            .build();
        savedProduct = productDao.save(product);
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void create() {
        final Menu menu = MenuFixture.후라이드_치킨_세트()
            .메뉴_그룹_아이디(savedMenuGroup.getId())
            .가격(savedProduct.getPrice())
            .build();

        final MenuProduct menuProduct = MenuProductFixture.후라이드()
            .상품_아이디(savedProduct.getId())
            .수량(1)
            .build();
        menu.setMenuProducts(List.of(menuProduct));

        final Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu.getId()).isNotNull();
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격은 null 이 아니어야 한다")
    @Test
    void createMenuPriceIsNull() {
        final Menu menu = MenuFixture.후라이드_치킨_세트()
            .메뉴_그룹_아이디(savedMenuGroup.getId())
            .가격(null)
            .build();

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격은 0원 이상이어야 한다")
    @Test
    void createMenuPriceIsLowerZero() {
        final Menu menu = MenuFixture.후라이드_치킨_세트()
            .메뉴_그룹_아이디(savedMenuGroup.getId())
            .가격(new BigDecimal(-1))
            .build();

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴 그룹의 아이디가 존재해야 한다")
    @Test
    void createMenuGroupIdIsNotExist() {
        long notSavedMenuGroupId = 0L;
        final Menu menu = MenuFixture.후라이드_치킨_세트()
            .메뉴_그룹_아이디(notSavedMenuGroupId)
            .build();

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴 상품은 등록되어 있는 상품이어야 한다")
    @Test
    void createMenuProductIsNotExist() {
        final MenuProduct notSavedMenuProduct = new MenuProduct();

        final Menu menu = MenuFixture.후라이드_치킨_세트()
            .메뉴_그룹_아이디(savedMenuGroup.getId())
            .메뉴_상품_리스트(List.of(notSavedMenuProduct))
            .build();

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격은 주문 금액의 총합보다 작거나 같아야 한다")
    @Test
    void createMenuPriceIsHigherThanAmount() {
        final BigDecimal price = savedProduct.getPrice().add(new BigDecimal(1L));
        final MenuProduct menuProduct = MenuProductFixture.후라이드()
            .상품_아이디(savedProduct.getId())
            .수량(1)
            .build();

        final Menu menu = MenuFixture.후라이드_치킨_세트()
            .메뉴_그룹_아이디(savedMenuGroup.getId())
            .가격(price)
            .메뉴_상품_리스트(List.of(menuProduct))
            .build();

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 목록을 조회한다")
    @Test
    void list() {
        final int numberOfMenu = 5;
        for (int i = 0; i < numberOfMenu; i++) {
            menuDao.save(MenuFixture.후라이드_치킨_세트().build());
        }

        final List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(numberOfMenu);
    }
}
