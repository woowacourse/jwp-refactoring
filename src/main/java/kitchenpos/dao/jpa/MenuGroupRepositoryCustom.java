package kitchenpos.dao.jpa;

import kitchenpos.domain.MenuGroup;

public interface MenuGroupRepositoryCustom {

    boolean existsBy(MenuGroup menuGroup);
}
