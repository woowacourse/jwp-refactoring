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
import static kitchenpos.fixture.TableGroupFixture.테이블그룹;
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
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

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
    private TableGroupDao tableGroupDao;

    @Autowired
    private TableGroupService tableGroupService;

    @Nested
    class 테이블_그룹_생성 {

        @Test
        void 테이블_그룹을_생성할_수_있다() {
            // given
            OrderTable 테이블1 = orderTableDao.save(빈테이블());
            OrderTable 테이블2 = orderTableDao.save(빈테이블());

            TableGroup tableGroup = 테이블그룹(List.of(테이블1, 테이블2));

            // when
            TableGroup created = tableGroupService.create(tableGroup);

            // then
            assertThat(tableGroupDao.findById(created.getId())).isPresent();
        }

        @Test
        void 묶으려는_테이블이_2개미만이면_생성할_수_없다() {
            // given
            OrderTable 테이블1 = orderTableDao.save(빈테이블());

            TableGroup tableGroup = 테이블그룹(List.of(테이블1));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이면_생성할_수_없다() {
            // given
            OrderTable notSavedTable = 주문테이블();
            TableGroup tableGroup = 테이블그룹(List.of(notSavedTable));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈테이블이_아니면_생성할_수_없다() {
            // given
            OrderTable 테이블1 = orderTableDao.save(비지않은_테이블());
            OrderTable 테이블2 = orderTableDao.save(빈테이블());

            TableGroup tableGroup = 테이블그룹(List.of(테이블1, 테이블2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_테이블_그룹을_가진_테이블이면_생성할_수_없다() {
            // given
            OrderTable 테이블1 = orderTableDao.save(빈테이블());
            OrderTable 테이블2 = orderTableDao.save(빈테이블());
            OrderTable 테이블3 = orderTableDao.save(빈테이블());

            TableGroup 테이블그룹1 = 테이블그룹(List.of(테이블1, 테이블2));
            tableGroupService.create(테이블그룹1);

            TableGroup 테이블그룹2 = 테이블그룹(List.of(테이블1, 테이블3));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(테이블그룹2))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_삭제 {

        @Test
        void 테이블_그룹을_삭제할_수_있다() {
            // given
            OrderTable 테이블1 = orderTableDao.save(빈테이블());
            OrderTable 테이블2 = orderTableDao.save(빈테이블());

            TableGroup 테이블그룹 = tableGroupService.create(테이블그룹(List.of(테이블1, 테이블2)));

            // when
            tableGroupService.ungroup(테이블그룹.getId());

            // then
            assertThat(orderTableDao.findAllByTableGroupId(테이블그룹.getId())).isEmpty();
        }

        @ParameterizedTest
        @EnumSource(mode = Mode.EXCLUDE, names = "COMPLETION")
        void 주문상태가_COMPLETION이_아니면_삭제할_수_없다(OrderStatus orderStatus) {
            // given
            MenuGroup 두마리메뉴 = menuGroupDao.save(메뉴그룹_두마리메뉴);

            Product 후라이드 = productDao.save(후라이드_16000);

            Menu 후라이드메뉴 = menuDao.save(메뉴("싼후라이드", 10000, 두마리메뉴.getId()));
            MenuProduct 싼후라이드상품 = menuProductDao.save(메뉴상품(후라이드메뉴.getId(), 후라이드.getId(), 1));
            후라이드메뉴.setMenuProducts(List.of(싼후라이드상품));

            OrderTable 테이블1 = orderTableDao.save(빈테이블());
            OrderTable 테이블2 = orderTableDao.save(빈테이블());

            TableGroup 테이블그룹 = tableGroupService.create(테이블그룹(List.of(테이블1, 테이블2)));

            Order order1 = orderDao.save(주문(테이블1.getId(), orderStatus.name()));
            OrderLineItem 주문상품 = orderLineItemDao.save(주문상품(order1.getId(), 후라이드메뉴.getId(), 1));
            order1.setOrderLineItems(List.of(주문상품));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
