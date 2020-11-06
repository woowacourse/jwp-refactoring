package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.MenuRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.domain.entity.Menu;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.service.MenuCreateService;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuCreateService menuCreateService;

    public MenuService(final MenuRepository menuRepository,
            final MenuCreateService menuCreateService) {
        this.menuRepository = menuRepository;
        this.menuCreateService = menuCreateService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        Menu menu = request.toEntity();
        Menu saved = menuRepository.save(menu.create(menuCreateService));
        return MenuResponse.of(saved);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.listOf(menus);
    }
}
