package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.repository.converter.MenuConverter;
import kitchenpos.persistence.dto.MenuDataDto;
import kitchenpos.persistence.specific.MenuDataAccessor;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository extends BaseRepository<Menu, MenuDataDto, MenuDataAccessor, MenuConverter> {

    public MenuRepository(final MenuDataAccessor dataAccessor, final MenuConverter converter) {
        super(dataAccessor, converter);
    }

    public long countByIdIn(final List<Long> ids) {
        return dataAccessor.countByIdIn(ids);
    }
}
