package kitchenpos.domain;

import java.util.List;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Component;

@Component
public class SnapShotCreatorImpl implements SnapShotCreator{

    private final MenuRepository menuRepository;

    public SnapShotCreatorImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> findAllByIds(final List<Long> menuIds) {
        return menuRepository.findAllById(menuIds);
    }
}
