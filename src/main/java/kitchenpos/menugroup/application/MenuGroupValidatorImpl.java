package kitchenpos.menugroup.application;

import kitchenpos.menu.application.MenuGroupValidator;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupValidatorImpl implements MenuGroupValidator {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupValidatorImpl(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Override
    public void validateMenuGroupExist(final Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }
}
