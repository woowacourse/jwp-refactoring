package kitchenpos.menu;

import kitchenpos.menugroup.MenuGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupValidator {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupValidator(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }
}
