package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.Fixtures;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    MenuService menuService;

    @Autowired
    ProductDao productDao;

    @Autowired
    MenuProductDao menuProductDao;

    @BeforeEach
    void setup() {
        productDao.save(Fixtures.상품_후라이드());
        menuProductDao.save(Fixtures.메뉴상품_후라이드());
    }

    @DisplayName("특정 메뉴를 추가할 시 메뉴 목록에 추가된다.")
    @Test
    void createAndList() {
        Menu 메뉴_후라이드치킨 = Fixtures.메뉴_후라이드치킨();

        Menu saved = menuService.create(메뉴_후라이드치킨);

        assertThat(menuService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(saved);
    }
}
