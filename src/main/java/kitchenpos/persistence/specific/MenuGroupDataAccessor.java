package kitchenpos.persistence.specific;

import kitchenpos.persistence.BasicDataAccessor;
import kitchenpos.persistence.dto.MenuGroupDataDto;

public interface MenuGroupDataAccessor extends BasicDataAccessor<MenuGroupDataDto> {

    boolean existsById(Long id);
}
