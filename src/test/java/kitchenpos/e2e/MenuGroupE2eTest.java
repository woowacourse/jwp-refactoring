package kitchenpos.e2e;

import static kitchenpos.support.MenuGroupFixture.간장_양념_세_마리_메뉴;
import static kitchenpos.support.MenuGroupFixture.단짜_두_마리_메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static kitchenpos.support.AssertionsSupport.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuGroupE2eTest extends E2eTest {

    @Test
    void create() {

        // given
        final ExtractableResponse<Response> 응답 = POST_요청(MENU_GROUP_URL, 단짜_두_마리_메뉴);

        // when
        final MenuGroup 저장된_메뉴그룹 = 응답.body().as(MenuGroup.class);

        // then
        assertAll(
                HTTP_STATUS_검증(HttpStatus.CREATED, 응답),
                () -> assertThat(저장된_메뉴그룹.getId()).isNotNull(),
                단일_검증(저장된_메뉴그룹.getName(), 단짜_두_마리_메뉴.getName())
        );
    }

    @Test
    void list() {

        // given
        POST_요청(MENU_GROUP_URL, 단짜_두_마리_메뉴);
        POST_요청(MENU_GROUP_URL, 간장_양념_세_마리_메뉴);

        // when
        final ExtractableResponse<Response> 응답 = GET_요청(MENU_GROUP_URL);
        final List<Product> 상품_리스트 = extractHttpBody(응답);

        // then
        assertAll(
                HTTP_STATUS_검증(HttpStatus.OK, 응답),
                리스트_검증(상품_리스트, "id", 1, 2),
                리스트_검증(상품_리스트, "name", 단짜_두_마리_메뉴.getName(), 간장_양념_세_마리_메뉴.getName())
        );
    }
}
