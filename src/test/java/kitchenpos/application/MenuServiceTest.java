package kitchenpos.application;

import static kitchenpos.domain.fixture.MenuFixture.후라이드_치킨_세트;
import static kitchenpos.domain.fixture.MenuFixture.후라이드_치킨_세트의_가격과_메뉴_상품_리스트는;
import static kitchenpos.domain.fixture.MenuFixture.후라이드_치킨_세트의_가격은;
import static kitchenpos.domain.fixture.MenuFixture.후라이드_치킨_세트의_메뉴_상품들은;
import static kitchenpos.domain.fixture.MenuGroupFixture.치킨_세트;
import static kitchenpos.domain.fixture.MenuProductFixture.상품_하나;
import static kitchenpos.domain.fixture.ProductFixture.후라이드_치킨;
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

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("Menu 서비스 테스트")
class MenuServiceTest {

    private MenuService menuService;

    private MenuDao menuDao;

    private MenuGroup 저장된_치킨_세트;
    private Product 저장된_후라이드_치킨;

    @BeforeEach
    void setUp() {
        final MenuGroupDao menuGroupDao = new FakeMenuGroupDao();
        final ProductDao productDao = new FakeProductDao();

        menuDao = new FakeMenuDao();
        menuService = new MenuService(menuDao, menuGroupDao, new FakeMenuProductDao(), productDao);

        저장된_후라이드_치킨 = productDao.save(후라이드_치킨());
        저장된_치킨_세트 = menuGroupDao.save(치킨_세트());
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void create() {
        final MenuProduct menuProduct = 상품_하나(저장된_후라이드_치킨.getId());
        final Menu menu = 후라이드_치킨_세트의_메뉴_상품들은(저장된_치킨_세트.getId(), List.of(menuProduct));

        final Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu.getId()).isNotNull();
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격은 null 이 아니어야 한다")
    @Test
    void createMenuPriceIsNull() {
        final Menu menu = 후라이드_치킨_세트의_가격은(저장된_치킨_세트.getId(), null);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격은 0원 이상이어야 한다")
    @Test
    void createMenuPriceIsLowerZero() {
        final Menu menu = 후라이드_치킨_세트의_가격은(저장된_치킨_세트.getId(), new BigDecimal(-1));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴 그룹의 아이디가 존재해야 한다")
    @Test
    void createMenuGroupIdIsNotExist() {
        final long notSavedMenuGroupId = 0L;
        final Menu menu = 후라이드_치킨_세트(notSavedMenuGroupId);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴 상품은 등록되어 있는 상품이어야 한다")
    @Test
    void createMenuProductIsNotExist() {
        final long notSavedProductId = 0L;
        final MenuProduct notSavedMenuProduct = 상품_하나(notSavedProductId);
        final Menu menu = 후라이드_치킨_세트의_메뉴_상품들은(저장된_치킨_세트.getId(), List.of(notSavedMenuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격은 주문 금액의 총합보다 작거나 같아야 한다")
    @Test
    void createMenuPriceIsHigherThanAmount() {
        final BigDecimal price = 저장된_후라이드_치킨.getPrice().add(new BigDecimal(1L));
        final MenuProduct menuProduct = 상품_하나(저장된_후라이드_치킨.getId());

        final Menu menu = 후라이드_치킨_세트의_가격과_메뉴_상품_리스트는(저장된_치킨_세트.getId(), price, List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 목록을 조회한다")
    @Test
    void list() {
        final int numberOfMenu = 5;
        for (int i = 0; i < numberOfMenu; i++) {
            menuDao.save(후라이드_치킨_세트());
        }

        final List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(numberOfMenu);
    }
}
