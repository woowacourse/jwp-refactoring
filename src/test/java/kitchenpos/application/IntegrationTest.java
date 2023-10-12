package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.추천메뉴_그룹;
import static kitchenpos.fixture.ProductFixture.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class IntegrationTest {

    @Autowired
    public MenuGroupDao menuGroupDao;

    @Autowired
    public ProductDao productDao;

    public MenuProduct 후추_치킨_2개() {
        Product 저장된_후추_치킨_10000원 = productDao.save(후추_치킨_10000원());
        return new MenuProduct(저장된_후추_치킨_10000원.getId(), 2);
    }

    public Menu 추천메뉴_후추칰힌_2개() {
        MenuGroup 저장된_추천메뉴_그룹 = menuGroupDao.save(추천메뉴_그룹());

        return new Menu(
                "더블후추칰힌",
                BigDecimal.valueOf(19000),
                저장된_추천메뉴_그룹.getId(),
                List.of(후추_치킨_2개())
        );
    }

}
