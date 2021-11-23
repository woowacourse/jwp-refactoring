package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeGuestRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.ui.dto.request.OrderTableRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderTableFixture {

    private final OrderTableRepository orderTableRepository;

    public OrderTableFixture(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableCreateRequest 주문_테이블_생성_요청(int numberOfGuests, boolean empty) {
        return new OrderTableCreateRequest(numberOfGuests, empty);
    }

    public OrderTableRequest 주문_테이블_요청(Long id, int numberOfGuests, boolean empty) {
        return new OrderTableRequest(id, numberOfGuests, empty);
    }

    public OrderTableChangeEmptyRequest 주문_테이블_Empty_Change_요청(boolean empty) {
        return new OrderTableChangeEmptyRequest(empty);
    }

    public OrderTableChangeGuestRequest 주문_테이블_인원_변경_요청(int numberOfGuests) {
        return new OrderTableChangeGuestRequest(numberOfGuests);
    }

    public List<OrderTableRequest> 주문_테이블_요청_리스트_생성(OrderTableRequest... orderTableRequests) {
        return Arrays.asList(orderTableRequests);
    }

    public List<OrderTableResponse> 주문_테이블_응답_리스트_생성(OrderTableResponse... orderTableResponses) {
        return Arrays.asList(orderTableResponses);
    }

    public OrderTable 주문_테이블_조회(Long id) {
        return orderTableRepository.getOne(id);
    }

    public List<OrderTable> 주문_테이블_리스트_조회_ByIds(List<Long> ids) {
        return orderTableRepository.findAllByIdIn(ids);
    }
}
