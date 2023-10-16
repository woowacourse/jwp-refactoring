package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderLineItemDao orderLineItemDao;
    @Autowired
    private OrderTableDao orderTableDao;

    @Nested
    class 주문을_생성할_때 {

        @Test
        void success() {

        }

        @Test
        void 주문_상품이_비어있으면_실패() {

        }

        @Test
        void 메뉴에_등록되지_않은_주문_상품이_있으면_실패() {

        }

        @Test
        void 등록되지_않은_주문테이블이면_실패() {

        }

        @Test
        void 주문_테이블이_비어있으면_실패() {

        }

    }

    @Test
    void 주문_목록_조회() {

    }

    @Nested
    class 주문_상태를_변경할_때 {

        @Test
        void success() {

        }

        @Test
        void 등록되어있지_않은_주문이면_실패() {

        }

        @Test
        void 주문_상태가_완료이면_실패() {

        }


    }
}
