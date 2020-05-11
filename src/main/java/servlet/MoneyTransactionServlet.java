package servlet;

import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> map = new HashMap<>();
        String senderName = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        String nameTo = req.getParameter("nameTo");
        try {
            Long count = Long.parseLong(req.getParameter("count"));
            BankClient sender = bankClientService.getClientByName(senderName);
            if (sender.getPassword().equals(senderPass)) {
                if (bankClientService.sendMoneyToClient(sender, nameTo, count)) {
                    map.put("message", "The transaction was successful");
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    map.put("message", "transaction rejected");
                }
            } else {
                map.put("message", "transaction rejected");
            }
            resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
            resp.setContentType("text/html;charset=utf-8");
        } catch (Exception e) {
            map.put("message", "transaction rejected");
            resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
            resp.setContentType("text/html;charset=utf-8");
        }
    }
}
