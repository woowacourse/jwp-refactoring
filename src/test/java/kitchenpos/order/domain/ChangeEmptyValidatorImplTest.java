package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.ChangeEmptyValidator;
import kitchenpos.order.domain.ChangeEmptyValidatorImpl;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ChangeEmptyValidatorImplTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private ChangeEmptyValidator changeEmptyValidator;

    @BeforeEach
    void setUp() {
        changeEmptyValidator = new ChangeEmptyValidatorImpl(orderRepository);
    }

    @Test
    void 포함된_그룹이_있는_경우_예외가_발생한다() {
        //given
        OrderTable 테이블 = orderTableRepository.save(new OrderTable(0, true));
        테이블.changeTableGroup(1L);

        //expect
        assertThatThrownBy(() -> 테이블.changeEmpty(changeEmptyValidator))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 완료되지_않은_주문이_있는_경우_예외가_발생한다() {
        //given
        OrderTable 테이블 = orderTableRepository.save(new OrderTable(2, false));
        orderRepository.save(new Order(null, 테이블.getId(), OrderStatus.COOKING, now(), List.of()));

        //expect
        assertThatThrownBy(() -> 테이블.changeEmpty(changeEmptyValidator))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
