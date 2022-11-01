package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_MENU_GROUP_EXCEPTION;

import java.util.List;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import kitchenpos.dao.JpaMenuGroupRepository;
import kitchenpos.dao.JpaMenuRepository;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.request.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {
    private final JpaMenuRepository menuRepository;
    private final JpaMenuGroupRepository menuGroupRepository;

    public MenuService(final JpaMenuRepository menuRepository, final JpaMenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public Menu create(final MenuRequest request) {
        validMenuGroup(request.getMenuGroupId());
        return menuRepository.save(request.toMenu());
    }

    private void validMenuGroup(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new CustomIllegalArgumentException(NOT_FOUND_MENU_GROUP_EXCEPTION);
        }
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
