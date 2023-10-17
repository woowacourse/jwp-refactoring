package kitchenpos.domain.menu;

import kitchenpos.ui.dto.MenuRequest;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupValidator {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupValidator(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴그룹입니다. 메뉴를 등록할 수 없습니다.");
        }
    }
}
