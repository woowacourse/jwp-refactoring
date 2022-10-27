package kitchenpos.application.concrete;

import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.exception.badrequest.MenuGroupNameDuplicateException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class JpaMenuGroupService implements MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public JpaMenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    @Override
    public MenuGroup create(final MenuGroupCreateRequest request) {
        final var name = request.getName();
        validateDuplicateMenuGroupName(name);
        final var newMenuGroup = new MenuGroup(name);

        return menuGroupRepository.save(newMenuGroup);
    }

    private void validateDuplicateMenuGroupName(final String name) {
        if (menuGroupRepository.existsByName(name)) {
            throw new MenuGroupNameDuplicateException(name);
        }
    }

    @Override
    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
