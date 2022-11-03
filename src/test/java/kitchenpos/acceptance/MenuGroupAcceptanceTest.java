package kitchenpos.acceptance;

import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.application.dto.MenuGroupDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("신규 메뉴그룹을 생성할 수 있다")
    void createMenuGroup() {
        final String menuGroupName = "사이드";
        final ExtractableResponse<Response> response = 메뉴_그룹_등록_요청(menuGroupName);

        final MenuGroupDto responseBody = response.as(MenuGroupDto.class);

        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.CREATED),
                () -> 단일_데이터_검증(responseBody.getName(), menuGroupName)
        );
    }

    @Test
    @DisplayName("전체 메뉴그룹을 조회할 수 있다")
    void getMenuGroups() {
        // given
        final MenuGroupDto menuGroup1 = 메뉴_그룹_등록("사이드");
        final MenuGroupDto menuGroup2 = 메뉴_그룹_등록("와인");

        // when
        final ExtractableResponse<Response> response = 모든_메뉴_그룹_조회_요청();
        final List<MenuGroupDto> responseBody = response.body()
                .jsonPath()
                .getList(".", MenuGroupDto.class);

        // then
        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.OK),
                () -> 리스트_데이터_검증(responseBody, "id", menuGroup1.getId(), menuGroup2.getId()),
                () -> 리스트_데이터_검증(responseBody, "name", menuGroup1.getName(), menuGroup2.getName())
        );
    }
}
