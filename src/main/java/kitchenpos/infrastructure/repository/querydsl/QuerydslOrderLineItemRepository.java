package kitchenpos.infrastructure.repository.querydsl;

import static kitchenpos.domain.order.QOrderLineItem.orderLineItem;
import static kitchenpos.infrastructure.repository.querydsl.QuerydslUtils.nullSafeBuilder;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItemRepository;
import org.springframework.stereotype.Repository;

@Repository
public class QuerydslOrderLineItemRepository implements OrderLineItemRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public QuerydslOrderLineItemRepository(final EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public OrderLineItem save(final OrderLineItem entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public Optional<OrderLineItem> findById(final Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(
                queryFactory.selectFrom(orderLineItem)
                        .where(idEq(id))
                        .fetchOne()
        );
    }

    @Override
    public List<OrderLineItem> findAll() {
        return queryFactory.selectFrom(orderLineItem)
                .fetch();
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(final Long orderId) {
        return queryFactory.selectFrom(orderLineItem)
                .where(orderIdEq(orderId))
                .fetch();
    }

    private BooleanBuilder orderIdEq(final Long orderId) {
        return nullSafeBuilder(() -> orderLineItem.order.id.eq(orderId));
    }

    private BooleanBuilder idEq(final Long id) {
        return nullSafeBuilder(() -> orderLineItem.seq.eq(id));
    }
}
