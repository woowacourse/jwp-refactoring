package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {


    @Autowired
    private MenuDao menuDao;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private MenuProductDao menuProductDao;
    @Autowired
    private ProductDao productDao;


    @Nested
    class 메뉴를_등록할_때 {

        @Test
        void success() {

        }

        @Test
        void 가격_정보가_없거나_0보다_작은_경우_실패() {

        }

        @Test
        void 등록되지_않은_메뉴그룹에_속할_시_실패() {

        }

        @Test
        void 메뉴의_가격이_메뉴_내부의_상품의_총_합계_가격보다_크면_실패() {

        }

    }

    @Test
    void 메뉴_목록_조회() {

    }
}
