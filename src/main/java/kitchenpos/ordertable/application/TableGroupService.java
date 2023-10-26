package kitchenpos.ordertable.application;

import kitchenpos.ordertable.application.dto.CreateOrderTableIdDto;
import kitchenpos.ordertable.application.dto.CreateTableGroupDto;
import kitchenpos.ordertable.application.dto.TableGroupDto;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderValidator;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroupRepository;
import kitchenpos.ordertable.exception.OrderTableException;
import kitchenpos.ordertable.exception.TableGroupException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderValidator orderValidator;

    public TableGroupService(
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository,
            OrderValidator orderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderValidator = orderValidator;
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
        checkTableGroupExists(tableGroupId);
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup(orderValidator);
        }
    }

    private void checkTableGroupExists(Long tableGroupId) {
        if (tableGroupRepository.existsById(tableGroupId)) {
            return;
        }
        throw new TableGroupException("해당하는 테이블 그룹이 존재하지 않습니다.");
    }
}
