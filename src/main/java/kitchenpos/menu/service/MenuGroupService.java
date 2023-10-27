package kitchenpos.menu.service;

import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.infra.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final String name) {
        return menuGroupRepository.save(new MenuGroup(name));
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
