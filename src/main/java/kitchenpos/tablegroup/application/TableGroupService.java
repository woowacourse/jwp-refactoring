package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.dao.TableGroupDao;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupCreatedEvent;
import kitchenpos.tablegroup.dto.TableGroupDto;
import kitchenpos.tablegroup.dto.TableGroupUngroupEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TableGroupService {

    private final ApplicationEventPublisher eventPublisher;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(ApplicationEventPublisher eventPublisher, TableGroupDao tableGroupDao) {
        this.eventPublisher = eventPublisher;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupDto create(final TableGroupDto tableGroupDto) {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), tableGroupDto.getOrderTableIds());
        eventPublisher.publishEvent(TableGroupCreatedEvent.from(tableGroup));
        return TableGroupDto.from(tableGroupDao.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        tableGroup.ungroup();
        eventPublisher.publishEvent(TableGroupUngroupEvent.from(tableGroup));
        tableGroupDao.save(tableGroup);
    }
}
