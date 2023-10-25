package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuProductFixtures;
import kitchenpos.request.MenuCreateRequest;
import kitchenpos.request.MenuProductDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixtures.두마리_메뉴;
import static kitchenpos.fixture.MenuGroupFixtures.한마리_메뉴;
import static kitchenpos.fixture.ProductFixtures.양념치킨_17000원;
import static kitchenpos.fixture.ProductFixtures.후라이드치킨_16000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다. 메뉴의 가격은 모든 상품의 금액(가격 * 수량)을 합한 것보다 클 수 없다.")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(두마리_메뉴);
        Product product1 = productDao.save(후라이드치킨_16000원);
        Product product2 = productDao.save(양념치킨_17000원);

        List<MenuProduct> menuProducts = List.of(
                MenuProductFixtures.create(product1, 1),
                MenuProductFixtures.create(product2, 1)
        );
        MenuCreateRequest request = new MenuCreateRequest(
                "후라이드+양념",
                BigDecimal.valueOf(33_000),
                menuGroup.getId(),
                MenuProductDto.of(menuProducts)
        );

        // when
        Menu actual = menuService.create(request);

        // then
        Assertions.assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getPrice().getValue().intValue()).isLessThanOrEqualTo(33_000),
                () -> assertThat(actual.getMenuProducts()).allMatch(
                        menuProduct -> menuProduct.getMenuId().equals(actual.getId())
                )
        );
    }

    @DisplayName("가격이 0원보다 낮은 메뉴를 생성하면 예외가 발생한다.")
    @ValueSource(ints = {-1, -100, -33_000})
    @ParameterizedTest
    void create_PriceLowerThanZero_ExceptionThrown(int invalidPrice) {
        // given
        MenuGroup menuGroup = menuGroupDao.save(한마리_메뉴);
        MenuCreateRequest request = new MenuCreateRequest(
                "후라이드+양념",
                BigDecimal.valueOf(invalidPrice),
                menuGroup.getId(),
                MenuProductDto.of(Collections.emptyList())
        );

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴 그룹의 메뉴를 주문하면 예외가 발생한다.")
    @Test
    void create_NonExistMenuGroup_ExceptionThrown() {
        // given
        Long nonExistMenuGroupId = 999_999L;

        MenuCreateRequest request = new MenuCreateRequest(
                "후라이드+양념",
                BigDecimal.valueOf(10_000),
                nonExistMenuGroupId,
                MenuProductDto.of(Collections.emptyList())
        );

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 상품이 담긴 메뉴를 생성하면 예외가 발생한다")
    @Test
    void create_NonExistProduct_ExceptionThrown() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(두마리_메뉴);
        Product product = productDao.save(후라이드치킨_16000원);
        Product nonSavedProduct = 양념치킨_17000원;

        List<MenuProduct> menuProducts = List.of(
                MenuProductFixtures.create(product, 1),
                MenuProductFixtures.create(nonSavedProduct, 1)
        );
        MenuCreateRequest request = new MenuCreateRequest(
                "후라이드+양념",
                BigDecimal.valueOf(33_000),
                menuGroup.getId(),
                MenuProductDto.of(menuProducts)
        );


        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품의 가격을 모두 합한 것보다 비싸면 예외가 발생한다")
    @Test
    void create_CalculateWrongPrice_ExceptionThrown() {
        // given
        int wrongCalculateResult = 330_000;

        MenuGroup menuGroup = menuGroupDao.save(두마리_메뉴);
        Product product1 = productDao.save(후라이드치킨_16000원);
        Product product2 = productDao.save(양념치킨_17000원);

        List<MenuProduct> menuProducts = List.of(
                MenuProductFixtures.create(product1, 1),
                MenuProductFixtures.create(product2, 1)
        );
        MenuCreateRequest request = new MenuCreateRequest(
                "후라이드+양념",
                BigDecimal.valueOf(wrongCalculateResult),
                menuGroup.getId(),
                MenuProductDto.of(menuProducts)
        );

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(두마리_메뉴);
        Product product1 = productDao.save(후라이드치킨_16000원);
        Product product2 = productDao.save(양념치킨_17000원);

        List<MenuProduct> menuProducts1 = List.of(
                MenuProductFixtures.create(product1, 1),
                MenuProductFixtures.create(product2, 1)
        );
        MenuCreateRequest request1 = new MenuCreateRequest(
                "후라이드+양념",
                BigDecimal.valueOf(33_000),
                menuGroup.getId(),
                MenuProductDto.of(menuProducts1)
        );

        List<MenuProduct> menuProducts2 = List.of(
                MenuProductFixtures.create(product1, 1),
                MenuProductFixtures.create(product1, 1)
        );
        MenuCreateRequest request2 = new MenuCreateRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(32_000),
                menuGroup.getId(),
                MenuProductDto.of(menuProducts2)
        );

        menuService.create(request1);
        menuService.create(request2);

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).hasSize(2);
    }
}
