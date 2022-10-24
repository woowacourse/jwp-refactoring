package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_생성;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목_생성;
import static kitchenpos.fixture.OrderTableFixture.빈_테이블_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static kitchenpos.fixture.ProductFixture.상품_생성;
import static kitchenpos.fixture.TableGroupFixture.단체_지정_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.support.IntegrationTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService sut;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Nested
    @DisplayName("단체 지정")
    class TableGroupTest {

        @DisplayName("정상적인 경우 단체로 지정할 수 있다.")
        @Test
        void groupTable() {
            final OrderTable 빈_테이블_1번 = orderTableDao.save(빈_테이블_생성(5));
            final OrderTable 빈_테이블_2번 = orderTableDao.save(빈_테이블_생성(5));

            final TableGroup tableGroup = new TableGroup();
            tableGroup.setCreatedDate(LocalDateTime.now());
            tableGroup.setOrderTables(List.of(빈_테이블_1번, 빈_테이블_2번));

            final TableGroup actual = sut.create(tableGroup);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTables()).hasSize(2)
            );
        }

        @DisplayName("단체로 지정할 테이블이 비어있으면 단체로 지정할 수 없다.")
        @Test
        void groupTableWithOrderTable() {
            final TableGroup 단체_지정 = 단체_지정_생성(List.of());

            assertThatThrownBy(() -> sut.create(단체_지정))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("두 개 미만의 테이블은 단체로 지정할 수 없다.")
        @Test
        void groupTableLessThanTwo() {
            final OrderTable 빈_테이블 = orderTableDao.save(빈_테이블_생성(5));
            final TableGroup 단체_지정 = 단체_지정_생성(List.of(빈_테이블));

            assertThatThrownBy(() -> sut.create(단체_지정))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 테이블이 존재하면 단체로 지정할 수 없다.")
        @Test
        void groupTableWithNotExistTable() {
            final OrderTable 빈_테이블 = 빈_테이블_생성(5);
            final TableGroup 단체_지정 = 단체_지정_생성(List.of(빈_테이블));

            assertThatThrownBy(() -> sut.create(단체_지정))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("빈 테이블이 아니면 단체로 지정할 수 없다.")
        @Test
        void groupTableWithNotEmptyTable() {
            final OrderTable 주문_테이블_1번 = orderTableDao.save(주문_테이블_생성(5));
            final OrderTable 주문_테이블_2번 = orderTableDao.save(주문_테이블_생성(5));
            final TableGroup 단체_지정 = 단체_지정_생성(List.of(주문_테이블_1번, 주문_테이블_2번));

            assertThatThrownBy(() -> sut.create(단체_지정))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이미 단체로 지정된 테이블인 경우 단체로 지정할 수 없다.")
        @Test
        void groupAlreadyGroupingTable() {
            final OrderTable 빈_테이블_1번 = orderTableDao.save(빈_테이블_생성(5));
            final OrderTable 빈_테이블_2번 = orderTableDao.save(빈_테이블_생성(5));
            sut.create(단체_지정_생성(List.of(빈_테이블_1번, 빈_테이블_2번)));

            final OrderTable 단체_지정된_테이블 = orderTableDao.findById(빈_테이블_1번.getId()).get();
            final OrderTable 빈_테이블_3번 = orderTableDao.save(빈_테이블_생성(5));
            final TableGroup 단체_지정 = 단체_지정_생성(List.of(단체_지정된_테이블, 빈_테이블_3번));

            assertThatThrownBy(() -> sut.create(단체_지정))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("단체 지정 해제")
    class clearGroupTableTest {

        @DisplayName("정상적인 경우 단체 지정을 해제할 수 있다.")
        @Test
        void clearGroupTable() {
            final OrderTable 빈_테이블_1번 = orderTableDao.save(빈_테이블_생성(5));
            final OrderTable 빈_테이블_2번 = orderTableDao.save(빈_테이블_생성(5));
            final TableGroup 단체_지정 = sut.create(단체_지정_생성(List.of(빈_테이블_1번, 빈_테이블_2번)));

            sut.ungroup(단체_지정.getId());

            assertAll(
                    () -> assertThat(orderTableDao.findById(빈_테이블_1번.getId()).get().getTableGroupId()).isNull(),
                    () -> assertThat(orderTableDao.findById(빈_테이블_2번.getId()).get().getTableGroupId()).isNull()
            );
        }

        @DisplayName("계산 완료되지 않은 테이블이 존재하는 경우 단체 지정을 해제할 수 없다.")
        @Test
        void clearGroupTableWithNotCompletionTable() {
            final OrderTable 빈_테이블_1번 = orderTableDao.save(빈_테이블_생성(5));
            final OrderTable 빈_테이블_2번 = orderTableDao.save(빈_테이블_생성(5));
            final TableGroup 단체_지정 = sut.create(단체_지정_생성(List.of(빈_테이블_1번, 빈_테이블_2번)));

            final Product 짱구 = productDao.save(상품_생성("짱구", 100));
            final MenuProduct 짱구_메뉴_상품 = 메뉴_상품_생성(짱구.getId(), 5L);
            final MenuGroup 떡잎_유치원 = menuGroupDao.save(메뉴_그룹_생성("떡잎 유치원"));
            final Menu 해바라기반 = menuDao.save(메뉴_생성("해바라기반", 500, 떡잎_유치원.getId(), List.of(짱구_메뉴_상품)));
            final OrderLineItem 주문_항목 = 주문_항목_생성(해바라기반.getId(), 1);
            final Order 주문 = orderDao.save(주문_생성(빈_테이블_1번.getId(), OrderStatus.COOKING.name(), List.of(주문_항목)));

            assertThatThrownBy(() -> sut.ungroup(단체_지정.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
