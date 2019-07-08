package dev.andreishilov.ade.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;

import dev.andreishilov.ade.core.services.ReadmeService;

@Model(adaptables = {Resource.class})
public class DocumentPageModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentPageModel.class);

    @Self
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @OSGiService
    private ReadmeService readmeService;

    private String htmlMarkup;

    private String title;

    @PostConstruct
    public void init() {
        LOGGER.trace("DocumentPageModel -> PostConstruct");
        title = resource.getValueMap().get(JcrConstants.JCR_TITLE, String.class);
        htmlMarkup = readmeService.getReadmeContent(resource.getPath(), resourceResolver);
    }

    public String getHtmlMarkup() {
        return htmlMarkup;
    }

    public String getTitle() {
        return title;
    }
}
