package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import kitchenpos.step.MenuGroupStep;
import kitchenpos.ui.request.MenuGroupRequest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.step.MenuGroupStep.MENU_GROUP_REQUEST_일식;
import static kitchenpos.step.MenuGroupStep.MENU_GROUP_REQUEST_한식;
import static kitchenpos.step.MenuGroupStep.메뉴_그룹_생성_요청;
import static kitchenpos.step.MenuGroupStep.메뉴_그룹_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void 메뉴_그룹을_생성한다() {
        final MenuGroupRequest request = MENU_GROUP_REQUEST_일식;
        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(request);

        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(request.getName());
    }

    @Test
    void 메뉴_그룹을_조회한다() {
        final List<MenuGroupRequest> requests = List.of(MENU_GROUP_REQUEST_일식, MENU_GROUP_REQUEST_한식);
        requests.forEach(MenuGroupStep::메뉴_그룹_생성_요청);

        final List<MenuGroup> menuGroups = requests.stream()
                .map(MenuGroupRequest::toMenuGroup)
                .collect(Collectors.toList());

        final ExtractableResponse<Response> response = 메뉴_그룹_조회_요청();
        final List<MenuGroup> result = response.jsonPath().getList("", MenuGroup.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(result.size()).isEqualTo(requests.size()),
                () -> assertThat(result.get(0))
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(menuGroups.get(0)),
                () -> assertThat(result.get(1))
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(menuGroups.get(1))
        );
    }
}
