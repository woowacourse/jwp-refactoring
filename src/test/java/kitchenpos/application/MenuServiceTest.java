package kitchenpos.application;

import static kitchenpos.support.TestFixtureFactory.메뉴_그룹을_생성한다;
import static kitchenpos.support.TestFixtureFactory.상품을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.request.MenuProductCreateRequest;
import kitchenpos.ui.dto.response.MenuCreateResponse;
import kitchenpos.ui.dto.response.MenuResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class MenuServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_생성할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long productId = productRepository.save(상품을_생성한다("상품", BigDecimal.valueOf(1_000)))
                .getId();
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(productId, 1);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴", BigDecimal.ZERO, menuGroupId,
                List.of(menuProductCreateRequest));

        MenuCreateResponse menuCreateResponse = menuService.create(menuCreateRequest);

        assertAll(
                () -> assertThat(menuCreateResponse.getId()).isNotNull(),
                () -> assertThat(menuCreateResponse.getPrice().compareTo(BigDecimal.ZERO)).isZero(),
                () -> assertThat(menuCreateResponse.getMenuProducts()).hasSize(1)
        );
    }

    @Test
    void 메뉴_가격이_0원_미만이면_예외를_반환한다() {
        assertThatThrownBy(
                () -> menuService.create(new MenuCreateRequest("메뉴", BigDecimal.valueOf(-1), 1L, new ArrayList<>()))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_예외를_반환한다() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴", BigDecimal.ZERO, 0L, new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_상품이_메뉴에_포함되어_있으면_예외를_반환한다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(0L, 1);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴", BigDecimal.valueOf(2_000), menuGroupId,
                List.of(menuProductCreateRequest));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_메뉴_상품_가격의_합보다_크면_예외를_반환한다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long productId = productRepository.save(상품을_생성한다("상품", BigDecimal.valueOf(1_000)))
                .getId();
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(productId, 1);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴", BigDecimal.valueOf(1_001), menuGroupId,
                List.of(menuProductCreateRequest));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_메뉴를_조회할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
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
