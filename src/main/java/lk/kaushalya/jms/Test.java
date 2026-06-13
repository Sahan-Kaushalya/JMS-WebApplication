package lk.kaushalya.jms;

import jakarta.annotation.Resource;
import jakarta.jms.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;

@WebServlet("/test")
public class Test extends HttpServlet {

    @Resource(lookup = "jms/myDefaultConnectionFactory")
    private ConnectionFactory factory;

    @Resource(lookup = "myTopic")
    private Topic topic;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
//            InitialContext ic = new InitialContext();
//            ConnectionFactory factory = (ConnectionFactory) ic.lookup("jms/myDefaultConnectionFactory");

            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            Topic topic = session.createTopic("newTopic");
            MessageProducer producer = session.createProducer(topic);
            for (int i = 1; i <= 10; i++) {
                String line = "Default Connection Message " + i + " - JMS-SecondClient -";
                TextMessage txtMessage = session.createTextMessage();
                txtMessage.setText(line);

                producer.send(txtMessage);
            }

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

    }
}

