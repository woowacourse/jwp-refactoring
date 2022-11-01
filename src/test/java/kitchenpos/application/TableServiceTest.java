package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import kitchenpos.RepositoryTest;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.request.OrderTableRequest;
import kitchenpos.table.application.response.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class TableServiceTest {

    private TableService sut;
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        sut = new TableService(orderTableRepository);
        orderService = new OrderService(menuRepository, orderRepository, orderTableRepository);
    }

    @DisplayName("새로운 주문 테이블을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final OrderTableRequest request = new OrderTableRequest(0, true);

        // when
        final OrderTableResponse response = sut.create(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        final OrderTable foundOrderTable = orderTableRepository.findById(response.getId()).get();
        assertThat(response)
                .extracting(OrderTableResponse::getTableGroupId, OrderTableResponse::getNumberOfGuests, OrderTableResponse::isEmpty)
                .containsExactly(foundOrderTable.getTableGroupId(), foundOrderTable.getNumberOfGuests(), foundOrderTable.isEmpty());
    }

    @DisplayName("전체 주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        // when
        final List<OrderTableResponse> responses = sut.list();

        // then
        assertThat(responses)
                .hasSize(8)
                .extracting(OrderTableResponse::getNumberOfGuests, OrderTableResponse::isEmpty)
                .containsExactlyInAnyOrder(
                        tuple(0, true), tuple(0, true),
                        tuple(0, true), tuple(0, true),
                        tuple(0, true), tuple(0, true),
                        tuple(0, true), tuple(0, true)
                );
    }

    @DisplayName("주문 테이블의 조회결과가 없는 경우 테이블을 빈 상태로 변경할 수 없다.")
    @Test
    void changeEmptyWithEmptyOrderTable() {
        // given
        final long invalidOrderTableId = -1L;
        final OrderTableRequest orderTableRequest = new OrderTableRequest(0, false);

        // when & then
        assertThatThrownBy(() -> sut.changeEmpty(invalidOrderTableId, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
