package kitchenpos.utils;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderTablesFactory {

    private final OrderTableRepository orderTableRepository;

    public OrderTablesFactory(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public List<OrderTable> generate(TableGroupCreateRequest tableGroupCreateRequest) {
        List<OrderTable> orderTables = new ArrayList<>();
        List<Long> orderTableIds = tableGroupCreateRequest.getOrderTableIds();

        for (Long orderTableId : orderTableIds) {
            OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("OrderTable을 찾을 수 없습니다."));

            orderTables.add(orderTable);
        }

        return orderTables;
    }
}
