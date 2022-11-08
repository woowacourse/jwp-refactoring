package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupDto create(final String menuGroupName) {
        final MenuGroup saveMenuGroup = menuGroupRepository.save(new MenuGroup(menuGroupName));
        return MenuGroupDto.of(saveMenuGroup);
    }

    public List<MenuGroupDto> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(MenuGroupDto::of)
                .collect(Collectors.toList());
    }
}
