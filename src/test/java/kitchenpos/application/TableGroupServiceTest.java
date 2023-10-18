package kitchenpos.application;

import static kitchenpos.support.TestFixtureFactory.새로운_단체_지정;
import static kitchenpos.support.TestFixtureFactory.새로운_주문;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void 테이블들을_단체로_지정한다() {
        OrderTable 주문_테이블1 = orderTableRepository.save(새로운_주문_테이블(null, 1, true));
        OrderTable 주문_테이블2 = orderTableRepository.save(새로운_주문_테이블(null, 2, true));
        TableGroup 단체_지정 = 새로운_단체_지정(LocalDateTime.now(), List.of(주문_테이블1, 주문_테이블2));

        TableGroup 등록된_단체_지정 = tableGroupService.create(단체_지정);

        주문_테이블1.setTableGroupId(등록된_단체_지정.getId());
        주문_테이블1.setEmpty(false);
        주문_테이블2.setTableGroupId(등록된_단체_지정.getId());
        주문_테이블2.setEmpty(false);

        assertSoftly(softly -> {
            softly.assertThat(등록된_단체_지정.getId()).isNotNull();
            softly.assertThat(등록된_단체_지정).usingRecursiveComparison()
                    .ignoringFields("id", "orderTables")
                    .isEqualTo(단체_지정);
            softly.assertThat(등록된_단체_지정.getOrderTables())
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(주문_테이블1, 주문_테이블2);
        });
    }

    @Test
    void 단체_지정하려는_테이블은_2개_이상이어야_한다() {
        OrderTable 주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, true));
        TableGroup 단체_지정 = 새로운_단체_지정(LocalDateTime.now(), List.of(주문_테이블));

        assertThatThrownBy(() -> tableGroupService.create(단체_지정))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_테이블을_단체로_지정할_수_없다() {
        OrderTable 등록된_주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, true));
        OrderTable 등록되지_않은_주문_테이블 = 새로운_주문_테이블(null, 2, true);
        TableGroup 단체_지정 = 새로운_단체_지정(LocalDateTime.now(), List.of(등록된_주문_테이블, 등록되지_않은_주문_테이블));

        assertThatThrownBy(() -> tableGroupService.create(단체_지정))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 비어있지_않은_테이블을_단체로_지정할_수_없다() {
        OrderTable 빈_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, true));
        OrderTable 비어있지_않은_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 2, false));
        TableGroup 단체_지정 = 새로운_단체_지정(LocalDateTime.now(), List.of(빈_테이블, 비어있지_않은_테이블));

        assertThatThrownBy(() -> tableGroupService.create(단체_지정))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_지정된_테이블을_단체_지정할_수_없다() {
        OrderTable 주문_테이블1 = orderTableRepository.save(새로운_주문_테이블(null, 1, true));
        OrderTable 주문_테이블2 = orderTableRepository.save(새로운_주문_테이블(null, 2, true));
        OrderTable 주문_테이블3 = orderTableRepository.save(새로운_주문_테이블(null, 2, true));
        TableGroup 단체_지정 = 새로운_단체_지정(LocalDateTime.now(), List.of(주문_테이블1, 주문_테이블2));
        tableGroupService.create(단체_지정);

        TableGroup 또다른_단체_지정 = 새로운_단체_지정(LocalDateTime.now(), List.of(주문_테이블2, 주문_테이블3));
        assertThatThrownBy(() -> tableGroupService.create(또다른_단체_지정))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정을_삭제한다() {
        OrderTable 주문_테이블1 = orderTableRepository.save(새로운_주문_테이블(null, 1, true));
        OrderTable 주문_테이블2 = orderTableRepository.save(새로운_주문_테이블(null, 2, true));
        TableGroup 단체_지정 = tableGroupService.create(새로운_단체_지정(LocalDateTime.now(), List.of(주문_테이블1, 주문_테이블2)));

        tableGroupService.ungroup(단체_지정.getId());

        assertSoftly(softly -> {
            softly.assertThat(주문_테이블1.getTableGroupId()).isNull();
            softly.assertThat(주문_테이블2.getTableGroupId()).isNull();
        });
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 단체_지정된_주문_테이블의_주문_상태가_조리_또는_식사인_경우_단체_지정을_삭제할_수_없다(OrderStatus orderStatus) {
        OrderTable 주문_테이블1 = orderTableRepository.save(새로운_주문_테이블(null, 1, true));
        OrderTable 주문_테이블2 = orderTableRepository.save(새로운_주문_테이블(null, 2, true));
        TableGroup 단체_지정 = tableGroupService.create(새로운_단체_지정(LocalDateTime.now(), List.of(주문_테이블1, 주문_테이블2)));

        orderRepository.save(새로운_주문(주문_테이블1.getId(), orderStatus.name(), LocalDateTime.now(), null));

        assertThatThrownBy(() -> tableGroupService.ungroup(단체_지정.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
