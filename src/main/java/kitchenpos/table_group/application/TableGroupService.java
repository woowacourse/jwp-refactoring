package kitchenpos.table_group.application;

import kitchenpos.table_group.application.dto.TableGroupDto;
import kitchenpos.table_group.application.validator.FrontTableGroupValidator;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.table_group.domain.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final ApplicationEventPublisher eventPublisher;
    private final FrontTableGroupValidator frontTableGroupValidator;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableDtoReader orderTableDtoReader;

    public TableGroupService(
        final ApplicationEventPublisher eventPublisher,
        final FrontTableGroupValidator frontTableGroupValidator,
        final TableGroupRepository tableGroupRepository,
        final OrderTableDtoReader orderTableDtoReader
    ) {
        this.eventPublisher = eventPublisher;
        this.frontTableGroupValidator = frontTableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableDtoReader = orderTableDtoReader;
    }

    @Transactional
    public TableGroupDto create(final TableGroupDto tableGroupDto) {
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroupDto.toTableGroup());
        frontTableGroupValidator.validateGroup(tableGroupDto.getOrderTableIds());

        final TableGroupCreateEvent tableGroupCreateEvent
            = new TableGroupCreateEvent(savedTableGroup.getId(), tableGroupDto.getOrderTableIds());
        eventPublisher.publishEvent(tableGroupCreateEvent);

        return TableGroupDto.createResponse(
            savedTableGroup, orderTableDtoReader.readTablesByTableGroupId(savedTableGroup.getId())
        );
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        frontTableGroupValidator.validateUngroup(tableGroup.getId());

        eventPublisher.publishEvent(new TableGroupUngroupEvent(tableGroup.getId()));

        tableGroupRepository.delete(tableGroup);
    }
}
