package messiasproject.microservicespecialist.order.domain.service.implementation;

import messiasproject.microservicespecialist.order.application.representation.ItemsOrderRepresention;
import messiasproject.microservicespecialist.order.application.representation.OrderRepresentation;
import messiasproject.microservicespecialist.order.domain.exception.RecordDoesntExist;
import messiasproject.microservicespecialist.order.domain.model.entity.OrderEntity;
import messiasproject.microservicespecialist.order.domain.model.enums.OrderStatus;
import messiasproject.microservicespecialist.order.infra.openfeign.interfaces.FindAllItemsOrder;
import messiasproject.microservicespecialist.order.infra.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SearchOrderByUuidImplementationTest {

    private OrderRepository repository;
    private FindAllItemsOrder findAllItemsOrder;
    private OrderEntity orderEntity;
    private final String uuid = "uuid";
    private ItemsOrderRepresention itemsOrderRepresention;
    private List<ItemsOrderRepresention> items;
    private SearchOrderByUuidImplementation searchOrderByUuidImplementation;


    @BeforeEach
    void setUp(){
        repository = mock(OrderRepository.class);
        findAllItemsOrder = mock(FindAllItemsOrder.class);
        orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setUuid(uuid);
        orderEntity.setStatus(OrderStatus.RECEBIDO);
        orderEntity.setTotalValue(2500d);
        orderEntity.setDateCreation(LocalDateTime.MIN);
        itemsOrderRepresention = new ItemsOrderRepresention();
        itemsOrderRepresention.setCount(1);
        itemsOrderRepresention.setProductName("tv");
        itemsOrderRepresention.setUnitaryPrice(2000d);
        items = Arrays.asList(itemsOrderRepresention);
        searchOrderByUuidImplementation = new SearchOrderByUuidImplementation(repository, findAllItemsOrder);
    }


    @Test
    void shoudSearchOrderByUuidWithSuccessTest(){
        when(repository.findByUuid(uuid)).thenReturn(Optional.of(orderEntity));
        when(findAllItemsOrder.find(uuid)).thenReturn(items);
        OrderRepresentation orderRepresentation = searchOrderByUuidImplementation.search(uuid);
        assertEquals(orderRepresentation.getUuid(), orderEntity.getUuid());
        assertEquals(orderRepresentation.getTotalValue(), orderEntity.getTotalValue());
        verify(repository).findByUuid(uuid);
        verify(findAllItemsOrder).find(uuid);
    }

    @Test
    void shoudThrowExceptionRecordDoesntExistTest(){
        when(repository.findByUuid(uuid)).thenReturn(Optional.empty());
        assertThrows(RecordDoesntExist.class, () -> {
            searchOrderByUuidImplementation.search(uuid);
        });
        verifyNoInteractions(findAllItemsOrder);
    }
}
