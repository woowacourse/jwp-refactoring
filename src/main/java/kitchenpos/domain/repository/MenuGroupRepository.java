package kitchenpos.domain.repository;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.converter.MenuGroupConverter;
import kitchenpos.persistence.dto.MenuGroupDataDto;
import kitchenpos.persistence.specific.MenuGroupDataAccessor;
import org.springframework.stereotype.Repository;

@Repository
public class MenuGroupRepository extends
        BaseRepository<MenuGroup, MenuGroupDataDto, MenuGroupDataAccessor, MenuGroupConverter> {

    public MenuGroupRepository(final MenuGroupDataAccessor dataAccessor, final MenuGroupConverter converter) {
        super(dataAccessor, converter);
    }

    public boolean existsById(final Long id) {
        return dataAccessor.existsById(id);
    }
}
