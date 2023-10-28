package kitchenpos.application;

import kitchenpos.domain.TableGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    private final TableGroupRepository tableGroupRepository;

    public TableGroupValidator(TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    public void validateGroup(Long tableGroupId) {
        tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("테이블 그룹이 존재하지 않습니다."));
    }
}
