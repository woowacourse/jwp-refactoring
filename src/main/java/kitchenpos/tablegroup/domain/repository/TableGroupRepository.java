package kitchenpos.tablegroup.domain.repository;

import kitchenpos.support.BaseRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.repository.converter.TableGroupConverter;
import kitchenpos.tablegroup.persistence.TableGroupDataAccessor;
import kitchenpos.tablegroup.persistence.dto.TableGroupDataDto;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepository extends
        BaseRepository<TableGroup, TableGroupDataDto, TableGroupDataAccessor, TableGroupConverter> {

    public TableGroupRepository(final TableGroupDataAccessor dataAccessor, final TableGroupConverter converter) {
        super(dataAccessor, converter);
    }
}
