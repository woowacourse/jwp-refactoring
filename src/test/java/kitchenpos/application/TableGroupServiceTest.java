package kitchenpos.application;

import static kitchenpos.test.fixture.OrderFixture.주문;
import static kitchenpos.test.fixture.TableFixture.테이블;
import static kitchenpos.test.fixture.TableGroupFixture.테이블_그룹;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Nested
    class 테이블_그룹_추가_시 {

        @Test
        void 정상적인_테이블_그룹이라면_테이블_그룹을_추가한다() {
            OrderTable orderTableA = orderTableDao.save(테이블(null, 0, true));
            OrderTable orderTableB = orderTableDao.save(테이블(null, 0, true));
            TableGroup tableGroup = 테이블_그룹(LocalDateTime.now(), List.of(orderTableA, orderTableB));

            LocalDateTime now = LocalDateTime.now();
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            assertSoftly(softly -> {
                softly.assertThat(savedTableGroup.getId()).isNotNull();
                softly.assertThat(savedTableGroup.getCreatedDate()).isAfter(now);
                softly.assertThat(savedTableGroup.getOrderTables()).usingRecursiveComparison()
                        .ignoringFields("tableGroupId", "empty")
                        .isEqualTo(List.of(orderTableA, orderTableB));
                softly.assertThat(savedTableGroup.getOrderTables()).extracting(OrderTable::getTableGroupId)
                        .containsOnly(savedTableGroup.getId());
                softly.assertThat(savedTableGroup.getOrderTables()).extracting(OrderTable::isEmpty).containsOnly(false);
            });
        }

        @Test
        void 테이블_목록이_비어있으면_예외를_던진다() {
            TableGroup tableGroup = 테이블_그룹(LocalDateTime.now(), Collections.emptyList());

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블_개수가_2보다_작으면_예외를_던진다() {
            OrderTable orderTable = orderTableDao.save(테이블(null, 0, true));
            TableGroup tableGroup = 테이블_그룹(LocalDateTime.now(), List.of(orderTable));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이_있으면_예외를_던진다() {
            OrderTable orderTableA = orderTableDao.save(테이블(null, 0, true));
            OrderTable orderTableB = 테이블(null, 0, true);
            TableGroup tableGroup = 테이블_그룹(LocalDateTime.now(), List.of(orderTableA, orderTableB));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_비어있지_않으면_예외를_던진다() {
            OrderTable orderTableA = orderTableDao.save(테이블(null, 0, true));
            OrderTable orderTableB = orderTableDao.save(테이블(null, 0, false));
            TableGroup tableGroup = 테이블_그룹(LocalDateTime.now(), List.of(orderTableA, orderTableB));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_그룹화된_테이블이_있으면_예외를_던진다() {
            OrderTable orderTableA = orderTableDao.save(테이블(null, 0, true));
            OrderTable orderTableB = orderTableDao.save(테이블(null, 0, true));
            OrderTable orderTableC = orderTableDao.save(테이블(null, 0, true));
            tableGroupService.create(테이블_그룹(LocalDateTime.now(), List.of(orderTableA, orderTableB)));

            assertThatThrownBy(() ->
                    tableGroupService.create(테이블_그룹(LocalDateTime.now(), List.of(orderTableA, orderTableC)))
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_해제_시 {

        @Test
        void 정상적인_테이블_그룹이라면_테이블_그룹을_해제한다() {
            OrderTable orderTableA = orderTableDao.save(테이블(null, 0, true));
            OrderTable orderTableB = orderTableDao.save(테이블(null, 0, true));
            TableGroup tableGroup =
                    tableGroupService.create(테이블_그룹(LocalDateTime.now(), List.of(orderTableA, orderTableB)));

            tableGroupService.ungroup(tableGroup.getId());

            assertSoftly(softly -> {
                softly.assertThat(orderTableDao.findById(orderTableA.getId()).get().getTableGroupId()).isNull();
                softly.assertThat(orderTableDao.findById(orderTableB.getId()).get().getTableGroupId()).isNull();
            });
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 테이블의_주문이_조리중이거나_식사중이라면_예외를_던진다(OrderStatus orderStatus) {
            OrderTable orderTableA = orderTableDao.save(테이블(null, 0, true));
            OrderTable orderTableB = orderTableDao.save(테이블(null, 0, true));
            TableGroup tableGroup =
                    tableGroupService.create(테이블_그룹(LocalDateTime.now(), List.of(orderTableA, orderTableB)));
            orderDao.save(주문(orderTableA.getId(), orderStatus.name(), LocalDateTime.now(), Collections.emptyList()));

            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블의_주문이_종료되었으면_예외를_던지지_않는다() {
            OrderTable orderTableA = orderTableDao.save(테이블(null, 0, true));
            OrderTable orderTableB = orderTableDao.save(테이블(null, 0, true));
            TableGroup tableGroup =
                    tableGroupService.create(테이블_그룹(LocalDateTime.now(), List.of(orderTableA, orderTableB)));
            orderDao.save(
                    주문(
                            orderTableA.getId(),
                            OrderStatus.COMPLETION.name(),
                            LocalDateTime.now(),
                            Collections.emptyList()
                    )
            );

            assertThatNoException().isThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()));
        }
    }
}
