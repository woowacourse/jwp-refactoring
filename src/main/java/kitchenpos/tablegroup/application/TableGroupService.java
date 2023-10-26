package kitchenpos.tablegroup.application;

import java.util.List;
import kitchenpos.order.application.dto.OrderTableGroupEventDto;
import kitchenpos.order.application.dto.OrderTableUnGroupEventDto;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest.OrderTableRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;
    private final TableMapper mapper;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final ApplicationEventPublisher publisher, final TableMapper mapper
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
        this.mapper = mapper;
    }

    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTableRequest> requests = request.getOrderTables();

        validateOrderTablesSize(requests);

        final List<Long> orderTableIds = mapper.toDomain(requests);
        TableGroup group = group(orderTableIds);

        return TableGroupResponse.of(group, orderTableIds);
    }

    private void validateOrderTablesSize(final List<OrderTableRequest> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블이 비어 있거나 2개 미만일 수 없습니다.");
        }
    }


    private TableGroup group(final List<Long> orderTableIds) {
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());

        OrderTableGroupEventDto orderTableGroupEventDto = new OrderTableGroupEventDto();
        orderTableGroupEventDto.setOrderTableIds(orderTableIds);
        orderTableGroupEventDto.setTableGroupId(savedTableGroup.getId());

        publisher.publishEvent(orderTableGroupEventDto);

        return savedTableGroup;
    }

    public void ungroup(final Long tableGroupId) {
        List<Long> orderTableIds = mapper.toDomain(tableGroupId);

        OrderTableUnGroupEventDto orderTableUnGroupEventDto = new OrderTableUnGroupEventDto();
        orderTableUnGroupEventDto.setOrderTableIds(orderTableIds);
        orderTableUnGroupEventDto.setTableGroupId(tableGroupId);

        publisher.publishEvent(orderTableUnGroupEventDto);
    }
}
