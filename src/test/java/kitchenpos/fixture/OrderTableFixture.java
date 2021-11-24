package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.testtool.RequestBuilder;
import kitchenpos.table.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.table.ui.dto.request.OrderTableChangeGuestRequest;
import kitchenpos.table.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.table.ui.dto.request.OrderTableRequest;
import kitchenpos.table.ui.dto.response.OrderTableResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class OrderTableFixture extends DefaultFixture {

    private final OrderTableRepository orderTableRepository;

    public OrderTableFixture(RequestBuilder requestBuilder,
            OrderTableRepository orderTableRepository) {
        super(requestBuilder);
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

    public OrderTableResponse 주문_테이블_등록(OrderTableCreateRequest request) {
        return request()
                .post("/api/tables", request)
                .build()
                .convertBody(OrderTableResponse.class);
    }

    public List<OrderTableResponse> 주문_테이블_리스트_조회() {
        return request()
                .get("/api/tables")
                .build()
                .convertBodyToList(OrderTableResponse.class);
    }

    public OrderTableResponse 주문_테이블_착석(
            Long orderTableId,
            OrderTableChangeEmptyRequest request
    ) {
        return request()
                .put("/api/tables/" + orderTableId + "/empty", request)
                .build()
                .convertBody(OrderTableResponse.class);
    }

    public OrderTableResponse 주문_테이블_인원_변경(
            Long orderTableId,
            OrderTableChangeGuestRequest request
    ) {
        return request()
                .put("/api/tables/" + orderTableId + "/number-of-guests", request)
                .build()
                .convertBody(OrderTableResponse.class);
    }
}
