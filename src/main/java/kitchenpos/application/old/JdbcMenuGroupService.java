package kitchenpos.application.old;

import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional(readOnly = true)
@Service
public class JdbcMenuGroupService implements MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public JdbcMenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    @Override
    public MenuGroup create(final MenuGroupCreateRequest request) {
        final var requestedName = request.getName();
        validateName(requestedName);
        return menuGroupRepository.save(new MenuGroup(requestedName));
    }

    private void validateName(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("유효하지 않은 메뉴 그룹명 : " + name);
        }

        if (menuGroupRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 메뉴 그룹명 : " + name);
        }
    }

    @Override
    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
