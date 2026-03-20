package messiasproject.microservicespecialist.order.domain.service;

import messiasproject.microservicespecialist.order.application.representation.*;
import messiasproject.microservicespecialist.order.application.representation.specification.OrderFilterSpec;
import messiasproject.microservicespecialist.order.infra.ReciveOrder;
import messiasproject.microservicespecialist.order.infra.SearchOrderByData;
import messiasproject.microservicespecialist.order.infra.SearchOrderByUuid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private SearchOrderByUuid searchOrderByUuid;
    private ReciveOrder reciveOrder;
    private SearchOrderByData searchOrderByData;
    private OrderRepresentation orderRepresentation;
    private ItemsOrderRepresention itemsOrderRepresention;
    private final String uuidCorreto = "uuid correto";
    private OrderService service;
    private ReceivingOrder receivingOrder;
    private ItemOrderDTO itemOrderDTO;
    private CodeOrder codeOrder;
    private Pageable pageable;
    private OrderFilterSpec filter;

    @BeforeEach
    void setUp(){
        pageable = Pageable.unpaged();
        filter = new OrderFilterSpec();
        receivingOrder = new ReceivingOrder();
        orderRepresentation = new OrderRepresentation();
        itemsOrderRepresention = new ItemsOrderRepresention();
        itemsOrderRepresention.setCount(1);
        itemsOrderRepresention.setProductName("tv");
        itemsOrderRepresention.setUnitaryPrice(2000d);
        List<ItemsOrderRepresention> items = Arrays.asList(itemsOrderRepresention);
        orderRepresentation.setUuid(uuidCorreto);
        orderRepresentation.setItems(items);
        orderRepresentation.setTotalValue(10d);
        searchOrderByUuid = mock(SearchOrderByUuid.class);
        reciveOrder = mock(ReciveOrder.class);
        searchOrderByData = mock(SearchOrderByData.class);
        service = new OrderService(reciveOrder, searchOrderByUuid, searchOrderByData);
        ItemOrderDTO itemOrderDTO = new ItemOrderDTO();
        itemOrderDTO.setCount(1);
        itemOrderDTO.setPrice(256d);
        itemOrderDTO.setProductName("celular");
        itemOrderDTO.setUuidProduct("1234");
        receivingOrder.setListOrderItem(Arrays.asList(itemOrderDTO));
        codeOrder = new CodeOrder("pd-111111");
    }

    @Test
    void shouldSearchOrderByUuidWithSuccess(){
        when(searchOrderByUuid.search("uuid correto")).thenReturn(orderRepresentation);
        OrderRepresentation orderReturned = service.searchOrderByUuid(uuidCorreto);
        assertEquals(orderRepresentation.getUuid(), orderReturned.getUuid());
        assertEquals(orderRepresentation.getTotalValue(), orderReturned.getTotalValue());
        assertEquals(orderRepresentation.getItems(), orderReturned.getItems());
        verify(searchOrderByUuid).search(uuidCorreto);
    }

    @Test
    void shouldReciveOrderWithSuccess(){
        when(reciveOrder.reciveOrder(receivingOrder)).thenReturn(codeOrder);
        CodeOrder codeOrderReturned = service.reciveOrder(receivingOrder);
        assertEquals(codeOrder, codeOrderReturned);
        verify(reciveOrder).reciveOrder(receivingOrder);
    }


    @Test
    void shouldSearchOrderByData(){
        Page<OrderRepresentation> page =
                new PageImpl<>(List.of(orderRepresentation));
        when(searchOrderByData.search(pageable, filter)).thenReturn(page);
        Page<OrderRepresentation> pageOrder = service.searchOrderByData(pageable, filter);
        assertEquals(1, pageOrder.getContent().size());
        assertEquals(pageOrder.getContent().get(0).getUuid(), orderRepresentation.getUuid());
        verify(searchOrderByData).search(pageable, filter);
    }
}
