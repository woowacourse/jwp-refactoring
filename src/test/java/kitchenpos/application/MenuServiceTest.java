package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.MenuProductFixture.createMenuProduct;
import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuRepository;
import kitchenpos.ui.dto.CreateMenuProductsRequest;
import kitchenpos.ui.dto.CreateMenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create_success() {
        // given
        MenuGroup newMenuGroup = menuGroupDao.save(createMenuGroup("추천메뉴"));
        CreateMenuRequest request = createMenuRequest("후라이드+후라이드", 19_000L, newMenuGroup.getId(),
                Collections.singletonList(createMenuProductRequest(1L, 2)));

        // when
        Menu savedMenu = menuService.create(request);

        // then
        Menu dbMenu = menuRepository.findById(savedMenu.getId())
                .orElseThrow(NoSuchElementException::new);
        assertAll(
                () -> assertThat(dbMenu.getName()).isEqualTo(request.getName()),
                () -> assertThat(dbMenu.getPrice().compareTo(request.getPrice())).isZero(),
                () -> assertThat(dbMenu.getMenuGroupId()).isEqualTo(request.getMenuGroupId())
        );
    }

    @DisplayName("메뉴를 생성할 때 가격이 음수이면 예외를 반환한다.")
    @Test
    void create_fail_if_price_is_negative() {
        // given
        MenuGroup newMenuGroup = menuGroupDao.save(createMenuGroup("추천메뉴"));
        CreateMenuRequest request = createMenuRequest("후라이드+후라이드", -1L, newMenuGroup.getId(),
                Collections.singletonList(createMenuProductRequest(1L, 2)));

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성할 때 존재하지 않는 메뉴그룹ID라면 예외를 반환한다.")
    @Test
    void create_fail_if_menuGroupId_is_null() {
        // given
        CreateMenuRequest request = createMenuRequest("후라이드+후라이드", 19_000L, 9999999L,
                Collections.singletonList(createMenuProductRequest(1L, 2)));

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성할 때 총 금액이 각 메뉴상품들의 가격의 총합보다 작으면 정상적으로 동작한다.")
    @Test
    void create_success_if_amount_is_smaller_than_menuPriceSum() {
        // given
        Product 후라이드 = productDao.save(createProduct("후라이드", 10_000L));
        MenuGroup newMenuGroup = menuGroupDao.save(createMenuGroup("추천메뉴"));
        CreateMenuRequest request = createMenuRequest("후라이드+후라이드", 19_000L, newMenuGroup.getId(),
                Arrays.asList(createMenuProductRequest(후라이드.getId(), 1), createMenuProductRequest(후라이드.getId(), 1)));

        // when, then
        assertDoesNotThrow(() -> menuService.create(request));
    }

    @DisplayName("메뉴를 생성할 때 총 금액이 각 메뉴상품들의 가격의 총합보다 크면 예외를 반환한다.")
    @ValueSource(longs = {20_001L, 30_000L})
    @ParameterizedTest
    void create_fail_if_amount_is_bigger_than_menuPriceSum(long price) {
        // given
        Product 후라이드 = productDao.save(createProduct("후라이드", 10_000L));
        MenuGroup newMenuGroup = menuGroupDao.save(createMenuGroup("추천메뉴"));
        CreateMenuRequest request = createMenuRequest("후라이드+후라이드", price, newMenuGroup.getId(),
                Arrays.asList(createMenuProductRequest(후라이드.getId(), 1), createMenuProductRequest(후라이드.getId(), 1)));

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list_success() {
        // given
        MenuGroup newMenuGroup = menuGroupDao.save(createMenuGroup("추천메뉴"));
        Menu menu = menuRepository.save(createMenu("후라이드+후라이드", 19_000L, newMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(1L, 2))));

        // when
        List<Menu> menus = menuService.list();

        // then
        List<String> menuNames = menus.stream()
                .map(Menu::getName)
                .collect(Collectors.toList());
        assertThat(menuNames).contains(menu.getName());
    }

    private CreateMenuRequest createMenuRequest(final String name, final Long price, final Long menuGroupId,
                                                final List<CreateMenuProductsRequest> menuProductsRequest) {
        return new CreateMenuRequest(name, BigDecimal.valueOf(price), menuGroupId, menuProductsRequest);
    }

    private CreateMenuProductsRequest createMenuProductRequest(final Long productId, final long quantity) {
        return new CreateMenuProductsRequest(productId, quantity);
    }
}
