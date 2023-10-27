package kitchenpos.menu.domain.repository;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.converter.MenuConverter;
import kitchenpos.menu.persistence.MenuDataAccessor;
import kitchenpos.menu.persistence.dto.MenuDataDto;
import kitchenpos.support.BaseRepository;
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
