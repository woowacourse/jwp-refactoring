package kitchenpos.menu.application;

import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuGroupRequestDto;
import kitchenpos.menu.application.dto.MenuGroupResponse;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupRequestDto menuGroupRequestDto) {
        return menuGroupRepository.save(new MenuGroup(menuGroupRequestDto.getName()));
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream()
                .map(it -> new MenuGroupResponse(it.getName()))
                .collect(Collectors.toList());
    }
}
