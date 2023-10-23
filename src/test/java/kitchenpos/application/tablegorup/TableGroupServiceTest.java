package kitchenpos.application.tablegorup;

import static kitchenpos.fixture.OrderTableFixture.테이블;
import static kitchenpos.fixture.TableGroupFixture.단체_지정;
import static kitchenpos.fixture.TableGroupFixture.단체_지정_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.tablegroup.TableGroupService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.domain.tablegroup.TableGroupUngroupedEvent;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@SuppressWarnings("NonAsciiCharacters")
@RecordApplicationEvents
@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService sut;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private ApplicationEvents applicationEvents;

    @Nested
    class 단체_지정을_할_때 {

        @Test
        void 단체_지정하는_테이블이_2개_미만인_경우_예외를_던진다() {
            // given
            OrderTable orderTable = orderTableRepository.save(테이블(true));
            TableGroupRequest request = 단체_지정_요청(orderTable);

            // expect
            assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정하려는 테이블은 2개 이상이어야 합니다.");
        }

        @Test
        void 단체_지정하려는_테이블이_비어있지_않은_경우_예외를_던진다() {
            // given
            OrderTable orderTable1 = orderTableRepository.save(테이블(true));
            OrderTable orderTable2 = orderTableRepository.save(테이블(false));
            TableGroupRequest request = 단체_지정_요청(orderTable1, orderTable2);

            // expect
            assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않거나, 이미 단체 지정이 된 테이블은 단체 지정을 할 수 없습니다.");
        }

        @Test
        void 이미_단체_지정이_되어있는_테이블을_단체_지정하려는_경우_예외를_던진다() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(단체_지정());
            OrderTable orderTable1 = orderTableRepository.save(테이블(true, 0, tableGroup));
            OrderTable orderTable2 = orderTableRepository.save(테이블(true, 0, tableGroup));
            OrderTable orderTable3 = orderTableRepository.save(테이블(true));
            TableGroupRequest request = 단체_지정_요청(orderTable2, orderTable3);

            // expect
            assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않거나, 이미 단체 지정이 된 테이블은 단체 지정을 할 수 없습니다.");
        }

        @Test
        void 단체_지정이_성공하는_경우_테이블의_상태가_주문_테이블로_변경된다() {
            // given
            OrderTable orderTable1 = orderTableRepository.save(테이블(true));
            OrderTable orderTable2 = orderTableRepository.save(테이블(true));
            TableGroupRequest request = 단체_지정_요청(orderTable1, orderTable2);

            // when
            TableGroupResponse result = sut.create(request);

            // then
            List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(result.getId());
            assertThat(orderTables)
                    .extracting(OrderTable::isEmpty)
                    .containsExactly(false, false);
        }
    }

    @Nested
    class 단체_지정을_해제_할_때 {

        @Test
        void 단체_지정_해제를_성공하는_경우_테이블의_단체_지정_번호가_제거된다() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(단체_지정());
            orderTableRepository.save(테이블(true, 0, tableGroup));
            orderTableRepository.save(테이블(true, 0, tableGroup));

            // when
            sut.ungroup(tableGroup.getId());

            // then
            assertThat(orderTableRepository.findAllByTableGroupId(tableGroup.getId())).isEmpty();
        }

        @Test
        void 단체_지정_해제_완료_이벤트를_발행한다() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(단체_지정());

            // when
            sut.ungroup(tableGroup.getId());

            // then
            assertThat(applicationEvents.stream(TableGroupUngroupedEvent.class)).hasSize(1);
        }
    }
}
