package kitchenpos.ui;

import io.restassured.response.ValidatableResponse;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;

class MenuRestControllerTest extends AcceptanceTest {
    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        get("/api/menus").then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("name", hasItems("후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨"))
                .body("price", hasItems(16000, 16000, 16000, 16000, 17000, 17000));
    }

    @Test
    void create() {
        // given
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 2L);
        MenuRequest menuRequest = new MenuRequest("후라이드두마리", 32000L, 2L, Arrays.asList(menuProductRequest));

        // when
        ValidatableResponse response = post("/api/menus", menuRequest).then();

        // then
        response.assertThat().statusCode(HttpStatus.CREATED.value());

        MenuResponse menuResponse = response.extract().as(MenuResponse.class);
        assertThat(menuResponse.getName()).isEqualTo(menuRequest.getName());
        assertThat(menuResponse.getPrice()).isEqualTo(menuRequest.getPrice());
    }
}
