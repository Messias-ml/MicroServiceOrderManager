package messiasproject.microservicespecialist.order.domain.service.implementation;

import messiasproject.microservicespecialist.order.application.representation.ItemsOrderRepresention;
import messiasproject.microservicespecialist.order.application.representation.OrderRepresentation;
import messiasproject.microservicespecialist.order.application.representation.specification.OrderFilterSpec;
import messiasproject.microservicespecialist.order.domain.exception.ListEmptyException;
import messiasproject.microservicespecialist.order.domain.model.entity.OrderEntity;
import messiasproject.microservicespecialist.order.domain.model.enums.OrderStatus;
import messiasproject.microservicespecialist.order.infra.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SearchOrderByDataImplementationTest {

    private Pageable pageable;
    private OrderFilterSpec filter;
    private OrderRepresentation orderRepresentation;
    private ItemsOrderRepresention itemsOrderRepresention;
    private final String uuidCorreto = "uuid correto";
    private SearchOrderByDataImplementation searchOrderByDataImplementation;
    private OrderRepository repository;
    private OrderEntity orderEntity;

    @BeforeEach
    void setUp(){
        orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setTotalValue(2500d);
        orderEntity.setStatus(OrderStatus.RECEBIDO);
        orderEntity.setDateCreation(LocalDateTime.MIN);
        orderEntity.setUuid(uuidCorreto);
        repository = mock(OrderRepository.class);
        searchOrderByDataImplementation = new SearchOrderByDataImplementation(repository);
        pageable = Pageable.unpaged();
        filter = new OrderFilterSpec();
    }

    @Test
    void shoudReturnOrderByDataWithSuccess(){
        Page<OrderEntity> page =
                new PageImpl<>(List.of(orderEntity));
        when(repository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);
        Page<OrderRepresentation> orderRepresentationReturned = searchOrderByDataImplementation.search(pageable, filter);
        assertEquals(orderRepresentationReturned.getContent().get(0).getUuid(), orderEntity.getUuid());
        assertEquals(orderRepresentationReturned.getContent().get(0).getTotalValue(), orderEntity.getTotalValue());
        verify(repository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void shoudThrowExceptionListEmptyException(){
        when(repository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        assertThrows(ListEmptyException.class, () -> {
            searchOrderByDataImplementation.search(pageable, filter);
        });
    }
}
