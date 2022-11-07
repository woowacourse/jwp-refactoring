package kitchenpos.application.menu;

import java.util.stream.Collectors;
import kitchenpos.application.menu.dto.request.CreateMenuGroupDto;
import kitchenpos.application.menu.dto.response.MenuGroupDto;
import kitchenpos.core.domain.menu.MenuGroup;
import kitchenpos.core.repository.menu.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuGroupDto> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(MenuGroupDto::of)
                .collect(Collectors.toList());
    }

    public MenuGroupDto create(final CreateMenuGroupDto menuGroupDto) {
        return MenuGroupDto.of(menuGroupRepository.save(new MenuGroup(menuGroupDto.getName())));
    }
}
