package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.converter.OrderTableConverter;
import kitchenpos.persistence.dto.OrderTableDataDto;
import kitchenpos.persistence.specific.OrderTableDataAccessor;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableRepository extends
        BaseRepository<OrderTable, OrderTableDataDto, OrderTableDataAccessor, OrderTableConverter> {

    public OrderTableRepository(final OrderTableDataAccessor dataAccessor, final OrderTableConverter converter) {
        super(dataAccessor, converter);
    }

    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        return converter.dataToEntity(dataAccessor.findAllByIdIn(ids));
    }

    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return converter.dataToEntity(dataAccessor.findAllByTableGroupId(tableGroupId));
    }
}
