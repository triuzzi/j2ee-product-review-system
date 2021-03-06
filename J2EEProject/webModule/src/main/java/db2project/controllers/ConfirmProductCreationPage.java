package db2project.controllers;

import db2project.services.CreationService;
import db2project.services.ProductService;
import db2project.utils.Utils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@MultipartConfig
@WebServlet(name = "ConfirmProductCreationPage", value = "/admin/ConfirmProductCreationPage")
public class ConfirmProductCreationPage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/ProductService")
    private ProductService prodService;

    public ConfirmProductCreationPage() {
        super();
    }

    public void init() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setPrefix("/WEB-INF/admin/");
        templateResolver.setSuffix(".html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final WebContext ctx = new WebContext(request, response, getServletContext());
        CreationService creationService = (CreationService) request.getSession().getAttribute("creationService");
        if (creationService == null) {
            // Se non esiste un creationService il workflow non è quello giusto (viene creato nella GoToCreationPage)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "AddQuestionCreationPage Error: Invalid session!");
            return;
        }
        try {
            String name = request.getParameter("name");
            if (name == null || !name.matches(".*[a-zA-Z]+.*"))
                throw new Exception("You must provide a valid name for the product");
            String dateStr = request.getParameter("date");
            if (dateStr == null)
                throw new Exception("You must provide a date for the product");
            Date date = Utils.utcDateFromString(dateStr);
            if (Utils.isBeforeToday(date))
                throw new Exception("You cannot provide a date before today");
            if (prodService.getProductOfDay(date) != null)
                throw new Exception("There is already a product for the selected day");
            Part imgFile = request.getPart("picture");
            if (imgFile == null || imgFile.getSize() == 0)
                throw new Exception("You must provide an image for the product");
            InputStream imgContent = imgFile.getInputStream();
            creationService.setDate(date);
            creationService.setProductName(name);
            creationService.setImgByteArray(Utils.readImage(imgContent));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            ctx.setVariable("displayMsg", e.getMessage());
            //response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        ctx.setVariable("creationService", creationService);
        templateEngine.process("creationPage", ctx, response.getWriter());
    }

    public void destroy() { }
}
