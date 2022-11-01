package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴_그룹을_생성한다;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품을_생성한다;
import static kitchenpos.acceptance.support.RequestUtil.get;
import static kitchenpos.acceptance.support.RequestUtil.post;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.domain.menu.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

public class MenuAcceptanceTest extends AcceptanceTest {

    static ExtractableResponse<Response> 메뉴를_생성한다(final String name, final int price, final long menuGroupId,
                                                  final List<Map<String, Long>> menuProducts) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("price", price);
        body.put("menuGroupId", menuGroupId);
        body.put("menuProducts", menuProducts.stream()
                .map(map -> Map.of("productId", map.get("productId"), "quantity", map.get("quantity")))
                .collect(Collectors.toList()));

        return post("/api/menus", body);
    }

    static ExtractableResponse<Response> 모든_메뉴를_조회한다() {
        return get("/api/menus");
    }

    @DisplayName("메뉴를 관리한다.")
    @TestFactory
    Stream<DynamicTest> manageMenu() {
        // given
        MenuGroup 치킨 = 메뉴_그룹을_생성한다("치킨").as(MenuGroup.class);

        Long 후라이드_ID = 상품을_생성한다("후라이드", 19_000).jsonPath().getLong("id");
        Long 양념_ID = 상품을_생성한다("양념", 19_000).jsonPath().getLong("id");
        Long 간장_ID = 상품을_생성한다("간장", 19_000).jsonPath().getLong("id");

        return Stream.of(
                dynamicTest("메뉴를 생성한다.", () -> {
                    // when
                    Map<String, Long> menuProduct1 = Map.of("productId", 후라이드_ID, "quantity", 1L);
                    Map<String, Long> menuProduct2 = Map.of("productId", 양념_ID, "quantity", 1L);
                    Map<String, Long> menuProduct3 = Map.of("productId", 간장_ID, "quantity", 1L);

                    ExtractableResponse<Response> response = 메뉴를_생성한다("후라이드+후라이드", 35_000, 치킨.getId(), List.of(
                            menuProduct1, menuProduct2, menuProduct3
                    ));

                    // then
                    상태코드를_검증한다(response, HttpStatus.CREATED);
                    필드가_Null이_아닌지_검증한다(response, "id");
                }),
                dynamicTest("모든 메뉴를 조회한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 모든_메뉴를_조회한다();

                    // then
                    상태코드를_검증한다(response, HttpStatus.OK);
                    리스트_길이를_검증한다(response, ".", 1);
                    리스트_길이를_검증한다(response, "[0].menuProducts", 3);
                    필드가_Null이_아닌지_검증한다(response, "[0].menuProducts[0].seq");
                })
        );
    }
}
