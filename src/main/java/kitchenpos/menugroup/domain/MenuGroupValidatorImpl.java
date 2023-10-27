package kitchenpos.menugroup.domain;

import kitchenpos.menu.domain.MenuGroupValidator;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;

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
