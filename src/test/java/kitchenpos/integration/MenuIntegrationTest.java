package kitchenpos.integration;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuCreateRequestDto;
import kitchenpos.dto.MenuCreateResponseDto;
import kitchenpos.dto.MenuReadResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class MenuIntegrationTest extends IntegrationTest {

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    public void list() {
        fixtureMaker.createMenu();
        webTestClient.get().uri("/api/menus")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(MenuReadResponseDto.class);
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    public void create() {
        MenuGroup menuGroup = fixtureMaker.createMenuGroup();
        List<MenuProduct> menuProducts = fixtureMaker.createMenuProducts(menuGroup.getId());
        Menu menu = new Menu("메뉴", new BigDecimal(1000), menuGroup.getId(), menuProducts);
        MenuCreateRequestDto menuCreateRequestDto = new MenuCreateRequestDto(menu);

        webTestClient.post().uri("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(menuCreateRequestDto), MenuCreateRequestDto.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(MenuCreateResponseDto.class);
    }
}
