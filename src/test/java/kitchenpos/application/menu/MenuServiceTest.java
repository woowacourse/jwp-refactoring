package kitchenpos.application.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.request.MenuCreateRequest;
import kitchenpos.dto.menu.request.MenuProductCreateRequest;
import kitchenpos.dto.menu.response.MenuResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_생성할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(new MenuGroup("메뉴 그룹"))
                .getId();
        Long productId = productRepository.save(new Product("상품", BigDecimal.valueOf(1_000)))
                .getId();
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(productId, 1);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴", BigDecimal.ZERO, menuGroupId,
                List.of(menuProductCreateRequest));

        MenuResponse menuResponse = menuService.create(menuCreateRequest);

        assertAll(
                () -> assertThat(menuResponse.getId()).isNotNull(),
                () -> assertThat(menuResponse.getPrice().compareTo(BigDecimal.ZERO)).isZero(),
                () -> assertThat(menuResponse.getMenuProducts()).hasSize(1)
        );
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_예외를_반환한다() {
        MenuCreateRequest request = new MenuCreateRequest("메뉴", BigDecimal.ZERO, 0L, new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_상품이_메뉴에_포함되어_있으면_예외를_반환한다() {
        Long menuGroupId = menuGroupRepository.save(new MenuGroup("메뉴 그룹"))
                .getId();
        MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(0L, 1);
        MenuCreateRequest menuRequest = new MenuCreateRequest("메뉴", BigDecimal.valueOf(2_000), menuGroupId,
                List.of(menuProductRequest));

        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_메뉴를_조회할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(new MenuGroup("메뉴 그룹"))
                .getId();
        Long menuId1 = menuService.create(new MenuCreateRequest("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();
        Long menuId2 = menuService.create(new MenuCreateRequest("메뉴", BigDecimal.ZERO, menuGroupId, new ArrayList<>()))
                .getId();

        List<MenuResponse> actual = menuService.list();

        assertThat(actual).hasSize(2)
                .extracting("id")
                .containsExactly(menuId1, menuId2);
    }
}
