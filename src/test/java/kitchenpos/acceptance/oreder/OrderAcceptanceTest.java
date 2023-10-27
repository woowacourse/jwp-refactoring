package kitchenpos.acceptance.oreder;

import static kitchenpos.acceptance.AcceptanceSteps.응답_상태를_검증한다;
import static kitchenpos.acceptance.menu.MenuAcceptanceSteps.메뉴_등록후_생성된_ID를_받아온다;
import static kitchenpos.acceptance.menu.MenuAcceptanceSteps.메뉴에_속한_상품;
import static kitchenpos.acceptance.menu.MenuGroupAcceptanceSteps.메뉴_그릅_등록후_생성된_ID를_가져온다;
import static kitchenpos.acceptance.oreder.OrderAcceptanceSteps.주문;
import static kitchenpos.acceptance.oreder.OrderAcceptanceSteps.주문_상태_변경_요청을_보낸다;
import static kitchenpos.acceptance.oreder.OrderAcceptanceSteps.주문_생성_요청을_보낸다;
import static kitchenpos.acceptance.oreder.OrderAcceptanceSteps.주문_생성후_ID를_가져온다;
import static kitchenpos.acceptance.oreder.OrderAcceptanceSteps.주문_조회_결과를_검증한다;
import static kitchenpos.acceptance.oreder.OrderAcceptanceSteps.주문_조회_요청을_보낸다;
import static kitchenpos.acceptance.oreder.OrderAcceptanceSteps.주문_항목;
import static kitchenpos.acceptance.oreder.OrderAcceptanceSteps.주문_항목_요청;
import static kitchenpos.acceptance.product.ProductAcceptanceSteps.상품_등록후_생성된_ID를_가져온다;
import static kitchenpos.acceptance.table.TableAcceptanceSteps.비어있음;
import static kitchenpos.acceptance.table.TableAcceptanceSteps.비어있지_않음;
import static kitchenpos.acceptance.table.TableAcceptanceSteps.테이블_등록후_생성된_ID를_가져온다;
import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.MenuProductSnapShot;
import kitchenpos.order.domain.MenuSnapShot;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("주문 인수테스트")
@SuppressWarnings("NonAsciiCharacters")
public class OrderAcceptanceTest {

    private final Product 상품1 = new Product("상품1", BigDecimal.valueOf(10_000));
    private final Product 상품2 = new Product("상품2", BigDecimal.valueOf(20_000));
    private final MenuGroup 세트_상품_메뉴_그룹 = new MenuGroup("세트 상품");

    private Long 상품1_ID;
    private Long 상품2_ID;
    private Long 세트_상품_메뉴_그룹_ID;
    private Long 말랑_메뉴_ID;
    private Long 말랑_메뉴_2_ID;
    private Long 테이블_1_ID;
    private Long 테이블_2_ID;

    private Menu 말랑_메뉴_1;
    private Menu 말랑_메뉴_2;
    private MenuSnapShot 말랑_메뉴_1_스냅샷;
    private MenuSnapShot 말랑_메뉴_2_스냅샷;

    private void settingData() {
        상품1_ID = 상품_등록후_생성된_ID를_가져온다("상품1", 10_000);
        상품2_ID = 상품_등록후_생성된_ID를_가져온다("상품2", 20_000);
        세트_상품_메뉴_그룹_ID = 메뉴_그릅_등록후_생성된_ID를_가져온다("세트 상품");
        말랑_메뉴_1 = new Menu(
                "말랑 메뉴",
                BigDecimal.valueOf(1_000),
                세트_상품_메뉴_그룹,
                List.of(new MenuProduct(상품1_ID, 2))
        );
        말랑_메뉴_2 = new Menu(
                "말랑 메뉴 2",
                BigDecimal.valueOf(20_000),
                세트_상품_메뉴_그룹,
                List.of(new MenuProduct(상품1_ID, 2),
                        new MenuProduct(상품2_ID, 3)
                )
        );

        말랑_메뉴_1_스냅샷 = new MenuSnapShot(
                세트_상품_메뉴_그룹.getName(),
                "말랑 메뉴",
                BigDecimal.valueOf(1_000),
                List.of(new MenuProductSnapShot(상품1.getName(), 상품1.getPrice(), 2))
        );
        말랑_메뉴_2_스냅샷 = new MenuSnapShot(
                세트_상품_메뉴_그룹.getName(),
                "말랑 메뉴 2",
                BigDecimal.valueOf(20_000),
                List.of(
                        new MenuProductSnapShot(상품1.getName(), 상품1.getPrice(), 2),
                        new MenuProductSnapShot(상품2.getName(), 상품2.getPrice(), 3)
                )
        );

        말랑_메뉴_ID = 메뉴_등록후_생성된_ID를_받아온다(
                세트_상품_메뉴_그룹_ID,
                말랑_메뉴_1.getName(),
                말랑_메뉴_1.getPrice().intValue(),
                메뉴에_속한_상품(
                        상품1_ID,
                        말랑_메뉴_1.getMenuProducts().get(0).getQuantity()
                )
        );
        말랑_메뉴_2_ID = 메뉴_등록후_생성된_ID를_받아온다(
                세트_상품_메뉴_그룹_ID,
                말랑_메뉴_2.getName(),
                말랑_메뉴_2.getPrice().intValue(),
                메뉴에_속한_상품(
                        상품1_ID,
                        말랑_메뉴_2.getMenuProducts().get(0).getQuantity()
                ),
                메뉴에_속한_상품(
                        상품2_ID,
                        말랑_메뉴_2.getMenuProducts().get(1).getQuantity()
                )
        );
        테이블_1_ID = 테이블_등록후_생성된_ID를_가져온다(2, 비어있지_않음);
        테이블_2_ID = 테이블_등록후_생성된_ID를_가져온다(3, 비어있지_않음);
    }

    @Nested
    class 주문_생성_API extends AcceptanceTest {

        @BeforeEach
        protected void setUp() {
            super.setUp();
            settingData();
        }

        @Test
        void 주문을_생성한다() {
            // when
            var 응답 = 주문_생성_요청을_보낸다(테이블_1_ID,
                    주문_항목_요청(말랑_메뉴_ID, 2),
                    주문_항목_요청(말랑_메뉴_2_ID, 1)
            );

            // then
            응답_상태를_검증한다(응답, 201);
        }

        @Test
        void 비어있는_테이블은_주문할_수_없다() {
            // given
            var 비어있는_테이블_ID = 테이블_등록후_생성된_ID를_가져온다(0, 비어있음);

            // when
            var 응답 = 주문_생성_요청을_보낸다(비어있는_테이블_ID,
                    주문_항목_요청(말랑_메뉴_ID, 2),
                    주문_항목_요청(말랑_메뉴_2_ID, 1)
            );

            // then
            응답_상태를_검증한다(응답, 500);
        }

        @Test
        void 주문_목록이_비어있으면_오류이다() {
            // when
            var 응답 = 주문_생성_요청을_보낸다(테이블_1_ID);

            // then
            응답_상태를_검증한다(응답, 500);
        }
    }

    @Nested
    class 주문_상태_변경_API extends AcceptanceTest {

        @BeforeEach
        protected void setUp() {
            super.setUp();
            settingData();
        }

        @Test
        void 주문의_상태를_변경한다() {
            // given
            var 주문_ID = 주문_생성후_ID를_가져온다(테이블_1_ID,
                    주문_항목_요청(말랑_메뉴_ID, 2),
                    주문_항목_요청(말랑_메뉴_2_ID, 1)
            );

            // when
            var 응답 = 주문_상태_변경_요청을_보낸다(주문_ID, MEAL);

            // then
            응답_상태를_검증한다(응답, 200);
            var 조회_응답 = 주문_조회_요청을_보낸다();
            주문_조회_결과를_검증한다(
                    조회_응답,
                    주문(주문_ID, 테이블_1_ID, MEAL,
                            주문_항목(말랑_메뉴_1_스냅샷, 2),
                            주문_항목(말랑_메뉴_2_스냅샷, 1)
                    )
            );
        }

        @Test
        void 계산_완료된_주문의_상태는_변경할_수_없다() {
            // given
            var 주문_ID = 주문_생성후_ID를_가져온다(테이블_1_ID,
                    주문_항목_요청(말랑_메뉴_ID, 2),
                    주문_항목_요청(말랑_메뉴_2_ID, 1)
            );
            주문_상태_변경_요청을_보낸다(주문_ID, COMPLETION);

            // when
            var 응답 = 주문_상태_변경_요청을_보낸다(주문_ID, MEAL);

            // then
            응답_상태를_검증한다(응답, 500);
            var 조회_응답 = 주문_조회_요청을_보낸다();
            주문_조회_결과를_검증한다(
                    조회_응답,
                    주문(주문_ID, 테이블_1_ID, COMPLETION,
                            주문_항목(말랑_메뉴_1_스냅샷, 2),
                            주문_항목(말랑_메뉴_2_스냅샷, 1)
                    )
            );
        }
    }

    @Nested
    class 주문_조회_API extends AcceptanceTest {

        @BeforeEach
        protected void setUp() {
            super.setUp();
            settingData();
        }

        @Test
        void 주문들을_조회한다() {
            // given
            Long 주문1_ID = 주문_생성후_ID를_가져온다(테이블_1_ID,
                    주문_항목_요청(말랑_메뉴_ID, 2),
                    주문_항목_요청(말랑_메뉴_2_ID, 1)
            );
            Long 주문2_ID = 주문_생성후_ID를_가져온다(테이블_2_ID,
                    주문_항목_요청(말랑_메뉴_ID, 3),
                    주문_항목_요청(말랑_메뉴_2_ID, 2)
            );

            // when
            var 응답 = 주문_조회_요청을_보낸다();

            // then
            주문_조회_결과를_검증한다(
                    응답,
                    주문(주문1_ID, 테이블_1_ID, COOKING,
                            주문_항목(말랑_메뉴_1_스냅샷, 2),
                            주문_항목(말랑_메뉴_2_스냅샷, 1)
                    ),
                    주문(주문2_ID, 테이블_2_ID, COOKING,
                            주문_항목(말랑_메뉴_1_스냅샷, 3),
                            주문_항목(말랑_메뉴_2_스냅샷, 2)
                    )
            );
        }
    }
}
