package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.CreateMenuGroupDto;
import kitchenpos.menu.application.dto.MenuGroupDto;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupDto create(CreateMenuGroupDto createMenuGroupDto) {
        MenuGroup menuGroup = new MenuGroup(createMenuGroupDto.getName());
        MenuGroup savedManuGroup = menuGroupRepository.save(menuGroup);

        return new MenuGroupDto(
                savedManuGroup.getId(),
                savedManuGroup.getName());
    }

    @Transactional(readOnly = true)
    public List<MenuGroupDto> list() {
        return menuGroupRepository.findAll().stream()
                .map(menuGroup -> new MenuGroupDto(
                        menuGroup.getId(),
                        menuGroup.getName()
                ))
                .collect(Collectors.toList());
    }
}
