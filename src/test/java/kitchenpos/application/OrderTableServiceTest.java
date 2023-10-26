package kitchenpos.application;

import kitchenpos.Product.repository.ProductRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.order.presentation.dto.OrderTableCreateRequest;
import kitchenpos.order.presentation.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.order.presentation.dto.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.TestFixtureFactory.새로운_주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;

class OrderTableServiceTest extends ServiceTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Test
    void 테이블을_등록한다() {
        OrderTableCreateRequest 주문_테이블_생성_요청 = new OrderTableCreateRequest(1, false);

        OrderTable orderTable = tableService.create(주문_테이블_생성_요청);

        assertThat(orderTable.getId()).isNotNull();
    }

    @Test
    void 테이블_목록을_조회한다() {
        OrderTable 주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, false));
        OrderTable 빈_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));

        List<OrderTable> orderTables = tableService.findAll();

        assertThat(orderTables).hasSize(2);
    }

    @Test
    void 테이블_상태를_변경한다() {
        OrderTable 주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));

        OrderTableUpdateEmptyRequest 주문_테이블_상태_변경_요청 = new OrderTableUpdateEmptyRequest(false);
        OrderTable orderTable = tableService.changeEmpty(주문_테이블.getId(), 주문_테이블_상태_변경_요청);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    void 방문한_손님_수를_입력한다() {
        OrderTable 주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, false));

        OrderTableUpdateNumberOfGuestsRequest 방문한_손님_수_입력_요청 = new OrderTableUpdateNumberOfGuestsRequest(3);

        OrderTable orderTable = tableService.changeNumberOfGuests(주문_테이블.getId(), 방문한_손님_수_입력_요청);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);
    }
}
