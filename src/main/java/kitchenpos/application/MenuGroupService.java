package kitchenpos.application;

import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
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
    public MenuGroup create(MenuGroup menuGroup) {
        return menuGroupRepository.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
