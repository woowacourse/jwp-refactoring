package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.persistence.MenuGroupRepository;
import kitchenpos.menu.request.MenuGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(MenuGroupCreateRequest request) {
        return menuGroupRepository.save(new MenuGroup(request.getName()));
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
