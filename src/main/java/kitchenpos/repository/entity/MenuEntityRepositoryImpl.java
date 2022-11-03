package kitchenpos.repository.entity;

import java.util.Collection;
import kitchenpos.repository.MenuRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;

public class MenuEntityRepositoryImpl implements MenuEntityRepository {

    private final MenuRepository menuRepository;

    @Lazy
    public MenuEntityRepositoryImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public boolean existsAllByIdIn(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException("id 목록이 비어있습니다");
        }
        long counts = menuRepository.countByIdIn(ids);
        return ids.size() == counts;
    }
}
