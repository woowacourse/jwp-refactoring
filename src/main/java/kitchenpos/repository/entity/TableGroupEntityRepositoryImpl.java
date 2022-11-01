package kitchenpos.repository.entity;

import kitchenpos.domain.TableGroup;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.context.annotation.Lazy;

public class TableGroupEntityRepositoryImpl implements TableGroupEntityRepository {

    private final TableGroupRepository tableGroupRepository;

    @Lazy
    public TableGroupEntityRepositoryImpl(TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }


    @Override
    public TableGroup getById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 테이블 그룹이 없습니다 : " + id));
    }
}
