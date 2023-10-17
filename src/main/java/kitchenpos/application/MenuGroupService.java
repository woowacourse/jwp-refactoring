package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public Long create(final String name) {
        return menuGroupRepository.save(new MenuGroup(name)).getId();
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
