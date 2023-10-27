package kitchenpos.tablegroup.application;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.exception.KitchenPosException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.event.TableGroupGroupEvent;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.event.TableGroupUngroupEvent;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(
        TableGroupRepository tableGroupRepository,
        ApplicationEventPublisher eventPublisher
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.eventPublisher = eventPublisher;
    }

    public TableGroupResponse create(TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTableIds();
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new KitchenPosException("주문 테이블 목록은 2개 이상이어야 합니다.");
        }
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(null, LocalDateTime.now()));
        eventPublisher.publishEvent(new TableGroupGroupEvent(tableGroup.getId(), orderTableIds));
        return TableGroupResponse.of(tableGroup, orderTableIds);
    }

    public void ungroup(Long tableGroupId) {
        eventPublisher.publishEvent(new TableGroupUngroupEvent(tableGroupId));
        tableGroupRepository.deleteById(tableGroupId);
    }
}
