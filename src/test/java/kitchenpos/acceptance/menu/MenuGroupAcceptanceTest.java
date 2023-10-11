package kitchenpos.acceptance.menu;


import static kitchenpos.acceptance.AcceptanceSteps.응답_상태를_검증한다;
import static kitchenpos.acceptance.menu.MenuGroupAcceptanceSteps.메뉴_그룹_정보;
import static kitchenpos.acceptance.menu.MenuGroupAcceptanceSteps.메뉴_그룹_조회_결과를_검증한다;
import static kitchenpos.acceptance.menu.MenuGroupAcceptanceSteps.메뉴_그룹_조회_요청을_보낸다;
import static kitchenpos.acceptance.menu.MenuGroupAcceptanceSteps.메뉴_그릅_등록_요청을_보낸다;

import java.util.List;
import kitchenpos.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("메뉴 인수 테스트")
public class MenuGroupAcceptanceTest {

    @Nested
    class 메뉴_그룹_등록_API extends AcceptanceTest {

        @Test
        void 메뉴_그룹을_등록한다() {
            // when
            var 응답 = 메뉴_그릅_등록_요청을_보낸다("단일 상품");

            // then
            응답_상태를_검증한다(응답, 201);
        }
    }

    @Nested
    class 메뉴_그룸_조회_API extends AcceptanceTest {

        @Test
        void 메뉴_그룹들을_조회한다() {
            // given
            메뉴_그릅_등록_요청을_보낸다("단일 상품");
            메뉴_그릅_등록_요청을_보낸다("두개 상품");

            // when
            var 응답 = 메뉴_그룹_조회_요청을_보낸다();

            // then
            메뉴_그룹_조회_결과를_검증한다(응답,
                    List.of(메뉴_그룹_정보("단일 상품"), 메뉴_그룹_정보("두개 상품"))
            );
        }
    }
}
