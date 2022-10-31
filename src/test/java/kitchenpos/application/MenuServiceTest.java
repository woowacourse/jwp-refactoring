package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.MenuFixtures;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuServiceTest {

    private MenuService menuService;

    @Autowired
    public MenuServiceTest(MenuService menuService) {
        this.menuService = menuService;
    }

    @Test
    void create() {
        // given
        MenuCreateRequest request = MenuFixtures.createMenuCreateRequest();
        // when
        MenuResponse response = menuService.create(request);
        // then
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void createMenuWithNullPrice() {
        // given
        MenuCreateRequest request = MenuFixtures.createMenuCreateRequest((BigDecimal) null);

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createMenuWithNegativePrice() {
        // given
        MenuCreateRequest request = MenuFixtures.createMenuCreateRequest(BigDecimal.valueOf(-1L));

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createMenuWithInvalidMenuGroup() {
        // given
        long invalidMenuGroupId = 999L;
        MenuCreateRequest request = MenuFixtures.createMenuCreateRequest(invalidMenuGroupId);

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createMenuWithInvalidProduct() {
        // given
        long invalidProductId = 999L;
        MenuCreateRequest request = MenuFixtures.createMenuCreateRequest(
                List.of(MenuFixtures.createMenuProductCreateRequest(invalidProductId, 3))
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createMenuWithMoreExpensivePrice() {
        // given
        int expensivePrice = 100_000;
        MenuCreateRequest request = MenuFixtures.createMenuCreateRequest(BigDecimal.valueOf(expensivePrice));

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given & when
        List<MenuResponse> responses = menuService.list();
        // then
        int defaultSize = 6;
        assertThat(responses).hasSize(defaultSize);
    }
}
