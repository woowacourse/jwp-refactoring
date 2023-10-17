package kitchenpos.acceptance.table;

import static kitchenpos.acceptance.AcceptanceSteps.응답_상태를_검증한다;
import static kitchenpos.acceptance.AcceptanceSteps.조회_요청_결과를_검증한다;
import static kitchenpos.acceptance.menu.MenuAcceptanceSteps.메뉴_등록후_생성된_ID를_받아온다;
import static kitchenpos.acceptance.menu.MenuAcceptanceSteps.메뉴에_속한_상품;
import static kitchenpos.acceptance.menu.MenuGroupAcceptanceSteps.메뉴_그릅_등록후_생성된_ID를_가져온다;
import static kitchenpos.acceptance.oreder.OrderAcceptanceSteps.주문_생성_요청을_보낸다;
import static kitchenpos.acceptance.product.ProductAcceptanceSteps.상품_등록후_생성된_ID를_가져온다;
import static kitchenpos.acceptance.table.TableAcceptanceSteps.비어있음;
import static kitchenpos.acceptance.table.TableAcceptanceSteps.비어있지_않음;
import static kitchenpos.acceptance.table.TableAcceptanceSteps.테이블_등록_요청을_보낸다;
import static kitchenpos.acceptance.table.TableAcceptanceSteps.테이블_등록후_생성된_ID를_가져온다;
import static kitchenpos.acceptance.table.TableAcceptanceSteps.테이블_손님_수_변경_요청을_보낸다;
import static kitchenpos.acceptance.table.TableAcceptanceSteps.테이블_조회_요청을_보낸다;
import static kitchenpos.acceptance.table.TableAcceptanceSteps.테이블의_비어있음_상태_변경_요청을_보낸다;
import static kitchenpos.acceptance.table.TableGroupAcceptanceSteps.테이블_그룹_등록_요청을_보낸다;

import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.acceptance.oreder.OrderAcceptanceSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("테이블 인수테스트")
public class TableAcceptanceTest {

    @Nested
    class 테이블_추가_인수테스트 {

        @Test
        void 빈_테이블을_등록한다() {
            // when
            var 응답 = 테이블_등록_요청을_보낸다(5, 비어있음);

            // then
            응답_상태를_검증한다(응답, 201);
        }

        @Test
        void 비어있지_않은_테이블을_등록한다() {
            // when
            var 응답 = 테이블_등록_요청을_보낸다(0, 비어있지_않음);

            // then
            응답_상태를_검증한다(응답, 201);
        }
    }

    @Nested
    class 테이블_상태_변경_API {

        @Nested
        class 테이블을_비어있는_상태로_만드는_경우 {

            @Test
            void 테이블을_비어있는_상태로_만든다() {
                // given
                var 비어있는_테이블_ID = 테이블_등록후_생성된_ID를_가져온다(0, 비어있지_않음);

                // when
                var 응답 = 테이블의_비어있음_상태_변경_요청을_보낸다(비어있는_테이블_ID, 비어있음);

                // then
                응답_상태를_검증한다(응답, 200);
            }

            @Test
            void 대상_테이블의_계산이_완료되지_않았다면_비어있는_상태로_바꿀_수_없다() {
                // given
                var 상품1_ID = 상품_등록후_생성된_ID를_가져온다("상품1", 10_000);
                var 메뉴_그룹_ID = 메뉴_그릅_등록후_생성된_ID를_가져온다("단일 상품");
                var 말랑_메뉴_ID = 메뉴_등록후_생성된_ID를_받아온다(메뉴_그룹_ID,
                        "메뉴", 1_000, 메뉴에_속한_상품(상품1_ID, 1)
                );
                var 테이블_ID_1 = 테이블_등록후_생성된_ID를_가져온다(0, 비어있지_않음);
                주문_생성_요청을_보낸다(테이블_ID_1, OrderAcceptanceSteps.주문_항목_요청(말랑_메뉴_ID, 2));

                // when
                var 응답 = 테이블의_비어있음_상태_변경_요청을_보낸다(테이블_ID_1, 비어있음);

                // given
                응답_상태를_검증한다(응답, 500);
            }
        }

        @Test
        void 테이블을_비어있지_않은_상태로_만든다() {
            // given
            var 비어있지_않은_테이블_ID = 테이블_등록후_생성된_ID를_가져온다(0, 비어있지_않음);

            // when
            var 응답 = 테이블의_비어있음_상태_변경_요청을_보낸다(비어있지_않은_테이블_ID, 비어있음);

            // then
            응답_상태를_검증한다(응답, 200);
        }

        @Test
        void 대상_테이블이_테이블_그룹에_속해있다면_상태를_변경할_수_없다() {
            // given
            var 테이블_ID_1 = 테이블_등록후_생성된_ID를_가져온다(0, 비어있음);
            var 테이블_ID_2 = 테이블_등록후_생성된_ID를_가져온다(2, 비어있음);
            테이블_그룹_등록_요청을_보낸다(테이블_ID_1, 테이블_ID_2);

            // when
            var 응답 = 테이블의_비어있음_상태_변경_요청을_보낸다(테이블_ID_1, 비어있음);

            // then
            응답_상태를_검증한다(응답, 500);
        }
    }

    @Nested
    class 테이블_손님_수_변경_API {

        @Test
        void 비어있지_않은_테이블의_손님_수를_변경한다() {
            // given
            var 비어있지_않은_테이블_ID = 테이블_등록후_생성된_ID를_가져온다(0, 비어있지_않음);

            // when
            var 응답 = 테이블_손님_수_변경_요청을_보낸다(비어있지_않은_테이블_ID, 10);

            // then
            응답_상태를_검증한다(응답, 200);
        }

        @Test
        void 비어있는_테이블은_손님_수를_변경할_수_없다() {
            // given
            var 비어있는_테이블_ID = 테이블_등록후_생성된_ID를_가져온다(0, 비어있음);

            // when
            var 응답 = 테이블_손님_수_변경_요청을_보낸다(비어있는_테이블_ID, 0);

            // then
            응답_상태를_검증한다(응답, 500);
        }

        @Test
        void 사람의_수가_0명보다_작으면_오류이다() {
            // given
            var 비어있지_않은_테이블_ID = 테이블_등록후_생성된_ID를_가져온다(0, 비어있지_않음);

            // when
            var 응답 = 테이블_손님_수_변경_요청을_보낸다(비어있지_않은_테이블_ID, -1);

            // then
            응답_상태를_검증한다(응답, 500);
        }
    }

    @Nested
    class 테이블_조회_API extends AcceptanceTest {

        @Test
        void 테이블들을_조회한다() {
            // given
            테이블_등록_요청을_보낸다(5, 비어있음);
            테이블_등록_요청을_보낸다(0, 비어있지_않음);

            // when
            var 응답 = 테이블_조회_요청을_보낸다();

            // then
            조회_요청_결과를_검증한다(응답,
                    "[\n"
                            + "    {\n"
                            + "        \"id\": 1,\n"
                            + "        \"tableGroupId\": null,\n"
                            + "        \"numberOfGuests\": 5,\n"
                            + "        \"empty\": true\n"
                            + "    },\n"
                            + "    {\n"
                            + "        \"id\": 2,\n"
                            + "        \"tableGroupId\": null,\n"
                            + "        \"numberOfGuests\": 0,\n"
                            + "        \"empty\": false\n"
                            + "    }\n"
                            + "]"
            );
        }
    }
}
