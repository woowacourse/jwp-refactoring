package kitchenpos.application;

import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuServiceTest extends ServiceTest {

    private MenuRequest menuRequest;

    @BeforeEach
    void setUp() {
        menuRequest = makeMenuRequest();
    }

    @Test
    void 메뉴를_생성한다() {

        MenuResponse saved = menuService.create(menuRequest);

        assertAll(
                () -> assertThat(saved.getName()).isEqualTo(menuRequest.getName()),
                () -> assertThat(saved.getPrice()).isEqualTo(menuRequest.getPrice()),
                () -> assertThat(saved.getMenuGroup().getId()).isEqualTo(menuRequest.getMenuGroupId())
        );
    }

    @Test
    void 전체_메뉴를_조회한다() {
        List<MenuResponse> responses = menuService.list();
        assertThat(responses.size()).isEqualTo(6);
    }

    private MenuRequest makeMenuRequest() {
        return new MenuRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                1L,
                List.of(new MenuProductRequest(1L, 2L)));
    }
}
