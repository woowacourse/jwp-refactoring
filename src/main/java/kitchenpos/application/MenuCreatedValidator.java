package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.dto.request.MenuCommand;
import kitchenpos.application.dto.request.MenuProductCommand;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class MenuCreatedValidator {

    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuCreatedValidator(MenuGroupDao menuGroupDao, ProductDao productDao) {
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    public void validate(MenuCommand menuCommand) {
        BigDecimal price = menuCommand.getPrice();
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격이 0원 이상이어야 합니다");
        }

        if (!menuGroupDao.existsById(menuCommand.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }

        List<MenuProductCommand> menuProducts = menuCommand.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProductCommand menuProduct : menuProducts) {
            final ProductDto product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격이 상품의 가격보다 높습니다.");
        }
    }
}
