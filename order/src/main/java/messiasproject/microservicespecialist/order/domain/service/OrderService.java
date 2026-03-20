package messiasproject.microservicespecialist.order.domain.service;

import lombok.RequiredArgsConstructor;
import messiasproject.microservicespecialist.order.application.representation.CodeOrder;
import messiasproject.microservicespecialist.order.application.representation.OrderRepresentation;
import messiasproject.microservicespecialist.order.application.representation.ReceivingOrder;
import messiasproject.microservicespecialist.order.application.representation.specification.OrderFilterSpec;
import messiasproject.microservicespecialist.order.infra.ReciveOrder;
import messiasproject.microservicespecialist.order.infra.SearchOrderByData;
import messiasproject.microservicespecialist.order.infra.SearchOrderByUuid;
import messiasproject.microservicespecialist.order.infra.rabbitmq.consumer.UpdateOrder;
import messiasproject.microservicespecialist.order.infra.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ReciveOrder reciveOrder;

    private final SearchOrderByUuid searchOrderByUuid;

    private final SearchOrderByData searchOrderByData;

    public CodeOrder reciveOrder(ReceivingOrder order) {
        return reciveOrder.reciveOrder(order);
    }
    
    public OrderRepresentation searchOrderByUuid(String uuid) {
        return searchOrderByUuid.search(uuid);
    }

    public Page<OrderRepresentation> searchOrderByData(Pageable page, OrderFilterSpec orderFilterSpec) {
        return searchOrderByData.search(page, orderFilterSpec);
    }

}
