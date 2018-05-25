package com.github.andreishilov.ade.core.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Markdown documentation servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "sling/servlet/default",
                "sling.servlet.selectors=markdowndoc",
                "sling.servlet.extensions=html"
        })
public class MarkdownDocServlet extends SlingSafeMethodsServlet {

    private static final String README_MD = "README.md";

    private static final RequestDispatcherOptions OPTIONS = new RequestDispatcherOptions();
    private static final String DOCUMENTATION_PAGE_RESOURCE_TYPE = "aem-documentation-extension/components/page/document-page";

    {
        OPTIONS.setReplaceSelectors(StringUtils.EMPTY); // to remove 'markdowndoc' selector
        OPTIONS.setForceResourceType(DOCUMENTATION_PAGE_RESOURCE_TYPE);
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        final Resource resource = request.getResource();

        final Resource child = resource.getChild(README_MD);

        if (child != null) {

            final RequestDispatcher requestDispatcher = request.getRequestDispatcher(resource, OPTIONS);

            if (requestDispatcher != null) {
                response.setContentType("text/html; charset=UTF-8");
                requestDispatcher.forward(request, response);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
