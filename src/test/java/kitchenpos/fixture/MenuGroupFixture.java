package kitchenpos.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.request.CreateMenuGroupRequest;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    private MenuGroupFixture() {
    }

    public static class REQUEST {

        public static CreateMenuGroupRequest 메뉴_그룹_치킨_생성_요청() {
            return CreateMenuGroupRequest.builder()
                    .name("치킨")
                    .build();
        }
    }

    public static Long 메뉴_그룹_생성(CreateMenuGroupRequest request) {
        ExtractableResponse<Response> response = RestAssured.given()
                .body(request)
                .contentType(ContentType.JSON)
                .when().post("/api/menu-groups")
                .then().log().all()
                .statusCode(201)
                .extract();
        return Long.parseLong(response.header("Location").split("/")[3]);
    }
}
