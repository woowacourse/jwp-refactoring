package kitchenpos.application.jpa;

import java.util.List;
import kitchenpos.domain.entity.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.jpa.dto.ordertable.ChangeEmptyRequest;
import kitchenpos.ui.jpa.dto.ordertable.ChangeEmptyResponse;
import kitchenpos.ui.jpa.dto.ordertable.ChangeNumberOfGuestsRequest;
import kitchenpos.ui.jpa.dto.ordertable.ChangeNumberOfGuestsResponse;
import kitchenpos.ui.jpa.dto.ordertable.OrderTableCreateRequest;
import kitchenpos.ui.jpa.dto.ordertable.OrderTableCreateResponse;
import kitchenpos.ui.jpa.dto.ordertable.OrderTableListResponse;
import org.springframework.stereotype.Service;

@Service
public class TableServiceJpa {

    private OrderTableRepository orderTableRepository;

    public TableServiceJpa(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableCreateResponse create(OrderTableCreateRequest orderTableCreateRequest) {
        return null;
    }

    public List<OrderTableListResponse> list() {
        return null;
    }

    public ChangeEmptyResponse changeEmpty(Long orderTableId,
                                           ChangeEmptyRequest changeEmptyRequest) {
        return null;
    }

    public ChangeNumberOfGuestsResponse changeNumberOfGuests(Long orderTableId,
                                                             ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest) {
        return null;
    }

    public OrderTable findTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
