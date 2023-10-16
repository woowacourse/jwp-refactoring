package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {


    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupDao tableGroupDao;

    @Nested
    class 단체_테이블을_등록할_때 {

        @Test
        void success() {
        }

        @Test
        void 테이블이_비어있거나_2개_미만이면_실패() {
        }

        @Test
        void 등록되지_않은_주문테이블이_존재할_경우_실패() {
        }

        @Test
        void 주문_테이블이_이미_주문을_받았거나_다른_단체_테이블에_등록된_경우_실패() {

        }
    }

    @Nested
    class 단체_테이블을_해제할_때 {

        @Test
        void success() {
        }


        @Test
        void 주문_상태가_조리_중이거나_식사_중인_경우_실패() {
        }

    }

}
