package cocodas.prier.orders.orders;

import cocodas.prier.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;

    @Transactional
    public Orders createOrder(Users user) {
        Orders order = Orders.builder()
                .user(user)
                .build();
        return ordersRepository.save(order);
    }
}
