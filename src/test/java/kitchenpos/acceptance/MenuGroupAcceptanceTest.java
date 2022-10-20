package kitchenpos.acceptance;

import static kitchenpos.KitchenPosFixtures.네_마리_메뉴_생성요청;
import static kitchenpos.KitchenPosFixtures.메뉴그룹_URL;
import static kitchenpos.KitchenPosFixtures.세_마리_메뉴_생성요청;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuGroupAcceptanceTest extends AcceptanceTest {
    @Test
    void 신규_메뉴그룹을_생성할_수_있다() {
        // given & when
        final var 메뉴그룹_생성응답 = 생성요청(메뉴그룹_URL, 세_마리_메뉴_생성요청);
        final var 생성된_메뉴그룹 = 메뉴그룹_생성응답.body().as(MenuGroupResponse.class);

        // then
        assertAll(
                응답일치(메뉴그룹_생성응답, HttpStatus.CREATED),
                단일_데이터_검증(생성된_메뉴그룹.getId(), 1L),
                단일_데이터_검증(생성된_메뉴그룹.getName(), 세_마리_메뉴_생성요청.getName())
        );
    }

    @Test
    void 전체_메뉴그룹을_조회할_수_있다() {
        // given
        생성요청(메뉴그룹_URL, 세_마리_메뉴_생성요청);
        생성요청(메뉴그룹_URL, 네_마리_메뉴_생성요청);

        // when
        final var 메뉴그룹_조회응답 = 조회요청(메뉴그룹_URL);
        final var 메뉴그룹_응답_데이터 = 메뉴그룹_조회응답.body().as(List.class);

        // then
        assertAll(
                응답일치(메뉴그룹_조회응답, HttpStatus.OK),
                리스트_데이터_검증(메뉴그룹_응답_데이터, "id", 1, 2),
                리스트_데이터_검증(메뉴그룹_응답_데이터, "name", 세_마리_메뉴_생성요청.getName(), 네_마리_메뉴_생성요청.getName())
        );
    }
}
