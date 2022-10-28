package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.dto.request.TableGroupCommand;
import kitchenpos.common.DataClearExtension;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("제품 관련 기능에서")
@ExtendWith(DataClearExtension.class)
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Nested
    @DisplayName("테이블 그룹을 생성할 때")
    class CreateTableGroup {

        @Test
        @DisplayName("정상적으로 생성한다.")
        void create() {
            OrderTable orderTable1 = orderTableRepository.save(new OrderTable(2, true));
            OrderTable orderTable2 = orderTableRepository.save(new OrderTable(2, true));
            List<Long> orderTableIds = List.of(orderTable1.getId(), orderTable2.getId());

            TableGroup tableGroup = tableGroupService.create(new TableGroupCommand(orderTableIds));

            assertThat(tableGroup).isNotNull();
        }

        @Test
        @DisplayName("주문 테이블이 2개 미만일 경우 예외가 발생한다.")
        void createInvalidOrderTableSize() {
            OrderTable orderTable1 = orderTableRepository.save(new OrderTable(2, true));
            List<Long> orderTableIds = List.of(orderTable1.getId());

            assertThatThrownBy(() -> tableGroupService.create(new TableGroupCommand(orderTableIds)))
                    .hasMessage("주문 테이블은 2개 이상이어야 합니다.");
        }

        @Test
        @DisplayName("빈 주문 테이블이 아닌경우 예외가 발생한다.")
        void createInvalidOrderTable() {
            OrderTable orderTable1 = orderTableRepository.save(new OrderTable(2, false));
            OrderTable orderTable2 = orderTableRepository.save(new OrderTable(2, true));
            List<Long> orderTableIds = List.of(orderTable1.getId(), orderTable2.getId());

            assertThatThrownBy(() -> tableGroupService.create(new TableGroupCommand(orderTableIds)))
                    .hasMessage("빈 주문 테이블이어야 합니다.");
        }
    }

    @Nested
    @DisplayName("테이블 그룹을 해제할 때")
    class UnGroup {

//        @Test
//        @DisplayName("테이블 그룹을 해제한다.")
//        void ungroup() {
//            OrderTable orderTable1 = createOrderTable(2, true);
//            OrderTable orderTable2 = createOrderTable(2, true);
//            TableGroup tableGroup = createTableGroup(List.of(orderTable1, orderTable2));
//
//            assertThatCode(() -> tableGroupController.ungroup(tableGroup.getId()));
//        }
    }


}
