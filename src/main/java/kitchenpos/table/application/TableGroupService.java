package kitchenpos.table.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupRequest.OrderTableDto;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.RequestOrderTableCountNotEnoughException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.exception.TableGroupNotFoundException;
import kitchenpos.table.exception.UnCompletedOrderExistsException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository,
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        // 오더테이블 리스트가 비었거나 사이즈가 2 미만이면 예외
        List<OrderTableDto> orderTableDtos = request.getOrderTables();
        if (CollectionUtils.isEmpty(orderTableDtos) || orderTableDtos.size() < 2) {
            throw new RequestOrderTableCountNotEnoughException();
        }

        // 요청으로 들어온 오더테이블이 실제 존재하는지 확인
        List<OrderTable> orderTables = request.getOrderTables().stream()
                .map(orderTable -> orderTableRepository.findById(orderTable.getId())
                        .orElseThrow(OrderTableNotFoundException::new))
                .collect(toList());

        TableGroup tableGroup = tableGroupRepository.save(
                TableGroup.of(LocalDateTime.now(), orderTables));

        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);
        List<OrderTable> orderTables = tableGroup.getOrderTables();

        // 테이블 그룹에 해당하는 주문 테이블을 가져온다
        List<Orders> orders = orderRepository.findAllByOrderTableIn(orderTables);

        // 그룹 내의 주문 테이블 중 cooking 또는 meal 상태인 테이블이 있으면 언그룹이 불가능하다
        boolean isUncompletedOrderExists = orders.stream()
                .anyMatch(Orders::isOrderUnCompleted);
        if (isUncompletedOrderExists) {
            throw new UnCompletedOrderExistsException();
        }
        tableGroupRepository.delete(tableGroup);
    }
}
