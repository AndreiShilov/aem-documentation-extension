package com.github.andreishilov.ade.core.services.impl;

import static com.day.crx.JcrConstants.JCR_CONTENT;
import static com.day.crx.JcrConstants.JCR_DATA;

import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.andreishilov.ade.core.services.ReadmeService;
import com.github.andreishilov.ade.core.utils.MarkDownUtils;

@Component(service = ReadmeService.class)
public class ReadmeServiceImpl implements ReadmeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadmeServiceImpl.class);

    private static final String README_MD = "README.md";

    @Override
    public String getReadmeContent(final String componentPath, final ResourceResolver resourceResolver) {

        final Resource componentResource = resourceResolver.getResource(componentPath);


        final Resource readMeResource = componentResource.getChild(README_MD);

        if (readMeResource == null) {
            LOGGER.error("Readme.md resource is null, should be not null, checked at MarkdownDocServlet.java. Resource path = [{}]",
                    componentResource.getPath());
            return null;
        }

        try {
            final Node readMeNode = readMeResource.adaptTo(Node.class);

            if (readMeNode == null) {
                LOGGER.error("Could not adapt resource to Node.class. Resource path = [{}]", readMeResource.getPath());
                return null;
            }

            final Node readMeJcrContentNode = readMeNode.getNode(JCR_CONTENT);

            final InputStream inputStream = readMeJcrContentNode.getProperty(JCR_DATA).getBinary().getStream();

            return MarkDownUtils.markdownToHtml(IOUtils.toString(inputStream, "UTF-8"));

        } catch (RepositoryException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            return e.getMessage() + ". Please checkout logs";
        }
    }
}
