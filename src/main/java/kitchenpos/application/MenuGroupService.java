package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.MenuGroupCreateDto;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupCreateDto menuGroupCreateDto) {
        final MenuGroup newMenuGroup = new MenuGroup(menuGroupCreateDto.getName());

        return menuGroupRepository.save(newMenuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
