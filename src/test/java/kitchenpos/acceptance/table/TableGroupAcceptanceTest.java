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
import static kitchenpos.acceptance.table.TableAcceptanceSteps.테이블_등록후_생성된_ID를_가져온다;
import static kitchenpos.acceptance.table.TableAcceptanceSteps.테이블_조회_요청을_보낸다;
import static kitchenpos.acceptance.table.TableGroupAcceptanceSteps.테이블_그룹_등록_요청을_보낸다;
import static kitchenpos.acceptance.table.TableGroupAcceptanceSteps.테이블_그룹_등록후_생성된_ID를_가져온다;
import static kitchenpos.acceptance.table.TableGroupAcceptanceSteps.테이블_그룹_제거_요청을_보낸다;

import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.acceptance.oreder.OrderAcceptanceSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("테이블 그룹 인수테스트")
public class TableGroupAcceptanceTest {

    @Nested
    class 테이블_그룹_생성_API extends AcceptanceTest {

        @Test
        void 여러_테이블들을_하나의_테이블_그룹으로_묶는다() {
            // given
            var 테이블_ID_1 = 테이블_등록후_생성된_ID를_가져온다(0, 비어있음);
            var 테이블_ID_2 = 테이블_등록후_생성된_ID를_가져온다(2, 비어있음);

            // when
            var 응답 = 테이블_그룹_등록_요청을_보낸다(테이블_ID_1, 테이블_ID_2);

            // then
            응답_상태를_검증한다(응답, 201);
        }

        @Test
        void 테이블들은_그룹화되는_동시에_비어있지_않은_상태가_된다() {
            // given
            var 테이블_ID_1 = 테이블_등록후_생성된_ID를_가져온다(0, 비어있음);
            var 테이블_ID_2 = 테이블_등록후_생성된_ID를_가져온다(2, 비어있음);

            // when
            var 테이블_그룹_ID = 테이블_그룹_등록후_생성된_ID를_가져온다(테이블_ID_1, 테이블_ID_2);

            // then
            var 응답 = 테이블_조회_요청을_보낸다();
            조회_요청_결과를_검증한다(응답,
                    "[\n"
                            + "    {\n"
                            + "        \"id\": 1,\n"
                            + "        \"tableGroupId\": " + 테이블_그룹_ID + ",\n"
                            + "        \"numberOfGuests\": 0,\n"
                            + "        \"empty\": false\n"
                            + "    },\n"
                            + "    {\n"
                            + "        \"id\": 2,\n"
                            + "        \"tableGroupId\": " + 테이블_그룹_ID + ",\n"
                            + "        \"numberOfGuests\": 2,\n"
                            + "        \"empty\": false\n"
                            + "    }\n"
                            + "]"
            );
        }

        @Test
        void 그룹화되는_테이블은_모두_비어있는_상태여야_한다() {
            // given
            var 테이블_ID_1 = 테이블_등록후_생성된_ID를_가져온다(0, 비어있음);
            var 테이블_ID_2 = 테이블_등록후_생성된_ID를_가져온다(2, 비어있지_않음);

            // when
            var 응답 = 테이블_그룹_등록_요청을_보낸다(테이블_ID_1, 테이블_ID_2);

            // then
            응답_상태를_검증한다(응답, 500);
        }

        @Test
        void 테이블_그룹에는_최소_2개_이상의_그룹이_묶어야_한다() {
            // given
            var 테이블_ID_1 = 테이블_등록후_생성된_ID를_가져온다(0, 비어있음);

            // when
            var 응답 = 테이블_그룹_등록_요청을_보낸다(테이블_ID_1);

            // then
            응답_상태를_검증한다(응답, 500);
        }
    }

    @Nested
    class 테이블_그룹_제거_API extends AcceptanceTest {

        @Test
        void 테이블_그룹을_제거한다() {
            // given
            var 테이블_ID_1 = 테이블_등록후_생성된_ID를_가져온다(0, 비어있음);
            var 테이블_ID_2 = 테이블_등록후_생성된_ID를_가져온다(2, 비어있음);
            var 테이블_그룹_ID = 테이블_그룹_등록후_생성된_ID를_가져온다(테이블_ID_1, 테이블_ID_2);

            // when
            var 응답 = 테이블_그룹_제거_요청을_보낸다(테이블_그룹_ID);

            // then
            응답_상태를_검증한다(응답, 204);
        }

        @Test
        void 테이블_그룹을_제거하더라도_테이블들은_비어있지_않은_상태이다() {
            // given
            var 테이블_ID_1 = 테이블_등록후_생성된_ID를_가져온다(0, 비어있음);
            var 테이블_ID_2 = 테이블_등록후_생성된_ID를_가져온다(2, 비어있음);
            var 테이블_그룹_ID = 테이블_그룹_등록후_생성된_ID를_가져온다(테이블_ID_1, 테이블_ID_2);

            // when
            테이블_그룹_제거_요청을_보낸다(테이블_그룹_ID);

            // then
            var 응답 = 테이블_조회_요청을_보낸다();
            조회_요청_결과를_검증한다(응답,
                    "[\n"
                            + "    {\n"
                            + "        \"id\": 1,\n"
                            + "        \"tableGroupId\": null,\n"
                            + "        \"numberOfGuests\": 0,\n"
                            + "        \"empty\": false\n"
                            + "    },\n"
                            + "    {\n"
                            + "        \"id\": 2,\n"
                            + "        \"tableGroupId\": null,\n"
                            + "        \"numberOfGuests\": 2,\n"
                            + "        \"empty\": false\n"
                            + "    }\n"
                            + "]"
            );
        }

        @Test
        void 테이블_그룹에_속한_테이블_중_계산_완료되지_않은_테이블이_있는_경우_그룹_제거를_할_수_없다() {
            // given
            var 상품1_ID = 상품_등록후_생성된_ID를_가져온다("상품1", 10_000);
            var 메뉴_그룹_ID = 메뉴_그릅_등록후_생성된_ID를_가져온다("단일 상품");
            var 말랑_메뉴_ID = 메뉴_등록후_생성된_ID를_받아온다(메뉴_그룹_ID,
                    "메뉴", 1_000, 메뉴에_속한_상품(상품1_ID, 1)
            );
            var 테이블_ID_1 = 테이블_등록후_생성된_ID를_가져온다(0, 비어있음);
            var 테이블_ID_2 = 테이블_등록후_생성된_ID를_가져온다(2, 비어있음);
            var 테이블_그룹_ID = 테이블_그룹_등록후_생성된_ID를_가져온다(테이블_ID_1, 테이블_ID_2);
            주문_생성_요청을_보낸다(테이블_ID_1, OrderAcceptanceSteps.주문_항목_요청(말랑_메뉴_ID, 2));

            // when
            var 응답 = 테이블_그룹_제거_요청을_보낸다(테이블_그룹_ID);

            // given
            응답_상태를_검증한다(응답, 500);
        }
    }
}
