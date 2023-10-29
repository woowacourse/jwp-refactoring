package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.service.MenuCreateService;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuCreateService menuCreateService;
    private final MenuRepository menuRepository;

    public MenuService(final MenuCreateService menuCreateService, final MenuRepository menuRepository) {
        this.menuCreateService = menuCreateService;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        final Menu menu = menuCreateService.create(request);
        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
