package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public Long create(final MenuCreateRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        MenuGroup menuGroup = menuGroupRepository.getById(request.getMenuGroupId());
        Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup);
        menu.validatePriceIsNullOrMinus();
        return menuRepository.save(menu).getId();
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

}
