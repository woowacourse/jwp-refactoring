package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import kitchenpos.step.MenuGroupStep;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.일식;
import static kitchenpos.fixture.MenuGroupFixture.한식;
import static kitchenpos.step.MenuGroupStep.toRequest;
import static kitchenpos.step.MenuGroupStep.메뉴_그룹_생성_요청;
import static kitchenpos.step.MenuGroupStep.메뉴_그룹_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void 메뉴_그룹을_생성한다() {
        final MenuGroup menuGroup = 일식();

        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(toRequest(menuGroup));

        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(menuGroup.getName());
    }

    @Test
    void 메뉴_그룹을_조회한다() {
        final List<MenuGroup> menuGroups = List.of(일식(), 한식());
        menuGroups.stream().map(MenuGroupStep::toRequest).forEach(MenuGroupStep::메뉴_그룹_생성_요청);

        final ExtractableResponse<Response> response = 메뉴_그룹_조회_요청();
        final List<MenuGroup> result = response.jsonPath().getList("", MenuGroup.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(result.size()).isEqualTo(menuGroups.size()),
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
