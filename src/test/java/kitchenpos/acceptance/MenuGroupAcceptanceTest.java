package kitchenpos.acceptance;

import io.restassured.response.ValidatableResponse;
import kitchenpos.domain.MenuGroup;
import kitchenpos.support.RequestBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 그룹 관련 api")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        // given, when
        final MenuGroup request = RequestBuilder.ofMenuGroup();
        final ValidatableResponse response = post("/api/menu-groups", request);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.notNullValue());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        // given, when
        final ValidatableResponse response = get("/api/menu-groups");

        // then
        response.statusCode(HttpStatus.OK.value());
    }

}
