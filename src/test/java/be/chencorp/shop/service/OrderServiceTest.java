package be.chencorp.shop.service;

import be.chencorp.shop.ExceptionMatcher;
import be.chencorp.shop.assembler.OrderResourceAssembler;
import be.chencorp.shop.converter.OrderConverter;
import be.chencorp.shop.exception.ResourceNotFoundException;
import be.chencorp.shop.model.OrderUser;
import be.chencorp.shop.repository.OrderRepository;
import be.chencorp.shop.repository.ProductRepository;
import be.chencorp.shop.resource.OrderResource;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    private final Integer ORDER_ID = 1;

    @Spy
    @InjectMocks
    OrderService orderService;

    @Mock
    ProductRepository productRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderResourceAssembler orderResourceAssembler;

    @Mock
    OrderConverter orderConverter;

    @Mock
    OrderUser orderUser;
    @Mock
    OrderResource orderResource;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetById(){
        Mockito.when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(orderUser));
        Mockito.when(orderResourceAssembler.convert(orderUser)).thenReturn(orderResource);

        OrderResource order = orderService.getById(ORDER_ID);
        Assertions.assertThat(order).isEqualTo(orderResource);
    }

    @Test
    public void testIdNotFound() {
        exception.expect(ResourceNotFoundException.class);
        exception.expect(ExceptionMatcher.hasCode("ORDER_NOT_FOUND"));

        Mockito.when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());

        orderService.getById(ORDER_ID);
    }

    @Test
    public void testCreate(){
        OrderUser newOrderUser = new OrderUser();
        Mockito.when(orderConverter.convert(orderResource)).thenReturn(orderUser);
        Mockito.when(orderRepository.save(orderUser)).thenReturn(newOrderUser);
        Mockito.when(orderResourceAssembler.convert(newOrderUser)).thenReturn(orderResource);

        OrderResource savedOrder = orderService.create(orderResource);

        Assertions.assertThat(savedOrder).isEqualTo(orderResource);
        Mockito.verify(orderService).getCurrentDate();
    }

    @Test
    public void testListAll(){
        OrderUser orderUser_1 = Mockito.mock(OrderUser.class);
        OrderUser orderUser_2 = Mockito.mock(OrderUser.class);
        List<OrderUser> orderUserList = Lists.newArrayList(orderUser_1, orderUser_2);

        Mockito.when(orderRepository.findAll()).thenReturn(orderUserList);
        Mockito.when(orderResourceAssembler.convert(orderUser_1)).thenReturn(orderResource);
        Mockito.when(orderResourceAssembler.convert(orderUser_2)).thenReturn(orderResource);

        List<OrderResource> orderResources = orderService.listAll();

        Assertions.assertThat(orderResources.get(0)).isEqualTo(orderResource);
        Mockito.verify(orderResourceAssembler).convert(orderUser_1);
        Mockito.verify(orderResourceAssembler).convert(orderUser_2);
    }

}
