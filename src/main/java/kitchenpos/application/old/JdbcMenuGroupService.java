package kitchenpos.application.old;

import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional(readOnly = true)
@Service
public class JdbcMenuGroupService implements MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public JdbcMenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    @Override
    public MenuGroup create(final MenuGroupCreateRequest request) {
        final var requestedName = request.getName();
        validateName(requestedName);
        return menuGroupDao.save(new MenuGroup(requestedName));
    }

    private void validateName(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("유효하지 않은 메뉴 그룹명 : " + name);
        }
    }

    @Override
    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
