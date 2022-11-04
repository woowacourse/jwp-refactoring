package kitchenpos.menu.service;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.exception.MenuGroupNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validateInMenuGroup(final Menu menu) {
        Long menuGroupId = menu.getMenuGroupId();
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new MenuGroupNotFoundException("등록되지 않은 메뉴그룹 입니다.");
        }
    }
}
