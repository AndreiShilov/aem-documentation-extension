package com.github.andreishilov.services

import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.SlingHttpServletResponse
import org.apache.sling.api.servlets.HttpConstants
import org.apache.sling.api.servlets.SlingSafeMethodsServlet
import org.osgi.framework.Constants
import org.osgi.service.component.annotations.Component
import javax.servlet.Servlet

@Component(service = [Servlet::class],
        property = arrayOf(Constants.SERVICE_DESCRIPTION + "=Simple Kotlin Demo Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/services/kotlin/demo/servlet"))
class SimpleCheckServlet : SlingSafeMethodsServlet() {

    override fun doGet(request: SlingHttpServletRequest, response: SlingHttpServletResponse) {
        response.writer.write("Hello from the new sub project")
    }
}