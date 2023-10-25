package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.order.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.order.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.order.application.dto.OrderTableCreateRequest;
import kitchenpos.order.application.dto.OrderTableResponse;
import kitchenpos.order.application.OrderTableService;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderTableServiceTest {

    private OrderTableService orderTableService;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        orderTableService = new OrderTableService(orderTableRepository);
    }

    @Test
    void 주문_테이블_생성할_수_있다() {
        OrderTableCreateRequest request = new OrderTableCreateRequest(0, true);
        OrderTableResponse orderTableResponse = orderTableService.create(request);
        Assertions.assertThat(orderTableRepository.findById(orderTableResponse.getId())).isPresent();
    }

    @Test
    void 주문_테이블_전체_조회할_수_있다() {
        OrderTableCreateRequest request = new OrderTableCreateRequest(0, true);
        orderTableService.create(request);

        List<OrderTableResponse> orderTableResponses = orderTableService.list();

        Assertions.assertThat(orderTableResponses.size()).isGreaterThan(0);
    }

    @Test
    void 주문_테이블_Id가_존재하지_않는경우_예외가_발생한다() {
        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(true);
        assertThatThrownBy(() -> orderTableService.changeEmpty(0L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("주문 테이블 ID가 존재하지 않습니다.");
    }

    @Test
    void 주문_테이블_그룹_Id가_존재하는_경우_예외가_발생한다() {
        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);
        assertThatThrownBy(() -> orderTableService.changeEmpty(9L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 그룹이 존재하는 경우 테이블 상태를 수정할 수 없습니다.");
    }

    @Test
    void 주문_테이블_아이디_주문상태가_존재하는_경우_예외가_발생한다() {
        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);
        assertThatThrownBy(() -> orderTableService.changeEmpty(9L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 그룹이 존재하는 경우 테이블 상태를 수정할 수 없습니다.");
    }

    @Test
    void 주문_테이블_상태를_빈_테이블로_변경할_수_있다() {
        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);
        OrderTableResponse response = orderTableService.changeEmpty(1L, request);

        OrderTable orderTable = orderTableRepository.findById(response.getId()).orElseThrow();
        Assertions.assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    void 게스트_수는_0명_이하일_수_없다() {
        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(-1);
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수일 수 없습니다.");
    }

    @Test
    void 주문_테이블_Id가_존재하지_않는_경우_예외가_발생한다() {
        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(-1);
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(0L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("주문 테이블 ID가 존재하지 않습니다.");
    }

    @Test
    void 주문_테이블_방문한_손님_수_변경을_할_수_있다() {
        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(-1);
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(0L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("주문 테이블 ID가 존재하지 않습니다.");
    }
}
