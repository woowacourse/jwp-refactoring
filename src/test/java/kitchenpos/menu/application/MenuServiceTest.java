package kitchenpos.menu.application;

import static kitchenpos.menu.fixture.MenuFixture.createMenu;
import static kitchenpos.menu.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.product.fixture.ProductFixture.createProduct;
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
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.exception.NotFoundException;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.menu.ui.dto.request.MenuCreateRequest;
import kitchenpos.menu.ui.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.ui.dto.response.MenuCreateResponse;
import kitchenpos.menu.ui.dto.response.MenuFindAllResponse;
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
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create_success() {
        // given
        MenuGroup newMenuGroup = menuGroupRepository.save(createMenuGroup("추천메뉴"));
        MenuCreateRequest request = createMenuCreateRequest("후라이드+후라이드", 19_000L, newMenuGroup.getId(),
                Collections.singletonList(createMenuProductCreateRequest(1L, 2)));

        // when
        MenuCreateResponse response = menuService.create(request);

        // then
        Menu dbMenu = menuRepository.findById(response.getId())
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
        MenuGroup newMenuGroup = menuGroupRepository.save(createMenuGroup("추천메뉴"));
        MenuCreateRequest request = createMenuCreateRequest("후라이드+후라이드", -1L, newMenuGroup.getId(),
                Collections.singletonList(createMenuProductCreateRequest(1L, 2)));

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성할 때 존재하지 않는 메뉴그룹ID라면 예외를 반환한다.")
    @Test
    void create_fail_if_menuGroupId_is_null() {
        // given
        MenuCreateRequest request = createMenuCreateRequest("후라이드+후라이드", 19_000L, 9999999L,
                Collections.singletonList(createMenuProductCreateRequest(1L, 2)));

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("메뉴를 생성할 때 총 금액이 각 메뉴상품들의 가격의 총합보다 작으면 정상적으로 동작한다.")
    @Test
    void create_success_if_amount_is_smaller_than_menuPriceSum() {
        // given
        Product 후라이드 = productRepository.save(createProduct("후라이드", 10_000L));
        MenuGroup newMenuGroup = menuGroupRepository.save(createMenuGroup("추천메뉴"));
        MenuCreateRequest request = createMenuCreateRequest("후라이드+후라이드", 19_000L, newMenuGroup.getId(),
                Arrays.asList(createMenuProductCreateRequest(후라이드.getId(), 1),
                        createMenuProductCreateRequest(후라이드.getId(), 1)));

        // when, then
        assertDoesNotThrow(() -> menuService.create(request));
    }

    @DisplayName("메뉴를 생성할 때 총 금액이 각 메뉴상품들의 가격의 총합보다 크면 예외를 반환한다.")
    @ValueSource(longs = {20_001L, 30_000L})
    @ParameterizedTest
    void create_fail_if_amount_is_bigger_than_menuPriceSum(long price) {
        // given
        Product 후라이드 = productRepository.save(createProduct("후라이드", 10_000L));
        MenuGroup newMenuGroup = menuGroupRepository.save(createMenuGroup("추천메뉴"));
        MenuCreateRequest request = createMenuCreateRequest("후라이드+후라이드", price, newMenuGroup.getId(),
                Arrays.asList(createMenuProductCreateRequest(후라이드.getId(), 1),
                        createMenuProductCreateRequest(후라이드.getId(), 1)));

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성할 때 존재하지 않는 상품ID라면 예외를 반환한다.")
    @Test
    void create_fail_if_productId_is_null() {
        // given
        MenuGroup newMenuGroup = menuGroupRepository.save(createMenuGroup("추천메뉴"));
        MenuCreateRequest request = createMenuCreateRequest("후라이드+후라이드", 19_000L, newMenuGroup.getId(),
                Collections.singletonList(createMenuProductCreateRequest(9999999L, 2)));

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list_success() {
        // given
        Product 후라이드 = productRepository.save(createProduct("후라이드", 10_000L));
        Menu menu = Menu.builder()
                .id(1L)
                .name("후라이드+후라이드")
                .price(BigDecimal.valueOf(19_000L))
                .menuGroupId(1L)
                .menuProducts(new MenuProducts(Collections.singletonList(new MenuProduct(후라이드.getId(), 2))))
                .build();
        Menu savedMenu = menuRepository.save(createMenu());

        // when
        List<MenuFindAllResponse> responses = menuService.list();

        // then
        List<String> menuNames = responses.stream()
                .map(MenuFindAllResponse::getName)
                .collect(Collectors.toList());
        assertThat(menuNames).contains(savedMenu.getName());
    }

    private MenuCreateRequest createMenuCreateRequest(final String name, final Long price, final Long menuGroupId,
                                                      final List<MenuProductCreateRequest> menuProductsRequest) {
        return new MenuCreateRequest(name, BigDecimal.valueOf(price), menuGroupId, menuProductsRequest);
    }

    private MenuProductCreateRequest createMenuProductCreateRequest(final Long productId, final long quantity) {
        return new MenuProductCreateRequest(productId, quantity);
    }
}
