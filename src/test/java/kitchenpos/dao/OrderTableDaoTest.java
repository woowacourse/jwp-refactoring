package kitchenpos.dao;

import static kitchenpos.support.TestFixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
class OrderTableDaoTest {

    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void 주문_테이블을_등록하면_ID를_부여받는다() {
        TableGroup 단체_지정 = tableGroupDao.save(새로운_단체_지정(LocalDateTime.now(), null));
        OrderTable 테이블 = 새로운_주문_테이블(단체_지정.getId(), 0, true);

        OrderTable 등록된_테이블 = orderTableDao.save(테이블);

        assertSoftly(softly -> {
            softly.assertThat(등록된_테이블.getId()).isNotNull();
            softly.assertThat(등록된_테이블).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(테이블);
        });
    }

    @Test
    void 등록하는_주문_테이블에_ID가_존재하면_해당_ID의_주문_테이블로_등록한다() {
        TableGroup 단체_지정 = tableGroupDao.save(새로운_단체_지정(LocalDateTime.now(), null));
        OrderTable 원래_등록되어_있던_주문_테이블 = orderTableDao.save(새로운_주문_테이블(단체_지정.getId(), 0, true));
        OrderTable 새_주문_테이블 = 새로운_주문_테이블(단체_지정.getId(), 1, false);
        새_주문_테이블.setId(원래_등록되어_있던_주문_테이블.getId());

        OrderTable savedOrderTable = orderTableDao.save(새_주문_테이블);

        assertThat(savedOrderTable).usingRecursiveComparison()
                .isEqualTo(새_주문_테이블);
    }

    @Test
    void 테이블은_단체_지정_되어있지_않을_수_있다() {
        OrderTable orderTable = 새로운_주문_테이블(null, 0, true);

        assertDoesNotThrow(() -> orderTableDao.save(orderTable));
    }

    @Test
    void ID로_주문_테이블을_조회한다() {
        TableGroup 단체_지정 = tableGroupDao.save(새로운_단체_지정(LocalDateTime.now(), null));
        OrderTable 테이블 = orderTableDao.save(새로운_주문_테이블(단체_지정.getId(), 0, true));

        OrderTable ID로_조회한_테이블 = orderTableDao.findById(테이블.getId())
                .orElseGet(Assertions::fail);

        assertThat(ID로_조회한_테이블).usingRecursiveComparison()
                .isEqualTo(테이블);
    }

    @Test
    void 존재하지_않는_ID로_주문_테이블을_조회하면_Optional_empty를_반환한다() {
        Optional<OrderTable> 존재하지_않는_ID로_조회한_주문_테이블 = orderTableDao.findById(Long.MIN_VALUE);

        assertThat(존재하지_않는_ID로_조회한_주문_테이블).isEmpty();
    }

    @Test
    void 모든_주문_테이블을_조회한다() {
        TableGroup 단체_지정 = tableGroupDao.save(새로운_단체_지정(LocalDateTime.now(), null));
        OrderTable 테이블1 = orderTableDao.save(새로운_주문_테이블(단체_지정.getId(), 0, true));
        OrderTable 테이블2 = orderTableDao.save(새로운_주문_테이블(단체_지정.getId(), 0, true));

        List<OrderTable> 모든_테이블 = orderTableDao.findAll();

        assertThat(모든_테이블).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(테이블1, 테이블2);
    }

    @Test
    void ID_목록에_있는_주문_테이블을_조회할_수_있다() {
        TableGroup 단체_지정 = tableGroupDao.save(새로운_단체_지정(LocalDateTime.now(), null));
        OrderTable 테이블1 = orderTableDao.save(새로운_주문_테이블(단체_지정.getId(), 0, true));
        OrderTable 테이블2 = orderTableDao.save(새로운_주문_테이블(단체_지정.getId(), 0, true));
        OrderTable 테이블3 = orderTableDao.save(새로운_주문_테이블(단체_지정.getId(), 0, true));
        List<Long> ID_목록 = List.of(테이블1.getId(), 테이블2.getId());

        List<OrderTable> actual = orderTableDao.findAllByIdIn(ID_목록);

        assertThat(actual).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(테이블1, 테이블2);
    }

    @Test
    void 단체_지정_id로_주문_테이블을_조회할_수_있다() {
        TableGroup 단체_지정1 = tableGroupDao.save(새로운_단체_지정(LocalDateTime.now(), null));
        TableGroup 단체_지정2 = tableGroupDao.save(새로운_단체_지정(LocalDateTime.now(), null));
        OrderTable 단체_지정1의_테이블1 = orderTableDao.save(새로운_주문_테이블(단체_지정1.getId(), 0, true));
        OrderTable 단체_지정1의_테이블2 = orderTableDao.save(새로운_주문_테이블(단체_지정1.getId(), 0, true));
        OrderTable 단체_지정2의_테이블 = orderTableDao.save(새로운_주문_테이블(단체_지정2.getId(), 0, true));

        List<OrderTable> 단체_지정1의_모든_주문_테이블 = orderTableDao.findAllByTableGroupId(단체_지정1.getId());

        assertThat(단체_지정1의_모든_주문_테이블).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(단체_지정1의_테이블1, 단체_지정1의_테이블2);
    }
}
