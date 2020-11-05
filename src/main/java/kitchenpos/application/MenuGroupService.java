package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    @Transactional
    public MenuGroup create(final String name) {
        final MenuGroup menuGroup = MenuGroup.builder()
            .name(name)
            .build();
        return menuGroupRepository.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
