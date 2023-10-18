package kitchenpos.application;

import kitchenpos.application.fixture.TableServiceFixture;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends TableServiceFixture {

    @InjectMocks
    TableService tableService;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Test
    void 테이블을_등록한다() {
        // given
        given(orderTableDao.save(any(OrderTable.class))).willReturn(저장된_주문_테이블);

        final OrderTable orderTable = new OrderTable(방문한_손님_수, false);

        // when
        final OrderTable actual = tableService.create(orderTable);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual).usingRecursiveComparison()
                          .ignoringFields("id")
                          .isEqualTo(저장된_주문_테이블);
        });
    }

    @Test
    void 주문_테이블_목록을_조회한다() {
        // given
        given(orderTableDao.findAll()).willReturn(저장된_주문_테이블들);

        // when
        final List<OrderTable> actual = tableService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0)).usingRecursiveComparison()
                          .isEqualTo(저장된_주문_테이블1);
            softAssertions.assertThat(actual.get(1)).usingRecursiveComparison()
                          .isEqualTo(저장된_주문_테이블2);
            softAssertions.assertThat(actual.get(2)).usingRecursiveComparison()
                          .isEqualTo(저장된_주문_테이블3);
        });
    }

    @Test
    void 주문_테이블의_empty_값을_참에서_거짓으로_변경할_수_있다() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(empty가_참인_저장된_주문_테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(empty가_거짓인_저장된_주문_테이블);

        // when
        final OrderTable actual = tableService.changeEmpty(empty가_참인_저장된_주문_테이블.getId(), empty가_거짓인_주문_테이블);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).usingRecursiveComparison()
                          .ignoringFields("empty")
                          .isEqualTo(empty가_참인_저장된_주문_테이블);
            softAssertions.assertThat(actual.isEmpty()).isFalse();
        });
    }

    @Test
    void 주문_테이블의_empty_값을_거짓에서_참으로_변경할_수_있다() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(empty가_거짓인_저장된_주문_테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(empty가_참인_저장된_주문_테이블);

        // when
        final OrderTable actual = tableService.changeEmpty(empty가_거짓인_저장된_주문_테이블.getId(), empty가_참인_주문_테이블);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).usingRecursiveComparison()
                          .ignoringFields("empty")
                          .isEqualTo(empty가_거짓인_저장된_주문_테이블);
            softAssertions.assertThat(actual.isEmpty()).isTrue();
        });
    }

    @Test
    void 주문_테이블의_empty_값_변경시_단체_지정_아이디가_null이_아니라면_예외를_반환한다() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(단체_지정_아이디가_있는_주문_테이블));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(단체_지정_아이디가_있는_주문_테이블.getId(), empty가_참인_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 특정_주문_테이블중_조리_혹은_식사_상태인_것이_존재한다면_예외를_반환한다() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(empty가_거짓인_저장된_주문_테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(empty가_거짓인_저장된_주문_테이블.getId(), empty가_참인_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_방문자_수의_값을_변경할_수_있다() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(방문자수가_4인_저장된_주문_테이블));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(방문자수가_2인_저장된_주문_테이블);

        // when
        final OrderTable actual = tableService.changeNumberOfGuests(방문자수가_4인_저장된_주문_테이블.getId(), 방문자수가_2인_저장된_주문_테이블);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).usingRecursiveComparison()
                          .ignoringFields("numberOfGuests")
                          .isEqualTo(방문자수가_2인_저장된_주문_테이블);
            softAssertions.assertThat(actual.getNumberOfGuests()).isEqualTo(2);
        });
    }

    @Test
    void 주문_테이블의_방문자_수를_0으로_변경할시_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(방문자수가_4인_저장된_주문_테이블.getId(), 방문자수가_0인_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_주문_테이블의_방문자_수를_변경할시_예외를_반환한다() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(존재하지_않는_주문_테이블_아이디, 방문자수가_2인_저장된_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_방문자_수를_변경할시_해당_주문_테이블의_empty가_참이라면_예외를_반환한다() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(empty가_참인_저장된_주문_테이블));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(empty가_참인_저장된_주문_테이블.getId(), 방문자수가_2인_저장된_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
