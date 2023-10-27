package kitchenpos.ordertable.domain.repository;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.converter.OrderTableConverter;
import kitchenpos.ordertable.persistence.OrderTableDataAccessor;
import kitchenpos.ordertable.persistence.dto.OrderTableDataDto;
import kitchenpos.support.BaseRepository;
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
