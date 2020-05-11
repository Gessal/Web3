package servlet;

import exception.DBException;
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

public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long money = 0L;
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        Map<String, Object> map = new HashMap<>();
        try {
            money = Long.parseLong(req.getParameter("money"));
            if (new BankClientService().addClient(new BankClient(name, password, money))) {
                map.put("message", "Add client successful");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                map.put("message", "Client not add");
            }
            resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
            resp.setContentType("text/html;charset=utf-8");
        } catch (Exception e) {
            map.put("message", "Client not add");
            resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
            resp.setContentType("text/html;charset=utf-8");
        }
    }
}
