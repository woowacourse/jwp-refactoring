package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.menu.Menu;

public class MenuFakeDao extends BaseFakeDao<Menu> implements MenuDao {

    @Override
    public long countByIdIn(final List<Long> ids) {
        var count = 0L;
        for (Long id : entities.keySet()) {
            if (ids.contains(id)) {
                count += 1;
            }
        }
        return count;
    }
}
