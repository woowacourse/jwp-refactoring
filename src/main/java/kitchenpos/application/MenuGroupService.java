package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuGroupCommand;
import kitchenpos.application.dto.CreateMenuGroupResponse;
import kitchenpos.application.dto.SearchMenuGroupResponse;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public CreateMenuGroupResponse create(CreateMenuGroupCommand command) {
        MenuGroup menuGroup = new MenuGroup(command.name());
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        return CreateMenuGroupResponse.from(savedMenuGroup);
    }

    public List<SearchMenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return menuGroups.stream()
                .map(SearchMenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}
