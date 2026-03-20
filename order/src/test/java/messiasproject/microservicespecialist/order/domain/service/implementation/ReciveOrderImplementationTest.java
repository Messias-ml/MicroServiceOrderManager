package messiasproject.microservicespecialist.order.domain.service.implementation;

import messiasproject.microservicespecialist.order.application.representation.CodeOrder;
import messiasproject.microservicespecialist.order.application.representation.ItemOrderDTO;
import messiasproject.microservicespecialist.order.application.representation.ReceivingOrder;
import messiasproject.microservicespecialist.order.domain.exception.ListEmptyException;
import messiasproject.microservicespecialist.order.domain.model.entity.OrderEntity;
import messiasproject.microservicespecialist.order.infra.rabbitmq.producer.SendNotificationReceiptOrder;
import messiasproject.microservicespecialist.order.infra.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReciveOrderImplementationTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private SendNotificationReceiptOrder sendNotificationReceiptOrder;

    @InjectMocks
    private ReciveOrderImplementation reciveOrderImplementation;

    private ReceivingOrder receivingOrder;

    private CodeOrder codeOrder;

    private OrderEntity orderEntity;

    @BeforeEach
    void setUp(){
        ItemOrderDTO itemOrderDTO = new ItemOrderDTO();
        itemOrderDTO.setCount(1);
        itemOrderDTO.setPrice(256d);
        itemOrderDTO.setProductName("celular");
        itemOrderDTO.setUuidProduct("1234");
        reciveOrderImplementation = new ReciveOrderImplementation(repository, sendNotificationReceiptOrder);
        receivingOrder = new ReceivingOrder();
        receivingOrder.setListOrderItem(Arrays.asList(itemOrderDTO));
        codeOrder = new CodeOrder("or-111111");
        orderEntity = new OrderEntity();
        orderEntity.setUuid(codeOrder.getCodeOrder());
    }

    @Test
    void shoudReturnCodeOrderWithSuccess(){
        when(repository.save(any())).thenReturn(orderEntity);
        CodeOrder codeOrderRecived = reciveOrderImplementation.reciveOrder(receivingOrder);
        assertEquals(codeOrderRecived.getCodeOrder(), codeOrder.getCodeOrder());
        verify(repository).save(any());
        verify(sendNotificationReceiptOrder, times(1)).send(any());
    }

    @Test
    void shoudThrowExceptionListEmptyException(){
        receivingOrder.setListOrderItem(null);
        assertThrows(ListEmptyException.class, () -> {
            reciveOrderImplementation.reciveOrder(receivingOrder);
        });

        verifyNoInteractions(repository);
        verifyNoInteractions(sendNotificationReceiptOrder);
    }
}
