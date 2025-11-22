package br.com.alura;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import br.com.alura.dispatcher.KafkaDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NewOrderServlet extends HttpServlet {

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<Order>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        for (int i = 0; i < 11; i++) {
            try {
                var email = req.getParameter("email");
                var amount = new BigDecimal(req.getParameter("amount"));

                var orderId = UUID.randomUUID().toString();

                var order = new Order(orderId, amount, email);
                orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order, new CorrelationId(NewOrderServlet.class.getSimpleName()));

                System.out.println("New order sent successfully");
                resp.setStatus(200);
                resp.getWriter().println("New order sent");
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
    }
}
