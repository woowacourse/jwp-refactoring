package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UngroupValidatorImplTest {

    private UngroupValidatorImpl ungroupValidatorImpl;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;


    @BeforeEach
    void setUp() {
        ungroupValidatorImpl = new UngroupValidatorImpl(orderRepository);
    }

    @Test
    void 주문이_완료되지_않은_테이블이_있으면_예외가_발생한다() {
        //given
        OrderTable 완료되지_않은_테이블 = orderTableRepository.save(new OrderTable(0, true));
        OrderTable 완료된_테이블 = orderTableRepository.save(new OrderTable(2, true));

        TableGroup 테이블_그룹 = tableGroupRepository.save(new TableGroup());
        테이블_그룹.changeOrderTables(List.of(완료되지_않은_테이블, 완료된_테이블));
        tableGroupRepository.save(테이블_그룹);
        orderRepository.save(new Order(null, 완료되지_않은_테이블.getId(), OrderStatus.MEAL, null, List.of()));

        //expect
        assertThatThrownBy(() -> ungroupValidatorImpl.validate(테이블_그룹))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 조리 또는 식사인 경우 테이블 그룹을 해제할 수 없습니다.");
    }

}
