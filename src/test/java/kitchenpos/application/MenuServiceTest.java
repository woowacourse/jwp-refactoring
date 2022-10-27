package kitchenpos.application;

import static kitchenpos.domain.fixture.MenuFixture.후라이드_치킨_세트;
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

import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.fake.FakeMenuDao;
import kitchenpos.dao.fake.FakeMenuGroupDao;
import kitchenpos.dao.fake.FakeMenuProductDao;
import kitchenpos.dao.fake.FakeProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("Menu 서비스 테스트")
class MenuServiceTest {

    private static final String MENU_NAME = "후라이드 치킨 세트";
    private static final BigDecimal PRICE = new BigDecimal(15_000);
    private static final List<MenuProduct> MENU_PRODUCTS = null;

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
        final MenuRequest request = new MenuRequest(MENU_NAME, PRICE, 저장된_치킨_세트.getId(), List.of(menuProduct));

        final MenuResponse response = menuService.create(request);

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격은 null 이 아니어야 한다")
    @Test
    void createMenuPriceIsNull() {
        final BigDecimal invalidPrice = null;
        final MenuRequest request = new MenuRequest(MENU_NAME, invalidPrice, 저장된_치킨_세트.getId(), MENU_PRODUCTS);

        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격은 0원 이상이어야 한다")
    @Test
    void createMenuPriceIsLowerZero() {
        final BigDecimal invalidPrice = new BigDecimal(-1);
        final MenuRequest request = new MenuRequest(MENU_NAME, invalidPrice, 저장된_치킨_세트.getId(), MENU_PRODUCTS);

        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴 그룹의 아이디가 존재해야 한다")
    @Test
    void createMenuGroupIdIsNotExist() {
        final long notSavedMenuGroupId = 0L;
        final MenuRequest request = new MenuRequest(MENU_NAME, PRICE, notSavedMenuGroupId, MENU_PRODUCTS);

        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴 상품은 등록되어 있는 상품이어야 한다")
    @Test
    void createMenuProductIsNotExist() {
        final long notSavedProductId = 0L;
        final MenuProduct notSavedMenuProduct = 상품_하나(notSavedProductId);
        final MenuRequest request = new MenuRequest(MENU_NAME, PRICE, 저장된_치킨_세트.getId(), List.of(notSavedMenuProduct));

        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴의 가격은 주문 금액의 총합보다 작거나 같아야 한다")
    @Test
    void createMenuPriceIsHigherThanAmount() {
        final BigDecimal price = 저장된_후라이드_치킨.getPrice().add(new BigDecimal(1L));
        final MenuProduct menuProduct = 상품_하나(저장된_후라이드_치킨.getId());

        final MenuRequest request = new MenuRequest(MENU_NAME, price, 저장된_치킨_세트.getId(), List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 목록을 조회한다")
    @Test
    void list() {
        final int numberOfMenu = 5;
        for (int i = 0; i < numberOfMenu; i++) {
            menuDao.save(후라이드_치킨_세트());
        }

        final List<MenuResponse> responses = menuService.list();

        assertThat(responses).hasSize(numberOfMenu);
    }
}
