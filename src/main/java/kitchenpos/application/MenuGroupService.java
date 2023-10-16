package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuGroupCreateDto;
import kitchenpos.application.dto.MenuGroupDto;
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
    public MenuGroupDto create(final MenuGroupCreateDto menuGroupCreateDto) {
        final MenuGroup newMenuGroup = new MenuGroup(menuGroupCreateDto.getName());

        final MenuGroup savedMenuGroup = menuGroupRepository.save(newMenuGroup);

        return MenuGroupDto.from(savedMenuGroup);
    }

    public List<MenuGroupDto> list() {
        final List<MenuGroup> findMenuGroups = menuGroupRepository.findAll();

        return findMenuGroups.stream()
            .map(MenuGroupDto::from)
            .collect(Collectors.toList());
    }
}
