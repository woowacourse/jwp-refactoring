package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductCreateRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.fixture.MenuFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends ServiceTest {
    @Test
    @DisplayName("메뉴를 생성한다.")
    void createMenu() {
        final BigDecimal menuPrice = BigDecimal.valueOf(16000L);
        final MenuCreateRequest request = MenuFixture.createMenuRequest("후라이드치킨", menuPrice, 2L,
                List.of(new MenuProductCreateRequest(1L, 1L)));

        final MenuResponse menuResponse = menuService.create(request);

        assertAll(
                () -> assertThat(menuResponse.getId()).isNotNull(),
                () -> assertThat(menuResponse.getName()).isEqualTo("후라이드치킨"),
                () -> assertThat(menuResponse.getPrice()).isEqualByComparingTo(menuPrice)
        );
    }

    @Test
    @DisplayName("메뉴 그룹이 올바르지 않은 경우 예외 발생")
    void whenInvalidMenuGroup() {
        long invalidMenuGroupId = 99999L;
        final MenuCreateRequest request = MenuFixture.createMenuRequest(invalidMenuGroupId);

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 목록을 가져온다.")
    void getList() {
        final List<MenuResponse> menus = menuService.list();

        assertThat(menus).hasSize(2);
    }
}
