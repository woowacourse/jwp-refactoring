package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.menu.MenuRequest;
import kitchenpos.application.dto.menu.MenuResponse;
import kitchenpos.application.dto.menu.ProductQuantityDto;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.support.DataDependentIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends DataDependentIntegrationTest {

    private static final long NOT_EXIST_ID = Long.MAX_VALUE;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴를 생성, 저장한다.")
    @Test
    void create_success() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final Product product = productRepository.save(new Product("product", BigDecimal.valueOf(1000L)));
        final MenuRequest menuRequest = new MenuRequest("menu", BigDecimal.valueOf(500L), menuGroup.getId(), List.of(
            new ProductQuantityDto(product.getId(), 2)
        ));

        // when
        final MenuResponse savedMenu = menuService.create(menuRequest);

        // then
        assertAll(
            () -> assertThat(savedMenu.getId()).isNotNull(),
            () -> assertThat(savedMenu.getName()).isEqualTo("menu"),
            () -> assertThat(savedMenu.getMenuProducts()).usingRecursiveComparison()
            .isEqualTo(List.of(new ProductQuantityDto(product.getId(), 2)))
        );
    }

    @DisplayName("메뉴를 저장 시, 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void create_price_fail() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final MenuRequest menuRequest = new MenuRequest("menu", BigDecimal.valueOf(-1L), menuGroup.getId(), Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 저장 시, 메뉴 그룹이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void create_menuGroup_fail() {
        // given
        final MenuRequest menuRequest = new MenuRequest("menu", BigDecimal.valueOf(500L), NOT_EXIST_ID, Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 저장 시, 메뉴의 상품이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void create_menuProduct_fail() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));

        final MenuRequest menuRequest = new MenuRequest("menu", BigDecimal.valueOf(500L), menuGroup.getId(), List.of(
            new ProductQuantityDto(NOT_EXIST_ID, 2)
        ));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 저장 시, 메뉴의 총 가격이 상품 총 가격보다 크다면 예외가 발생한다.")
    @Test
    void create_menuTotalPrice_fail() {
        // given
        final Product product = productRepository.save(new Product("product", BigDecimal.valueOf(1000L)));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final MenuRequest menuRequest = new MenuRequest("menu", BigDecimal.valueOf(2001L), menuGroup.getId(), List.of(
            new ProductQuantityDto(product.getId(), 2)
        ));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final Product product = productRepository.save(new Product("product", BigDecimal.valueOf(1000L)));
        final MenuRequest menuRequest = new MenuRequest("menu", BigDecimal.valueOf(1000L), menuGroup.getId(), List.of(
            new ProductQuantityDto(product.getId(), 2L)
        ));
        final MenuResponse menuResponse = menuService.create(menuRequest);

        // when
        final List<MenuResponse> foundMenus = menuService.list();

        // then
        assertThat(foundMenus).usingRecursiveComparison()
            .comparingOnlyFields("id")
            .isEqualTo(List.of(menuResponse));
    }
}
