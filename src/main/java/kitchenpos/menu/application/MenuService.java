package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MenuService {

    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(MenuGroupRepository menuGroupRepository, MenuRepository menuRepository, ProductRepository productRepository, MenuProductRepository menuProductRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProductRequest menuProductRequest : request.getMenuProductRequests()) {
            Product product = productRepository.findById(menuProductRequest.getProductId()).orElseThrow(()->new IllegalArgumentException());
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }
        if (request.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴 상품 가격의 합보다 큰 가격으로 메뉴를 생성할 수 없습니다.");
        }

        List<MenuProduct> menuProducts = saveMenuProducts(request.getMenuProductRequests());
        Menu menu = menuRepository.save(request.toEntity(menuProducts));
        return MenuResponse.of(menu);
    }

    private List<MenuProduct> saveMenuProducts(List<MenuProductRequest> requests) {
        return requests.stream().map(this::saveMenuProduct).collect(Collectors.toList());
    }

    private MenuProduct saveMenuProduct(MenuProductRequest request) {
        if(!productRepository.existsById(request.getProductId())){
            throw new IllegalArgumentException("존재하지 않는 제품으로 메뉴를 생성할 수 없습니다.");
        }
        return menuProductRepository.save(request.toEntity());
    }

    public List<MenuResponse> list() {
        return MenuResponse.listOf(menuRepository.findAll());
    }
}
