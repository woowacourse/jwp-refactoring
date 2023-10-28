package kitchenpos.menu;

import java.util.List;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.MenuExistenceValidator;
import org.springframework.stereotype.Component;

@Component
public class MenuExistenceValidatorImpl implements MenuExistenceValidator {

    private final MenuRepository menuRepository;

    public MenuExistenceValidatorImpl(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void validate(final List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }
}
