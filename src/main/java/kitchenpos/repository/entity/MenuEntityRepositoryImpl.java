package kitchenpos.repository.entity;

import java.util.Collection;
import kitchenpos.repository.MenuRepository;
import org.springframework.context.annotation.Lazy;

public class MenuEntityRepositoryImpl implements MenuEntityRepository {

    private final MenuRepository menuRepository;

    @Lazy
    public MenuEntityRepositoryImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public boolean existsAllByInIn(Collection<Long> ids) {
        long counts = menuRepository.countByIdIn(ids);
        return ids.size() == counts;
    }
}
