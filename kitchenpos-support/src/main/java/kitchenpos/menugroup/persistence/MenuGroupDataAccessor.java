package kitchenpos.menugroup.persistence;

import kitchenpos.menugroup.persistence.dto.MenuGroupDataDto;
import kitchenpos.support.BasicDataAccessor;

public interface MenuGroupDataAccessor extends BasicDataAccessor<MenuGroupDataDto> {

    boolean existsById(Long id);
}
