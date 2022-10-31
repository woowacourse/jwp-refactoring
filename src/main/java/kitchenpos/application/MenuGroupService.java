package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.request.CreateMenuGroupDto;
import kitchenpos.application.dto.response.MenuGroupDto;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
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
    public MenuGroupDto create(final CreateMenuGroupDto menuGroupDto) {
        return MenuGroupDto.of(menuGroupRepository.save(new MenuGroup(menuGroupDto.getName())));
    }

    public List<MenuGroupDto> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(MenuGroupDto::of)
                .collect(Collectors.toList());
    }
}
