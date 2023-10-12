package kitchenpos.acceptance.menu;

import static kitchenpos.acceptance.AcceptanceSteps.응답_상태를_검증한다;
import static kitchenpos.acceptance.menu.MenuAcceptanceSteps.메뉴_등록_요청을_보낸다;
import static kitchenpos.acceptance.menu.MenuAcceptanceSteps.메뉴_정보;
import static kitchenpos.acceptance.menu.MenuAcceptanceSteps.메뉴_조회_요청_결과를_검증한다;
import static kitchenpos.acceptance.menu.MenuAcceptanceSteps.메뉴_조회_요청을_보낸다;
import static kitchenpos.acceptance.menu.MenuAcceptanceSteps.메뉴에_속한_상품;
import static kitchenpos.acceptance.menu.MenuGroupAcceptanceSteps.메뉴_그릅_등록후_생성된_ID를_가져온다;
import static kitchenpos.acceptance.product.ProductAcceptanceSteps.상품_등록후_생성된_ID를_가져온다;

import java.util.List;
import kitchenpos.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
public class MenuAcceptanceTest {

    @Nested
    class 메뉴_등록_API extends AcceptanceTest {

        @Test
        void 메뉴를_등록한다() {
            // given
            var 상품1_ID = 상품_등록후_생성된_ID를_가져온다("상품1", 10_000);
            var 상품2_ID = 상품_등록후_생성된_ID를_가져온다("상품2", 20_000);
            var 세트_상품_메뉴_그룹_ID = 메뉴_그릅_등록후_생성된_ID를_가져온다("세트 상품");

            // when
            var 응답 = 메뉴_등록_요청을_보낸다(
                    세트_상품_메뉴_그룹_ID,
                    "말랑 메뉴",
                    10_000,
                    메뉴에_속한_상품(상품1_ID, 2),
                    메뉴에_속한_상품(상품2_ID, 3)
            );

            // then
            응답_상태를_검증한다(응답, 201);
        }

        @Test
        void 메뉴가_메뉴_그룹에_속하지_않으면_오류() {
            // given
            var 상품1_ID = 상품_등록후_생성된_ID를_가져온다("상품1", 10_000);

            // when
            var 응답 = 메뉴_등록_요청을_보낸다(
                    null,
                    "말랑 메뉴",
                    10_000,
                    메뉴에_속한_상품(상품1_ID, 2)
            );

            // then
            응답_상태를_검증한다(응답, 500);
        }

        @Test
        void 메뉴의_가격이_메뉴에_속한_상품들의_가격의_총합보다_크다면_오류() {
            // given
            var 상품1_ID = 상품_등록후_생성된_ID를_가져온다("상품1", 10_000);
            var 상품2_ID = 상품_등록후_생성된_ID를_가져온다("상품2", 20_000);
            var 세트_상품_메뉴_그룹_ID = 메뉴_그릅_등록후_생성된_ID를_가져온다("세트 상품");

            // when
            var 응답 = 메뉴_등록_요청을_보낸다(
                    세트_상품_메뉴_그룹_ID,
                    "말랑 메뉴",
                    80_001,
                    메뉴에_속한_상품(상품1_ID, 2),
                    메뉴에_속한_상품(상품2_ID, 3)
            );

            // then
            응답_상태를_검증한다(응답, 500);
        }
    }

    @Nested
    class 메뉴_조회_API extends AcceptanceTest {

        @Test
        void 메뉴들을_조회한다() {
            // given
            var 상품1_ID = 상품_등록후_생성된_ID를_가져온다("상품1", 10_000);
            var 상품2_ID = 상품_등록후_생성된_ID를_가져온다("상품2", 20_000);
            var 세트_상품_메뉴_그룹_ID = 메뉴_그릅_등록후_생성된_ID를_가져온다("세트 상품");
            메뉴_등록_요청을_보낸다(
                    세트_상품_메뉴_그룹_ID,
                    "말랑 메뉴",
                    10_000,
                    메뉴에_속한_상품(상품1_ID, 1),
                    메뉴에_속한_상품(상품2_ID, 1)
            );
            메뉴_등록_요청을_보낸다(
                    세트_상품_메뉴_그룹_ID,
                    "말랑 메뉴 2",
                    20_000,
                    메뉴에_속한_상품(상품1_ID, 2),
                    메뉴에_속한_상품(상품2_ID, 3)
            );

            // when
            var 응답 = 메뉴_조회_요청을_보낸다();

            // then
            메뉴_조회_요청_결과를_검증한다(응답, List.of(
                    메뉴_정보(
                            세트_상품_메뉴_그룹_ID,
                            "말랑 메뉴",
                            10_000,
                            메뉴에_속한_상품(상품1_ID, 1),
                            메뉴에_속한_상품(상품2_ID, 1)
                    ),
                    메뉴_정보(
                            세트_상품_메뉴_그룹_ID,
                            "말랑 메뉴 2",
                            20_000,
                            메뉴에_속한_상품(상품1_ID, 2),
                            메뉴에_속한_상품(상품2_ID, 3)
                    )
            ));
        }
    }
}
