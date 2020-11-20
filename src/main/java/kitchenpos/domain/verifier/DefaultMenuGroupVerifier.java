package kitchenpos.domain.verifier;

import org.springframework.stereotype.Component;

import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.repository.MenuGroupRepository;

@Component
public class DefaultMenuGroupVerifier implements MenuGroupVerifier{
    private final MenuGroupRepository menuGroupRepository;

    public DefaultMenuGroupVerifier(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Override
    public void verifyExist(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new MenuGroupNotFoundException(menuGroupId);
        }
    }
}
