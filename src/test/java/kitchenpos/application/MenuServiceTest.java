package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequestDto;
import kitchenpos.dto.MenuProductCreateRequestDto;
import kitchenpos.dto.MenuResponseDto;

class MenuServiceTest extends ServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "백마리치킨"));
        Product product = productDao.save(createProduct(null, "양념치킨", BigDecimal.valueOf(18_000)));
        MenuProductCreateRequestDto menuProductCreateRequest = new MenuProductCreateRequestDto(product.getId(), 1);
        MenuCreateRequestDto menuCreateRequest = new MenuCreateRequestDto("양념치킨", BigDecimal.valueOf(18_000),
            menuGroup.getId(),
            Collections.singletonList(menuProductCreateRequest));

        MenuResponseDto menuResponse = menuService.create(menuCreateRequest);

        assertAll(
            () -> assertThat(menuResponse.getId()).isNotNull(),
            () -> assertThat(menuResponse.getName()).isEqualTo(menuCreateRequest.getName()),
            () -> assertThat(menuResponse.getPrice().longValue()).isEqualTo(
                menuCreateRequest.getPrice().longValue()),
            () -> assertThat(menuResponse.getMenuGroupId()).isEqualTo(menuCreateRequest.getMenuGroupId()),
            () -> assertThat(menuResponse.getMenuProductResponses()).hasSize(1)
        );
    }

    @DisplayName("메뉴 등록 시, 메뉴그룹에 속하지 않은 메뉴는 등록할 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = 1)
    @NullSource
    void create_NonExistingMenuGroup_ThrownException(Long menuGroupId) {
        Product product = productDao.save(createProduct(null, "양념치킨", BigDecimal.valueOf(18_000)));
        MenuProductCreateRequestDto menuProductCreateRequest = new MenuProductCreateRequestDto(product.getId(), 1);
        MenuCreateRequestDto menuCreateRequest = new MenuCreateRequestDto("양념치킨", BigDecimal.valueOf(18_000),
            menuGroupId, Collections.singletonList(menuProductCreateRequest));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시, 메뉴에 속한 상품 금액의 합은 메뉴의 가격보다 크거나 같아야 한다.")
    @Test
    void create_OverSumOfProductsPrice_ThrownException() {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "백마리치킨"));
        Product product = productDao.save(createProduct(null, "양념치킨", BigDecimal.valueOf(18_000)));
        MenuProductCreateRequestDto menuProductCreateRequest = new MenuProductCreateRequestDto(product.getId(), 1);
        MenuCreateRequestDto menuCreateRequest = new MenuCreateRequestDto("양념치킨", BigDecimal.valueOf(18_001),
            menuGroup.getId(), Collections.singletonList(menuProductCreateRequest));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시, 1개 이상의 등록된 상품으로 메뉴를 등록할 수 있다.")
    @ParameterizedTest
    @ValueSource(longs = 1)
    @NullSource
    void create_NonExistingProductId_ThrownException(Long productId) {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "백마리치킨"));
        MenuProductCreateRequestDto menuProductCreateRequest = new MenuProductCreateRequestDto(productId,1);
        MenuCreateRequestDto menuCreateRequest = new MenuCreateRequestDto("양념치킨", BigDecimal.valueOf(18_000),
            menuGroup.getId(), Collections.singletonList(menuProductCreateRequest));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "백마리치킨"));
        Product product = productDao.save(createProduct(null, "양념치킨", BigDecimal.valueOf(18_000)));
        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        Menu menuRequest = createMenu(null, "양념치킨", BigDecimal.valueOf(18_000), menuGroup.getId(),
            Collections.singletonList(menuProduct));
        menuDao.save(menuRequest);

        List<MenuResponseDto> menuResponses = menuService.list();

        assertThat(menuResponses).hasSize(1);
    }
}
