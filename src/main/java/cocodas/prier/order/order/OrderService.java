package cocodas.prier.order.order;

import cocodas.prier.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;

    @Transactional
    public Orders createOrder(Users users) {
        Orders order = Orders.builder()
                .users(users)
                .build();
        return ordersRepository.save(order);
    }
}
