package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menu.ProductQuantityRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

@Import(MenuService.class)
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuService menuService;

    private MenuGroup menuGroup;

    private ProductQuantityRequest productQuantityRequest;

    @BeforeEach
    void setUp() {
        final Product product = productRepository.save(new Product("매콤치킨", BigDecimal.valueOf(16000)));
        menuGroup = menuGroupRepository.save(new MenuGroup("이십마리메뉴"));

        final Menu menu = menuRepository.save(new Menu("마늘간장치킨", BigDecimal.valueOf(16000), menuGroup));
        final MenuProduct menuProduct = menuProductRepository.save(new MenuProduct(menu, product, 1L));

        productQuantityRequest = new ProductQuantityRequest(menuProduct.getProductId(), menuProduct.getQuantity());
    }

    @DisplayName("create: 메뉴 생성")
    @Test
    void create() {
        final MenuRequest menuRequest = new MenuRequest("후라이드치킨", BigDecimal.valueOf(16000), menuGroup.getId(), Collections.singletonList(productQuantityRequest));

        final MenuResponse actual = menuService.create(menuRequest);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("후라이드치킨"),
                () -> assertThat(actual.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(16000)),
                () -> assertThat(actual.getMenuGroupId()).isEqualTo(menuGroup.getId()),
                () -> assertThat(actual.getMenuProductResponses()).isNotEmpty()
        );

    }

    @DisplayName("create: MenuGroup의 Id가 존재하지 않을 때 예외 처리")
    @Test
    void create_IfMenuGroupIdDoesNotExist_Exception() {
        final MenuRequest menuRequest = new MenuRequest("후라이드치킨", BigDecimal.valueOf(16000),0L, Collections.singletonList(productQuantityRequest));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 상품의 Id가 존재하지 않을 때 예외 처리")
    @Test
    void create_IfProductIdDoesNotExist_InMenuProduct_Exception() {
        final ProductQuantityRequest menuProductNotExistId = new ProductQuantityRequest(0L, 1);
        final MenuRequest menuRequest = new MenuRequest("후라이드치킨", BigDecimal.valueOf(16000), menuGroup.getId(), Collections.singletonList(menuProductNotExistId));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 가격이 메뉴상품의 가격의 총 합보다 클 때 예외 처리")
    @Test
    void create_IfPriceIsGreaterThanSumOfMenuProduct_Exception() {
        final MenuRequest menuRequest = new MenuRequest("후라이드치킨", BigDecimal.valueOf(Integer.MAX_VALUE), menuGroup.getId(), Collections.singletonList(productQuantityRequest));

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("list: 메뉴 전체 조회")
    @Test
    void list() {
        final MenuRequest menuRequest1 = new MenuRequest("후라이드치킨", BigDecimal.valueOf(16000), menuGroup.getId(), Collections.singletonList(productQuantityRequest));
        final MenuRequest menuRequest2 = new MenuRequest("양념치킨", BigDecimal.valueOf(16000), menuGroup.getId(), Collections.singletonList(productQuantityRequest));
        menuService.create(menuRequest1);
        menuService.create(menuRequest2);

        final List<MenuResponse> actual = menuService.list();

        assertThat(actual).isNotEmpty();
    }
}