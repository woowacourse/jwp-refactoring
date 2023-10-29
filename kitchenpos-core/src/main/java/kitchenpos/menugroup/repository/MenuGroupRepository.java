package kitchenpos.menugroup.repository;

import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.persistence.MenuGroupDataAccessor;
import kitchenpos.menugroup.persistence.dto.MenuGroupDataDto;
import kitchenpos.menugroup.repository.converter.MenuGroupConverter;
import kitchenpos.support.BaseRepository;
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
