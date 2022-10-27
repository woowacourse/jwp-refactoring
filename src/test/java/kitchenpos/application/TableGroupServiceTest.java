package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixtures.한마리메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_생성;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목_생성;
import static kitchenpos.fixture.OrderTableFixtures.빈_테이블1;
import static kitchenpos.fixture.OrderTableFixtures.빈_테이블2;
import static kitchenpos.fixture.OrderTableFixtures.주문_테이블9;
import static kitchenpos.fixture.OrderTableFixtures.테이블_생성;
import static kitchenpos.fixture.ProductFixtures.후라이드_상품;
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
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

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
            final OrderTable emptyTable1 = orderTableDao.save(테이블_생성(빈_테이블1.getNumberOfGuests(), 빈_테이블1.isEmpty()));
            final OrderTable emptyTable2 = orderTableDao.save(테이블_생성(빈_테이블2.getNumberOfGuests(), 빈_테이블2.isEmpty()));

            final TableGroup tableGroup = new TableGroup();
            tableGroup.addCreatedDate(LocalDateTime.now());
            tableGroup.addOrderTables(List.of(emptyTable1, emptyTable2));

            final TableGroup actual = sut.create(tableGroup);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTables()).hasSize(2)
            );
        }

        @DisplayName("단체로 지정할 테이블이 비어있으면 단체로 지정할 수 없다.")
        @Test
        void groupTableWithOrderTable() {
            final TableGroup tableGroup = 단체_지정_생성(List.of());

            assertThatThrownBy(() -> sut.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("두 개 미만의 테이블은 단체로 지정할 수 없다.")
        @Test
        void groupTableLessThanTwo() {
            final OrderTable emptyTable = orderTableDao.save(테이블_생성(빈_테이블1.getNumberOfGuests(), 빈_테이블1.isEmpty()));
            final TableGroup tableGroup = 단체_지정_생성(List.of(emptyTable));

            assertThatThrownBy(() -> sut.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 테이블이 존재하면 단체로 지정할 수 없다.")
        @Test
        void groupTableWithNotExistTable() {
            final Long 존재하지_않는_테이블_ID = -1L;
            final OrderTable emptyTable = orderTableDao.save(테이블_생성(빈_테이블1.getNumberOfGuests(), 빈_테이블1.isEmpty()));
            final OrderTable notExistentTable = 테이블_생성(존재하지_않는_테이블_ID, 0, true);
            final TableGroup tableGroup = 단체_지정_생성(List.of(emptyTable, notExistentTable));

            assertThatThrownBy(() -> sut.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("빈 테이블이 아니면 단체로 지정할 수 없다.")
        @Test
        void groupTableWithNotEmptyTable() {
            final OrderTable emptyTable = orderTableDao.save(테이블_생성(빈_테이블1.getNumberOfGuests(), 빈_테이블1.isEmpty()));
            final OrderTable orderTable = orderTableDao.save(테이블_생성(주문_테이블9.getNumberOfGuests(), 주문_테이블9.isEmpty()));
            final TableGroup tableGroup = 단체_지정_생성(List.of(emptyTable, orderTable));

            assertThatThrownBy(() -> sut.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이미 단체로 지정된 테이블인 경우 단체로 지정할 수 없다.")
        @Test
        void groupAlreadyGroupingTable() {
            final OrderTable emptyTable1 = orderTableDao.save(테이블_생성(빈_테이블1.getNumberOfGuests(), 빈_테이블1.isEmpty()));
            final OrderTable emptyTable2 = orderTableDao.save(테이블_생성(빈_테이블2.getNumberOfGuests(), 빈_테이블2.isEmpty()));
            sut.create(단체_지정_생성(List.of(emptyTable1, emptyTable2)));

            final TableGroup tableGroup = 단체_지정_생성(List.of(emptyTable1, emptyTable2));

            assertThatThrownBy(() -> sut.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("단체 지정 해제")
    class clearGroupTableTest {

        @DisplayName("정상적인 경우 단체 지정을 해제할 수 있다.")
        @Test
        void clearGroupTable() {
            final OrderTable emptyTable1 = orderTableDao.save(테이블_생성(빈_테이블1.getNumberOfGuests(), 빈_테이블1.isEmpty()));
            final OrderTable emptyTable2 = orderTableDao.save(테이블_생성(빈_테이블2.getNumberOfGuests(), 빈_테이블2.isEmpty()));
            final TableGroup tableGroup = sut.create(단체_지정_생성(List.of(emptyTable1, emptyTable2)));

            sut.ungroup(tableGroup.getId());

            assertAll(
                    () -> assertThat(orderTableDao.findById(emptyTable1.getId()).get().getTableGroupId()).isNull(),
                    () -> assertThat(orderTableDao.findById(emptyTable2.getId()).get().getTableGroupId()).isNull()
            );
        }

        @DisplayName("계산 완료되지 않은 테이블이 존재하는 경우 단체 지정을 해제할 수 없다.")
        @Test
        void clearGroupTableWithNotCompletionTable() {
            final OrderTable emptyTable1 = orderTableDao.save(테이블_생성(빈_테이블1.getNumberOfGuests(), 빈_테이블1.isEmpty()));
            final OrderTable emptyTable2 = orderTableDao.save(테이블_생성(빈_테이블2.getNumberOfGuests(), 빈_테이블2.isEmpty()));
            final TableGroup tableGroup = sut.create(단체_지정_생성(List.of(emptyTable1, emptyTable2)));

            final MenuProduct menuProduct = 메뉴_상품_생성(후라이드_상품.getId(), 5L);
            final Menu menu = menuDao.save(메뉴_생성("한마리메뉴", 500, 한마리메뉴_그룹.getId(), List.of(menuProduct)));
            final OrderLineItem orderLineItem = 주문_항목_생성(menu.getId(), 1);
            orderDao.save(주문_생성(emptyTable1.getId(), OrderStatus.COOKING.name(), List.of(orderLineItem)));

            assertThatThrownBy(() -> sut.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
