package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuGroupCommand;
import kitchenpos.application.dto.domain.MenuGroupDto;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupDto create(final CreateMenuGroupCommand command) {
        MenuGroup menuGroup = menuGroupRepository.save(command.toDomain());
        return MenuGroupDto.from(menuGroup);
    }

    public List<MenuGroupDto> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return menuGroups.stream()
                .map(MenuGroupDto::from)
                .collect(Collectors.toList());
    }
}
