package kitchenpos.application;

import java.time.LocalDateTime;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Order 매장에서 발생하는 주문입니다.  `테이블 번호`, `주문 상태`, `주문 시간`, 주문에 속하는 수량이 있는 `메뉴`를 가지고 있습니다.
 * <p>
 * `create`(주문을 생성할 수 있습니다.) 주문 메뉴가 *null* 이면 예외를 반환합니다.
 * <p>
 * `없는 메뉴를 주문한 경우` 예외를 반환합니다.
 * <p>
 * 주문한 *테이블이 존재하지 않으면* 예외를 반환합니다.
 * <p>
 * `findAll` (전체 주문을 조회할 수 있습니다.)
 * <p>
 * `changeOrderStatus` (주문 상태를 변경할 수 있습니다.)
 * <p>
 * `없는 주문의 상태를 변경`하려고 하면 예외를 반환합니다.
 * <p>
 * `Completion 상태의 주문`인데, 변경을 시도할 경우 예외를 반환합니다.
 */

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void 주문메뉴가_null_이면_예외를_반환한다() {
        final Order 주문 = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);

        Assertions.assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 없는_메뉴를_주문_할_경우_예외를_반환한다() {
        final Order 주문 = 주문_생성(OrderStatus.COOKING);

        메뉴존재유뮤세팅(0L);

        Assertions.assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("없는 메뉴가 포함된 주문입니다.");
    }

    @Test
    void 없는_테이블에_주문_할_경우_예외를_반환한다() {
        final Order 주문 = 주문_생성(OrderStatus.COOKING);

        메뉴존재유뮤세팅(1L);
        존재하지않는_테이블_세팅();

        Assertions.assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("없는 테이블에 대한 주문입니다.");
    }

    @Test
    void 이미_완료된_주문의_상태를_변경_할_경우_예외를_반환한다() {

        final Order 주문 = 주문_생성(OrderStatus.COOKING);

        메뉴존재유뮤세팅(1L);
        존재하는_테이블_세팅();
        완료된_주문_조회();

        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(1L, 주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 완료된 주문입니다.");
    }
}
