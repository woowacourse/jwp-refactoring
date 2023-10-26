package kitchenpos.domain.repository;

import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.converter.TableGroupConverter;
import kitchenpos.persistence.dto.TableGroupDataDto;
import kitchenpos.persistence.specific.TableGroupDataAccessor;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepository extends
        BaseRepository<TableGroup, TableGroupDataDto, TableGroupDataAccessor, TableGroupConverter> {

    public TableGroupRepository(final TableGroupDataAccessor dataAccessor, final TableGroupConverter converter) {
        super(dataAccessor, converter);
    }
}
