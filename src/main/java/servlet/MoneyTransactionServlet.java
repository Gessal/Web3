package servlet;

import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /* Вместо построения страницы результатов делаю перенаправление запроса */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String senderName = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        String nameTo = req.getParameter("nameTo");
        try {
            Long count = Long.parseLong(req.getParameter("count"));
            BankClient sender = bankClientService.getClientByName(senderName);
            if (bankClientService.checkPassword(senderName, senderPass) &&
                    bankClientService.sendMoneyToClient(sender, nameTo, count)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setHeader("message", "The transaction was successful");
            } else {
                resp.setHeader("message", "transaction rejected");
            }
        } catch (Exception e) {
            resp.setHeader("message", "transaction rejected");
        }
        finally {
            ServletContext servletContext = getServletContext();
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/result");
            requestDispatcher.forward(req, resp);
        }
    }
}
