package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.request.MenuGroupCommand;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    @Transactional
    public MenuGroupResponse create(final MenuGroupCommand menuGroupCommand) {
        MenuGroup menuGroup = menuGroupRepository.save(menuGroupCommand.toEntity());
        return MenuGroupResponse.from(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
