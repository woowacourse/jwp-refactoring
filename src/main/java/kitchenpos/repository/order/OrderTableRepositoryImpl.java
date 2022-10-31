package kitchenpos.repository.order;

import static java.util.Collections.emptyList;
import static kitchenpos.domain.QOrder.order;
import static kitchenpos.domain.QOrderTable.orderTable;
import static kitchenpos.domain.QTableGroup.tableGroup;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.QOrderTable;

public class OrderTableRepositoryImpl implements OrderTableRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QOrderTable qOrderTable = new QOrderTable("orderTable");

    public OrderTableRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<OrderTable> findWithTableGroupByIdIn(List<Long> ids) {

        if (ids == null || ids.isEmpty() || ids.contains(null)) {
            return emptyList();
        }

        return queryFactory
                .selectFrom(orderTable)
                .distinct()
                .where(orderTable.id.in(ids))
                .leftJoin(orderTable.tableGroup, tableGroup)
                .fetchJoin()
                .fetch();
    }

    @Override
    public Optional<OrderTable> findWithOrdersAndTableGroupById(Long id) {

        final OrderTable orderTable = queryFactory
                .selectFrom(qOrderTable)
                .distinct()
                .where(qOrderTable.id.eq(id))
                .leftJoin(qOrderTable.orders, order)
                .fetchJoin()
                .leftJoin(qOrderTable.tableGroup, tableGroup)
                .fetchJoin()
                .fetchFirst();

        return Optional.ofNullable(orderTable);
    }

}
