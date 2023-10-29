package kitchenpos.core.application;

import kitchenpos.core.Product.repository.ProductRepository;
import kitchenpos.core.menu.repository.MenuProductRepository;
import kitchenpos.core.menu.repository.MenuRepository;
import kitchenpos.core.menugroup.repository.MenuGroupRepository;
import kitchenpos.core.order.domain.Order;
import kitchenpos.core.order.repository.OrderRepository;
import kitchenpos.core.table.domain.OrderTable;
import kitchenpos.core.table.presentation.dto.OrderTableCreateRequest;
import kitchenpos.core.table.presentation.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.core.table.presentation.dto.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.core.table.repository.OrderTableRepository;
import kitchenpos.core.table.repository.TableGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.TestFixtureFactory.새로운_주문;
import static kitchenpos.TestFixtureFactory.새로운_주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void 빈테이블변경요청시_진행중인_주문이있다면_예외가_발생한다() {
        OrderTable 주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));
        Order 주문1 = orderRepository.save(새로운_주문(주문_테이블));
        Order 주문2 = orderRepository.save(새로운_주문(주문_테이블));

        OrderTableUpdateEmptyRequest 주문_테이블_상태_변경_요청 = new OrderTableUpdateEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), 주문_테이블_상태_변경_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void 방문한_손님_수를_입력한다() {
        OrderTable 주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, false));

        OrderTableUpdateNumberOfGuestsRequest 방문한_손님_수_입력_요청 = new OrderTableUpdateNumberOfGuestsRequest(3);

        OrderTable orderTable = tableService.changeNumberOfGuests(주문_테이블.getId(), 방문한_손님_수_입력_요청);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);
    }
}
