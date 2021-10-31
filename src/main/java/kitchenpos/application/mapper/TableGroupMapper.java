package kitchenpos.application.mapper;

import java.util.stream.Collectors;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.application.dto.request.TableGroupRequest.OrderTableId;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class TableGroupMapper {
    private final OrderTableRepository tableRepository;

    public TableGroupMapper(OrderTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public TableGroup mapFrom(TableGroupRequest tableGroupRequest) {
        return new TableGroup(
                tableGroupRequest.getOrderTables()
                        .stream().map(this::toOrderTable)
                        .collect(Collectors.toList())
        );
    }

    private OrderTable toOrderTable(OrderTableId orderTableId) {
        return tableRepository.findById(orderTableId.getId())
                .orElseThrow(IllegalArgumentException::new);
    }
}
