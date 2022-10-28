package kitchenpos.e2e;

import static kitchenpos.e2e.E2eTest.AssertionPair.row;
import static kitchenpos.support.MenuGroupFixture.간장_양념_세_마리_메뉴;
import static kitchenpos.support.MenuGroupFixture.단짜_두_마리_메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

public class MenuGroupE2eTest extends E2eTest {

    @Test
    void create() {

        // given & when
        final ExtractableResponse<Response> 응답 = POST_요청(MENU_GROUP_URL, 단짜_두_마리_메뉴);

        // then
        final MenuGroup 저장된_메뉴그룹 = 응답.body().as(MenuGroup.class);

        assertAll(
                () -> assertThat(저장된_메뉴그룹.getId()).isNotNull(),
                단일_검증(저장된_메뉴그룹.getName(), 단짜_두_마리_메뉴.getName())
        );
    }

    @Test
    void list() {

        // given & when
        POST_요청(MENU_GROUP_URL, 단짜_두_마리_메뉴);
        POST_요청(MENU_GROUP_URL, 간장_양념_세_마리_메뉴);

        // then
        final ExtractableResponse<Response> 응답 = GET_요청(MENU_GROUP_URL);
        final List 상품_리스트 = 응답.body().as(List.class);

        assertAll(
                리스트_검증(상품_리스트,
                        row("id", 1, 2),
                        row("name", 단짜_두_마리_메뉴.getName(), 간장_양념_세_마리_메뉴.getName())
                )
        );
    }
}
