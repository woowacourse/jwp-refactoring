package application;

import domain.MenuGroup;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.MenuGroupDao;
import ui.request.MenuGroupCreateRequest;
import ui.response.MenuGroupResponse;

@Service
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        final var menuGroup = new MenuGroup(request.getName());
        return MenuGroupResponse.from(menuGroupDao.save(menuGroup));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll().stream()
                           .map(MenuGroupResponse::from)
                           .collect(Collectors.toList());
    }
}
