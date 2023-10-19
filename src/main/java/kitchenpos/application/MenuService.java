package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    public MenuService(final MenuRepository menuRepository,
                       final ProductService productService,
                       final MenuGroupService menuGroupService) {
        this.menuRepository = menuRepository;
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        menuGroupService.validateExistenceById(request.getMenuGroupId());

        if (calculateSumOfPrice(request).compareTo(request.getPrice()) < 0) {
            throw new IllegalArgumentException("메뉴 가격은 상품 가격들의 합보다 클 수 없습니다.");
        }

        final Menu menu = menuRepository.save(request.toEntity());
        return MenuResponse.from(menu);
    }

    private BigDecimal calculateSumOfPrice(final MenuCreateRequest request) {
        return request.getMenuProducts().stream()
                .map(menuProduct -> productService.calculatePrice(
                        menuProduct.getProductId(), menuProduct.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    public void validateExistenceByIds(final List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 주문 항목입니다.");
        }
    }
}
