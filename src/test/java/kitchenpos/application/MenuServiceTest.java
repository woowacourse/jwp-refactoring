package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴 가격이 null인 경우, 예외를 발생한다")
    @Test
    void null_price_exception() {
        MenuCreateRequest request = new MenuCreateRequest("뿌링클", null, 1L, new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 null이 아니고 0보다 크거나 같아야 합니다.");
    }

    @DisplayName("메뉴 가격이 0보다 작은 경우, 예외를 발생한다")
    @Test
    void negative_price_exception() {
        final MenuCreateRequest request = new MenuCreateRequest("뿌링클", BigDecimal.valueOf(-1), 1L, new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 null이 아니고 0보다 크거나 같아야 합니다.");
    }

    @DisplayName("존재하지 않은 메뉴 그룹인 경우, 예외를 발생한다")
    @Test
    void does_not_exist_menu_group_exception() {
        final MenuCreateRequest request = new MenuCreateRequest("뿌링클", BigDecimal.valueOf(18000), -1L,
                new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    @DisplayName("존재하지 않은 상품인 경우, 예외를 발생한다")
    @Test
    void does_not_exist_product_exception() {
        final MenuProductCreateRequest notExistProduct = new MenuProductCreateRequest(0L, 1L);

        final MenuCreateRequest request = new MenuCreateRequest("뿌링클", BigDecimal.valueOf(18000), 1L,
                Collections.singletonList(notExistProduct));

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    @DisplayName("메뉴 가격이 상품의 가격 총합보다 큰 경우, 예외를 발생한다")
    @Test
    void menu_price_more_expensive_than_sum_of_product_exception() {
        final MenuProductCreateRequest product = new MenuProductCreateRequest(1L, 2L);

        final MenuCreateRequest request = new MenuCreateRequest("뿌링클", BigDecimal.valueOf(180000), 1L,
                Collections.singletonList(product));

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품의 총 합과 메뉴의 총 합이 같지 않습니다.");
    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void create() {
        final MenuProductCreateRequest menuProduct = new MenuProductCreateRequest(1L, 2L);

        final MenuCreateRequest request = new MenuCreateRequest("뿌링클", BigDecimal.valueOf(18000), 1L,
                Collections.singletonList(menuProduct));

        final MenuResponse response = menuService.create(request);

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(request.getName()),
                () -> assertThat(response.getPrice()).isEqualByComparingTo(request.getPrice()),
                () -> assertThat(response.getMenuGroupId()).isEqualTo(request.getMenuGroupId())
        );
    }

    @DisplayName("메뉴 전체 목록을 조회한다")
    @Test
    void findAll() {
        final List<MenuResponse> menuResponses = menuService.list();

        assertThat(menuResponses).hasSize(6);
    }
}
