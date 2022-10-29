package kitchenpos.application;

import kitchenpos.application.request.menugroup.MenuGroupRequest;
import kitchenpos.application.response.ResponseAssembler;
import kitchenpos.application.response.menugroup.MenuGroupResponse;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;
    private final ResponseAssembler responseAssembler;

    public MenuGroupService(final MenuGroupDao menuGroupDao, final ResponseAssembler responseAssembler) {
        this.menuGroupDao = menuGroupDao;
        this.responseAssembler = responseAssembler;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        final var menuGroup = new MenuGroup(request.getName());

        final var savedMenuGroup = menuGroupDao.save(menuGroup);
        return responseAssembler.menuGroupResponse(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        final var menuGroups = menuGroupDao.findAll();
        return responseAssembler.menuGroupResponses(menuGroups);
    }
}
