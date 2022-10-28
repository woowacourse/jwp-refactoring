package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableIdRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {

    @Nested
    class 그룹_생성_메소드는 extends ServiceTest {

        @Test
        void 입력받은_테이블로_그룹을_저장한다() {
            // given
            TableIdRequest tableIdRequest1 = new TableIdRequest(빈_테이블을_저장한다().getId());
            TableIdRequest tableIdRequest2 = new TableIdRequest(빈_테이블을_저장한다().getId());

            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(tableIdRequest1, tableIdRequest2));

            // when
            TableGroupResponse savedTableGroup = tableGroupService.create(request);

            // then
            List<OrderTable> updatedTables = orderTableDao.findAllByIdIn(
                    List.of(tableIdRequest1.getId(), tableIdRequest2.getId()));

            Assertions.assertAll(() -> {
                assertThat(savedTableGroup.getId()).isNotNull();
                assertThat(updatedTables).extracting(OrderTable::getTableGroupId, OrderTable::isEmpty)
                        .containsOnly(tuple(savedTableGroup.getId(), false));
            });
        }

        @Test
        void 그룹화하려는_테이블의_수가_2보다_작다면_예외가_발생한다() {
            // given
            TableIdRequest tableIdRequest = new TableIdRequest(빈_테이블을_저장한다().getId());

            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(tableIdRequest));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 그룹화하려는_테이블_리스트가_비어있다면_예외가_발생한다() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 그룹화하려는_테이블이_존재하지_않는다면_예외가_발생한다() {
            // given
            TableIdRequest tableIdRequest1 = new TableIdRequest(빈_테이블을_저장한다().getId());
            TableIdRequest tableIdRequest2 = new TableIdRequest(0L);

            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(tableIdRequest1, tableIdRequest2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 그룹화하려는_테이블이_비어있지_않다면_예외가_발생한다() {
            // given
            TableIdRequest tableIdRequest1 = new TableIdRequest(빈_테이블을_저장한다().getId());
            TableIdRequest tableIdRequest2 = new TableIdRequest(테이블을_저장한다(4).getId());

            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(tableIdRequest1, tableIdRequest2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 그룹화하려는_테이블이_이미_다른_테이블_그룹에_속해있다면_예외가_발생한다() {
            // given
            TableGroup savedTableGroup = tableGroupDao.save(테이블_그룹을_저장한다());
            OrderTable orderTableWithTableGroupId = orderTableDao.save(
                    new OrderTable(savedTableGroup.getId(), 0, true));

            TableIdRequest tableIdRequest1 = new TableIdRequest(orderTableWithTableGroupId.getId());
            TableIdRequest tableIdRequest2 = new TableIdRequest(빈_테이블을_저장한다().getId());

            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(tableIdRequest1, tableIdRequest2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_해제_메소드는 extends ServiceTest {

        @Test
        void 테이블_그룹을_해제한다() {
            // given
            OrderTable savedOrderTable1 = 빈_테이블을_저장한다();
            OrderTable savedOrderTable2 = 빈_테이블을_저장한다();
            TableGroup savedTableGroup = 테이블_그룹을_저장한다(savedOrderTable1, savedOrderTable2);

            // when
            tableGroupService.ungroup(savedTableGroup.getId());

            // then
            List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(savedTableGroup.getId());
            assertThat(orderTables).isEmpty();
        }

        @Test
        void 해제하려는_테이블들의_주문_상태가_계산_완료_상태가_아니라면_예외가_발생한다() {
            // given
            OrderTable savedOrderTable1 = 빈_테이블을_저장한다();
            OrderTable savedOrderTable2 = 빈_테이블을_저장한다();
            TableGroup savedTableGroup = 테이블_그룹을_저장한다(savedOrderTable1, savedOrderTable2);

            OrderTable orderTableWithGuest = orderTableDao.save(new OrderTable(savedOrderTable1.getId(), 3, false));
            Order savedOrder = 주문을_저장한다(orderTableWithGuest);
            Order orderWithCookingStatus = new Order(savedOrder.getOrderTableId(), OrderStatus.COOKING.name(),
                    savedOrder.getOrderedTime(), savedOrder.getOrderLineItems());
            orderDao.save(orderWithCookingStatus);

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
