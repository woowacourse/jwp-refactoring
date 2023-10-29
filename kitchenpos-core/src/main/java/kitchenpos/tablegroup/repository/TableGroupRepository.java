package kitchenpos.tablegroup.repository;

import kitchenpos.support.BaseRepository;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.persistence.TableGroupDataAccessor;
import kitchenpos.tablegroup.persistence.dto.TableGroupDataDto;
import kitchenpos.tablegroup.repository.converter.TableGroupConverter;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepository extends
        BaseRepository<TableGroup, TableGroupDataDto, TableGroupDataAccessor, TableGroupConverter> {

    public TableGroupRepository(final TableGroupDataAccessor dataAccessor, final TableGroupConverter converter) {
        super(dataAccessor, converter);
    }
}
