package kitchenpos.acceptance;

import static kitchenpos.support.DomainFixture.세트_메뉴;
import static kitchenpos.support.DomainFixture.인기_메뉴;
import static kitchenpos.support.MenuGroupRestAssuredFixture.메뉴_그룹_목록_조회_요청;
import static kitchenpos.support.MenuGroupRestAssuredFixture.메뉴_그룹_생성_요청;
import static kitchenpos.support.SimpleRestAssured.toObjectList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.ui.dto.MenuGroupCreateRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import kitchenpos.support.AcceptanceTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void 메뉴_그룹_생성을_요청한다() {
        // given
        final var request = new MenuGroupCreateRequest(인기_메뉴.getName());

        // when
        final var response = 메뉴_그룹_생성_요청(request);

        // then
        final var created = response.as(MenuGroupResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location").split("/menu-groups/")[1]).isNotNull(),
                () -> assertThat(created.getId()).isNotNull(),
                () -> assertThat(created.getName()).isEqualTo(request.getName())
        );
    }

    @Test
    void 메뉴_그룹_목록_조회를_요청한다() {
        // given
        메뉴_그룹_생성_요청( new MenuGroupCreateRequest(인기_메뉴.getName()));
        메뉴_그룹_생성_요청( new MenuGroupCreateRequest(세트_메뉴.getName()));

        // when
        final var response = 메뉴_그룹_목록_조회_요청();

        // then
        final var found = toObjectList(response, MenuGroupResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(found).hasSize(2)
        );
    }
}
