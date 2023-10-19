package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupRequest.OrderTableDto;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
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
            throw new IllegalArgumentException();
        }

        // 요청으로 들어온 오더테이블이 실제 존재하는지 확인
        List<OrderTable> orderTables = request.getOrderTables().stream()
                .map(orderTable -> orderTableRepository.findById(orderTable.getId())
                        .orElseThrow(IllegalArgumentException::new))
                .collect(toList());

        // 요청으로 들어온 오더테이블이 empty거나 이미 그룹이 존재하면 예외
        boolean isTableEmptyOrAlreadyGrouped = orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty() || Objects.nonNull(
                        orderTable.getTableGroup()));
        if (isTableEmptyOrAlreadyGrouped) {
            throw new IllegalArgumentException();
        }

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        tableGroupRepository.save(tableGroup);

        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        List<OrderTable> orderTables = tableGroup.getOrderTables();

        // 테이블 그룹에 해당하는 주문 테이블을 가져온다
        List<Orders> orders = orderRepository.findAllByOrderTableIn(orderTables);

        // 그룹 내의 주문 테이블 중 cooking 또는 meal 상태인 테이블이 있으면 언그룹이 불가능하다
        boolean isUncompletedOrderExists = orders.stream()
                .anyMatch(Orders::isOrderUnCompleted);
        if (isUncompletedOrderExists) {
            throw new IllegalArgumentException();
        }
        tableGroupRepository.delete(tableGroup);
    }
}
