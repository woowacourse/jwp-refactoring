package kitchenpos.dao;

import java.lang.reflect.Field;
import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.util.ReflectionUtils;

public class InMemoryMenuDao extends InMemoryAbstractDao<Menu> implements MenuDao {

    @Override
    protected void setId(final Menu entity, final Long entityId) {
        final Field field = ReflectionUtils.findField(Menu.class, "id");
        field.setAccessible(true);
        ReflectionUtils.setField(field, entity, entityId);
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return ids.stream()
                .distinct()
                .filter(database::containsKey)
                .count();
    }
}
