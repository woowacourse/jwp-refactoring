package kitchenpos.application;

import kitchenpos.application.fixture.OrderServiceFixture;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceTest extends OrderServiceFixture {

    @Autowired
    private OrderService orderService;

    @Nested
    class 메뉴_주문 {

        @Test
        void 메뉴를_주문한다() {
            메뉴를_주문한다_픽스처_생성();

            final Order actual = orderService.create(주문_생성_요청_dto);

            assertThat(actual.getId()).isPositive();
        }

        @Test
        void 주문_항목이_1개_미만이면_예외가_발생한다() {
            주문_항목이_1개_미만이면_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> orderService.create(주문항목이_1개_미만인_주문_생성_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목에서_입력받은_메뉴가_올바른_메뉴가_아니라면_예외가_발생한다() {
            주문_항목에서_입력받은_메뉴가_올바른_메뉴가_아니라면_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> orderService.create(주문항목이_2개인_주문_생성_요청_dto));
        }

        @Test
        void 유효하지_않은_주문_테이블_아이디라면_예외가_발생한다() {
            유효하지_않은_주문_테이블_아이디라면_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> orderService.create(유효하지_않은_주문_테이블_아이디를_갖는_주문_생성_요청_dto));
        }

        @Test
        void 주문_테이블_아이디에_해당하는_주문_테이블이_empty_table이라면_예외가_발생한다() {
            주문_테이블_아이디에_해당하는_주문_테이블이_empty_table이라면_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> orderService.create(주문_불가능_상태의_주문_테이블_생성_요청_dto));
        }
    }

    @Nested
    class 주문내역_조회 {

        @Test
        void 모든_주문내역을_조회한다() {
            모든_주문내역을_조회한다_픽스처_생성();

            final List<Order> actual = orderService.list();

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(2);
                softAssertions.assertThat(actual.get(0)).isEqualTo(조회할_주문_1);
                softAssertions.assertThat(actual.get(1)).isEqualTo(조회할_주문_2);
            });
        }
    }

    @Nested
    class 주문상태_변경 {

        @Test
        void 주문_상태를_변경한다() {
            주문_상태를_변경한다_픽스처_생성();

            final Order actual = orderService.changeOrderStatus(식사중에서_완료로_상태를_변경할_주문.getId(), 주문_상태_변경_요청_dto);

            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        }

        @Test
        void 주문_상태가_COMPLETION인_경우_상태를_변경하면_예외가_발생한다() {
            완료된_상태의_주문을_완료된_상태로_변경하는_경우_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> orderService.changeOrderStatus(COMPLETION_상태의_주문.getId(), 주문_상태_변경_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 유효하지_않은_주문_번호를_입력한_경우_예외가_발생한다() {
            유효하지_않은_주문_번호를_입력한_경우_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> orderService.changeOrderStatus(유효하지_않은_주문_아이디, 완료_상태인_주문_변경_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_status가_잘못_입력된_경우_예외가_발생한다() {
            order_status가_잘못_입력된_경우_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> orderService.changeOrderStatus(식사중에서_완료로_상태를_변경할_주문.getId(), 잘못된_상태로_수정하고자_하는_주문_변경_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
