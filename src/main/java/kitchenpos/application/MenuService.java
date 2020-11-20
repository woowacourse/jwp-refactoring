package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuCreateService;
import kitchenpos.domain.MenuRepository;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuResponse;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuCreateService menuCreateService;

    public MenuService(MenuRepository menuRepository, MenuCreateService menuCreateService) {
        this.menuRepository = menuRepository;
        this.menuCreateService = menuCreateService;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest menuCreateRequest) {
        Menu menu = menuCreateService.createMenu(menuCreateRequest.getName(), menuCreateRequest.getPrice(),
            menuCreateRequest.getMenuGroupId(),
            menuCreateRequest.getMenuProductCreateInfos());

        return MenuResponse.of(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAll());
    }
}
