package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_두마리메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderLineItemFixture.주문상품;
import static kitchenpos.fixture.OrderTableFixture.비지않은_테이블;
import static kitchenpos.fixture.OrderTableFixture.빈테이블;
import static kitchenpos.fixture.OrderTableFixture.주문테이블;
import static kitchenpos.fixture.ProductFixture.후라이드_16000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private TableService tableService;

    @Nested
    class 테이블_등록 {

        @Test
        void 테이블을_등록할_수_있다() {
            // given
            OrderTable 주문테이블 = 주문테이블();

            // when
            OrderTable created = tableService.create(주문테이블);

            // then
            assertThat(orderTableDao.findById(created.getId())).isPresent();
        }
    }

    @Nested
    class 테이블_empty_변경 {

        @Test
        void 테이블의_empty여부를_변경할_수_있다() {
            // given
            OrderTable saved = orderTableDao.save(주문테이블());

            OrderTable changeRequest = 빈테이블();

            // when
            OrderTable updated = tableService.changeEmpty(saved.getId(), changeRequest);

            // then
            assertThat(updated.isEmpty()).isTrue();
        }

        @Test
        void 테이블이_존재하지_않으면_변경할_수_없다() {
            // given
            Long wrongTableId = 999L;
            OrderTable changeRequest = 빈테이블();

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(wrongTableId, changeRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @EnumSource(mode = Mode.INCLUDE, names = {"COOKING", "MEAL"})
        void 주문상태가_COOKING이거나_MEAL이면_변경할_수_없다(OrderStatus orderStatus) {
            // given
            MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

            Product 후라이드 = productDao.save(후라이드_16000);

            Menu 후라이드메뉴 = menuDao.save(메뉴("싼후라이드", 10000, 두마리메뉴.getId()));
            MenuProduct 싼후라이드상품 = menuProductDao.save(메뉴상품(후라이드메뉴.getId(), 후라이드.getId(), 1));
            후라이드메뉴.setMenuProducts(List.of(싼후라이드상품));

            OrderTable 테이블 = orderTableDao.save(비지않은_테이블());
            OrderLineItem 주문상품 = 주문상품(후라이드메뉴.getId(), 3);

            Order order = orderDao.save(주문(테이블.getId(), orderStatus.name()));
            orderLineItemDao.save(주문상품(order.getId(), 후라이드메뉴.getId(), 1));

            OrderTable changeRequest = 빈테이블();

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(테이블.getId(), changeRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_numberOfGuests_변경 {

        @Test
        void 테이블의_손님_수를_변경할_수_있다() {
            // given
            OrderTable saved = orderTableDao.save(주문테이블());

            OrderTable changeRequest = 주문테이블(3);

            // when
            OrderTable updated = tableService.changeNumberOfGuests(saved.getId(), changeRequest);

            // then
            assertThat(updated.getNumberOfGuests()).isEqualTo(3);
        }

        @Test
        void 바꾸려는_손님_수가_0미만이면_변경할_수_없다() {
            // given
            OrderTable saved = orderTableDao.save(주문테이블());

            OrderTable changeRequest = 주문테이블(-3);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getId(), changeRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_존재하지_않으면_변경할_수_없다() {
            // given
            Long wrongTableId = 999L;
            OrderTable changeRequest = 주문테이블(3);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(wrongTableId, changeRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블이면_변경할_수_없다() {
            // given
            OrderTable saved = orderTableDao.save(빈테이블());

            OrderTable changeRequest = 주문테이블(3);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getId(), changeRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_목록 {

        @Test
        void 테이블의_목록을_조회할_수_있다() {
            // given
            OrderTable 테이블1 = orderTableDao.save(빈테이블());
            OrderTable 테이블2 = orderTableDao.save(빈테이블());
            OrderTable 테이블3 = orderTableDao.save(비지않은_테이블());

            // when
            List<OrderTable> findList = tableService.list();

            // then
            assertThat(findList).usingRecursiveComparison()
                    .isEqualTo(List.of(테이블1, 테이블2, 테이블3));
        }
    }
}
