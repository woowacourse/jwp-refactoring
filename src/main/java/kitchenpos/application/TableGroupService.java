package kitchenpos.application;

import kitchenpos.application.dto.CreateOrderTableIdDto;
import kitchenpos.application.dto.CreateTableGroupDto;
import kitchenpos.application.dto.TableGroupDto;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.exception.OrderTableException;
import kitchenpos.exception.TableGroupException;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupDto create(CreateTableGroupDto createTableGroupDto) {
        List<Long> orderTableIds = createTableGroupDto.getOrderTables().stream()
                .map(CreateOrderTableIdDto::getId)
                .collect(Collectors.toList());
        List<OrderTable> orderTables = findOrderTables(orderTableIds);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        tableGroup.addOrderTables(orderTables);
        tableGroupRepository.save(tableGroup);

        return TableGroupDto.from(tableGroup);
    }

    private List<OrderTable> findOrderTables(List<Long> orderTablesId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTablesId);
        if (orderTables.size() != orderTablesId.size()) {
            throw new OrderTableException("주문 테이블을 찾을 수 없어 테이블 그룹을 생성할 수 없습니다.");
        }
        return orderTables;
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        findTableGroup(tableGroupId);
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupIdWithOrders(tableGroupId);
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private TableGroup findTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new TableGroupException("해당하는 테이블 그룹이 존재하지 않습니다."));
    }
}
