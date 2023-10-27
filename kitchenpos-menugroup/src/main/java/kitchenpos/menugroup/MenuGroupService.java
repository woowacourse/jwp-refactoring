package kitchenpos.menugroup;

import kitchenpos.menugroup.dto.MenuGroupRequest;
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
    public MenuGroup create(final MenuGroupRequest menuGroup) {
        return menuGroupRepository.save(new MenuGroup(
                menuGroup.getName()
        ));
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
