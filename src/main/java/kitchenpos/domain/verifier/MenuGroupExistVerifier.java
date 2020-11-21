package kitchenpos.domain.verifier;

import org.springframework.stereotype.Component;

import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.repository.MenuGroupRepository;

@Component
public class MenuGroupExistVerifier implements MenuGroupVerifier{
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupExistVerifier(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Override
    public void verify(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new MenuGroupNotFoundException(menuGroupId);
        }
    }
}
