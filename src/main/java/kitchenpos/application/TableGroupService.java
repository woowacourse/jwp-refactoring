package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.table.OrderTableIdRequest;
import kitchenpos.application.dto.request.table.TableGroupRequest;
import kitchenpos.application.dto.response.table.TableGroupResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.NotFoundTableGroupException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {

        List<OrderTable> orderTables = orderTableRepository.findAllById(
                tableGroupRequest.getOrderTableIdRequests().stream()
                        .map(OrderTableIdRequest::getId)
                        .collect(Collectors.toList())
        );

        checkOrderTableSize(orderTables.size(), tableGroupRequest.getOrderTableIdRequests().size());

        TableGroup tableGroup = new TableGroup.TableGroupBuilder()
                .setCreatedDate(LocalDateTime.now())
                .setOrderTables(orderTables)
                .build();

        tableGroup.updateOrderTables();

        return TableGroupResponse.create(tableGroupRepository.save(tableGroup));
    }

    private void checkOrderTableSize(int orderTableSize, int requestSize) {
        if (orderTableSize != requestSize) {
            throw new NotFoundOrderTableException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {

        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(NotFoundTableGroupException::new);

        tableGroup.unGroup();
    }
}
