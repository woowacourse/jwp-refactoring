package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.generateMenuRequest;
import static kitchenpos.fixture.MenuGroupFixture.generateMenuGroup;
import static kitchenpos.fixture.MenuGroupFixture.generateMenuGroupWithId;
import static kitchenpos.fixture.MenuProductFixture.generateMemberProduct;
import static kitchenpos.fixture.MenuProductFixture.generateMemberProductRequest;
import static kitchenpos.fixture.ProductFixture.맛초킹;
import static kitchenpos.fixture.ProductFixture.맛초킹_저장안됨;
import static kitchenpos.fixture.ProductFixture.뿌링클;
import static kitchenpos.fixture.ProductFixture.사이다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.FakeMenuDao;
import kitchenpos.dao.FakeMenuGroupDao;
import kitchenpos.dao.FakeMenuProductDao;
import kitchenpos.dao.FakeProductDao;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuServiceTest {

    private MenuService menuService;

    private MenuGroupDao menuGroupDao;
    private MenuProductDao menuProductDao;
    private ProductDao productDao;
    private MenuValidator menuValidator;

    @BeforeEach
    void beforeEach() {
        MenuDao menuDao = new FakeMenuDao();
        this.menuGroupDao = new FakeMenuGroupDao();
        this.menuProductDao = new FakeMenuProductDao();
        this.productDao = new FakeProductDao();
        this.menuValidator = new MenuValidator(productDao, menuGroupDao);
        this.menuService = new MenuService(menuDao, menuProductDao, menuValidator);
    }


    @Test
    @DisplayName("메뉴를 생성한다.")
    void create() {
        // given
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 세트메뉴_1L = menuGroupDao.save(세트메뉴);

        List<MenuProductRequest> menuProducts = new ArrayList<>();
        MenuProductRequest 사이다_두개_REQUEST = generateMemberProductRequest(사이다_1L, 2);
        MenuProductRequest 뿌링클_한개_REQUEST = generateMemberProductRequest(뿌링클_2L, 1);
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 뿌링클_한개 = menuProductDao.save(generateMemberProduct(뿌링클_2L, 1));
        menuProducts.add(사이다_두개_REQUEST);
        menuProducts.add(뿌링클_한개_REQUEST);

        MenuRequest 뿌링클_음료두개_세트 = generateMenuRequest("뿌링클 음료두개 세트", BigDecimal.valueOf(21000), 세트메뉴_1L, menuProducts);
        // when
        MenuResponse menuResponse = menuService.create(뿌링클_음료두개_세트);

        // then
        assertThat(menuResponse.getId()).isNotNull();
    }

    @Test
    @DisplayName("메뉴를 생성 시 가격이 null이라면 예외를 반환한다.")
    void create_WhenNullPrice() {
        // given
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 세트메뉴_1L = menuGroupDao.save(세트메뉴);

        List<MenuProductRequest> menuProducts = new ArrayList<>();
        MenuProductRequest 사이다_두개_REQUEST = generateMemberProductRequest(사이다_1L, 2);
        MenuProductRequest 뿌링클_한개_REQUEST = generateMemberProductRequest(뿌링클_2L, 1);
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 뿌링클_한개 = menuProductDao.save(generateMemberProduct(뿌링클_2L, 1));
        menuProducts.add(사이다_두개_REQUEST);
        menuProducts.add(뿌링클_한개_REQUEST);

        MenuRequest 뿌링클_음료두개_세트 = generateMenuRequest("뿌링클 음료두개 세트", null, 세트메뉴_1L, menuProducts);
        // when & then
        assertThatThrownBy(() -> menuService.create(뿌링클_음료두개_세트))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 공백이거나 0원보다 작을 수 없습니다.");
    }

    @Test
    @DisplayName("메뉴를 생성 시 가격이 0보다 작다면 예외를 반환한다.")
    void create_WhenPriceUnderZero() {
        // given
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 세트메뉴_1L = menuGroupDao.save(세트메뉴);

        List<MenuProductRequest> menuProducts = new ArrayList<>();
        MenuProductRequest 사이다_두개_REQUEST = generateMemberProductRequest(사이다_1L, 2);
        MenuProductRequest 뿌링클_한개_REQUEST = generateMemberProductRequest(뿌링클_2L, 1);
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 뿌링클_한개 = menuProductDao.save(generateMemberProduct(뿌링클_2L, 1));
        menuProducts.add(사이다_두개_REQUEST);
        menuProducts.add(뿌링클_한개_REQUEST);

        MenuRequest 뿌링클_음료두개_세트 = generateMenuRequest("뿌링클 음료두개 세트", BigDecimal.valueOf(-1), 세트메뉴_1L, menuProducts);
        // when & then
        assertThatThrownBy(() -> menuService.create(뿌링클_음료두개_세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 시 존재하지 않는 MenuGroup이라면 예외를 반환한다.")
    void create_WhenNotExistMenuGroup() {
        // given
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 할인메뉴 = generateMenuGroupWithId("할인메뉴", -1L);
        menuGroupDao.save(세트메뉴);

        List<MenuProductRequest> menuProducts = new ArrayList<>();
        MenuProductRequest 사이다_두개_REQUEST = generateMemberProductRequest(사이다_1L, 2);
        MenuProductRequest 뿌링클_한개_REQUEST = generateMemberProductRequest(뿌링클_2L, 1);
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 뿌링클_한개 = menuProductDao.save(generateMemberProduct(뿌링클_2L, 1));
        menuProducts.add(사이다_두개_REQUEST);
        menuProducts.add(뿌링클_한개_REQUEST);

        MenuRequest 뿌링클_음료두개_세트 = generateMenuRequest("뿌링클 음료두개 세트", BigDecimal.valueOf(21000), 할인메뉴, menuProducts);

        // when & then
        assertThatThrownBy(() -> menuService.create(뿌링클_음료두개_세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 시 존재하지 않는 Product라면 예외를 반환한다.")
    void create_WhenNotExistProduct() {
        // given
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 세트메뉴_1L = menuGroupDao.save(세트메뉴);

        List<MenuProductRequest> menuProducts = new ArrayList<>();
        MenuProductRequest 사이다_두개_REQUEST = generateMemberProductRequest(사이다_1L, 2);
        MenuProductRequest 맛초킹_저장안됨_한개_REQUEST = generateMemberProductRequest(맛초킹_저장안됨, 1);
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        menuProducts.add(사이다_두개_REQUEST);
        menuProducts.add(맛초킹_저장안됨_한개_REQUEST);

        MenuRequest 맛초킹_음료두개_세트 = generateMenuRequest("맛초킹 음료두개 세트", BigDecimal.valueOf(21000), 세트메뉴_1L, menuProducts);
        // when & then
        assertThatThrownBy(() -> menuService.create(맛초킹_음료두개_세트))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("메뉴 생성 시 Product의 수량 * 합보다 가격이 비싸면 예외를 반환한다.")
    void create_WhenMoreThanSumPrice() {
        // given
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 세트메뉴_1L = menuGroupDao.save(세트메뉴);

        List<MenuProductRequest> menuProducts = new ArrayList<>();
        MenuProductRequest 사이다_두개_REQUEST = generateMemberProductRequest(사이다_1L, 2);
        MenuProductRequest 뿌링클_한개_REQUEST = generateMemberProductRequest(뿌링클_2L, 1);
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 뿌링클_한개 = menuProductDao.save(generateMemberProduct(뿌링클_2L, 1));
        menuProducts.add(사이다_두개_REQUEST);
        menuProducts.add(뿌링클_한개_REQUEST);

        MenuRequest 뿌링클_음료두개_세트 = generateMenuRequest("뿌링클 음료두개 세트", BigDecimal.valueOf(22000), 세트메뉴_1L, menuProducts);
        // when & then
        assertThatThrownBy(() -> menuService.create(뿌링클_음료두개_세트))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 상품 수량 * 상품 가격의 합보다 클 수 없습니다.");
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        // given
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 세트메뉴_1L = menuGroupDao.save(세트메뉴);

        List<MenuProductRequest> menuProducts1 = new ArrayList<>();
        MenuProductRequest 사이다_두개_REQUEST = generateMemberProductRequest(사이다_1L, 2);
        MenuProductRequest 뿌링클_한개_REQUEST = generateMemberProductRequest(뿌링클_2L, 1);
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 뿌링클_한개 = menuProductDao.save(generateMemberProduct(뿌링클_2L, 1));
        menuProducts1.add(사이다_두개_REQUEST);
        menuProducts1.add(뿌링클_한개_REQUEST);

        Product 맛초킹_3L = productDao.save(맛초킹);

        List<MenuProductRequest> menuProducts2 = new ArrayList<>();
        MenuProductRequest 사이다_세개_REQUEST = generateMemberProductRequest(사이다_1L, 3);
        MenuProductRequest 맛초킹_한개_REQUEST = generateMemberProductRequest(맛초킹_3L, 1);
        MenuProduct 사이다_세개 = menuProductDao.save(generateMemberProduct(사이다_1L, 3));
        MenuProduct 맛초킹_한개 = menuProductDao.save(generateMemberProduct(맛초킹_3L, 1));

        menuProducts2.add(사이다_세개_REQUEST);
        menuProducts2.add(맛초킹_한개_REQUEST);

        MenuRequest 뿌링클_음료두개_세트 = generateMenuRequest("뿌링클 음료두개 세트", BigDecimal.valueOf(21000), 세트메뉴_1L, menuProducts1);
        MenuRequest 맛초킹_음료세개_세트 = generateMenuRequest("맛쵸킹 음료세개 세트", BigDecimal.valueOf(21000), 세트메뉴_1L, menuProducts2);
        menuService.create(뿌링클_음료두개_세트);
        menuService.create(맛초킹_음료세개_세트);

        // when
        List<MenuResponse> menuResponses = menuService.list();

        // then
        assertThat(menuResponses.size()).isEqualTo(2);
    }
}
