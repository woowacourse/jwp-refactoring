package kitchenpos.application;

import static kitchenpos.support.TestFixtureFactory.새로운_단체_지정;
import static kitchenpos.support.TestFixtureFactory.새로운_주문;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableServiceTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableService tableService;

    @Test
    void 테이블을_등록한다() {
        OrderTable 주문_테이블 = 새로운_주문_테이블(null, 1, false);

        OrderTable 등록된_주문_테이블 = tableService.create(주문_테이블);

        assertSoftly(softly -> {
            softly.assertThat(등록된_주문_테이블.getId()).isNotNull();
            softly.assertThat(등록된_주문_테이블).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(주문_테이블);
        });
    }

    @Test
    void 테이블_목록을_조회한다() {
        OrderTable 주문_테이블 = tableService.create(새로운_주문_테이블(null, 1, false));
        OrderTable 빈_테이블 = tableService.create(새로운_주문_테이블(null, 0, true));

        List<OrderTable> 테이블_목록 = tableService.list();

        assertThat(테이블_목록).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(주문_테이블, 빈_테이블);
    }

    @Test
    void 테이블_상태를_변경한다() {
        OrderTable 테이블 = tableService.create(새로운_주문_테이블(null, 1, false));
        테이블.setEmpty(true);
        OrderTable changedOrderTable = tableService.changeEmpty(테이블.getId(), 테이블);

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    void 존재하지_않는_테이블의_상태를_변경할_수_없다() {
        OrderTable 등록되지_않은_테이블 = 새로운_주문_테이블(null, 1, false);
        assertThatThrownBy(() -> tableService.changeEmpty(Long.MIN_VALUE, 등록되지_않은_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정된_주문_테이블은_빈_테이블로_설정할_수_없다() {
        OrderTable 주문_테이블1 = tableService.create(새로운_주문_테이블(null, 1, false));
        OrderTable 주문_테이블2 = tableService.create(새로운_주문_테이블(null, 2, false));

        TableGroup 단체_지정 = tableGroupRepository.save(새로운_단체_지정(LocalDateTime.now(), List.of(주문_테이블1, 주문_테이블2)));
        주문_테이블1.setTableGroupId(단체_지정.getId());
        주문_테이블2.setTableGroupId(단체_지정.getId());
        orderTableRepository.save(주문_테이블1);
        orderTableRepository.save(주문_테이블2);

        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블1.getId(), 주문_테이블1))
                    .isInstanceOf(IllegalArgumentException.class);
            softly.assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블2.getId(), 주문_테이블2))
                    .isInstanceOf(IllegalArgumentException.class);
        });
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 주문_상태가_조리_또는_식사인_주문_테이블은_빈_테이블로_설정할_수_없다(OrderStatus orderStatus) {
        OrderTable 주문_테이블 = tableService.create(새로운_주문_테이블(null, 1, false));
        Order 주문 = 새로운_주문(주문_테이블.getId(), orderStatus.name(), LocalDateTime.now(), null);
        orderRepository.save(주문);

        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), 주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 방문한_손님_수를_입력한다() {
        OrderTable 주문_테이블 = tableService.create(새로운_주문_테이블(null, 0, false));
        주문_테이블.setNumberOfGuests(1);

        tableService.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블);

        assertThat(주문_테이블.getNumberOfGuests()).isOne();
    }

    @Test
    void 방문한_손님_수는_0명_이상이어야_한다() {
        OrderTable 주문_테이블 = tableService.create(새로운_주문_테이블(null, 1, false));
        주문_테이블.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_테이블에_방문한_손님_수를_입력할_수_없다() {
        OrderTable 등록되지_않은_테이블 = 새로운_주문_테이블(null, 1, false);
        등록되지_않은_테이블.setNumberOfGuests(0);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(등록되지_않은_테이블.getId(), 등록되지_않은_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_테이블에_방문한_손님_수를_입력할_수_없다() {
        OrderTable 빈_테이블 = tableService.create(새로운_주문_테이블(null, 0, true));
        빈_테이블.setNumberOfGuests(2);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(빈_테이블.getId(), 빈_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
