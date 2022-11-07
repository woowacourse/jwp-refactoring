package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.request.CreateMenuGroupDto;
import kitchenpos.application.dto.response.MenuGroupDto;
import kitchenpos.common.domain.menu.MenuGroup;
import kitchenpos.common.repository.menu.MenuGroupRepository;
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
