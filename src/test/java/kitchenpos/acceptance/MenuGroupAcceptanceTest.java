package kitchenpos.acceptance;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹을 관리한다.")
    @TestFactory
    Stream<DynamicTest> manageMenuGroup() {
        return Stream.of(
                dynamicTest("메뉴 그룹을 생성한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 메뉴_그룹을_생성한다("메뉴 그룹");

                    // then
                    상태코드를_검증한다(response, HttpStatus.CREATED);
                    필드가_Null이_아닌지_검증한다(response, "id");
                }),
                dynamicTest("모든 메뉴 그룹을 조회한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 모든_메뉴_그룹을_조회한다();

                    // then
                    assertAll(() -> {
                        상태코드를_검증한다(response, HttpStatus.OK);
                        리스트_길이를_검증한다(response, ".", 1);
                    });
                })
        );
    }

    ExtractableResponse<Response> 메뉴_그룹을_생성한다(final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return post("/api/menu-groups", menuGroup);
    }

    ExtractableResponse<Response> 모든_메뉴_그룹을_조회한다() {
        return get("/api/menu-groups");
    }
}
