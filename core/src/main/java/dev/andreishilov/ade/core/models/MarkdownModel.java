package dev.andreishilov.ade.core.models;


import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.ContentVariation;

import dev.andreishilov.ade.core.services.ReadmeService;
import dev.andreishilov.ade.core.utils.MarkDownUtils;

@Model(adaptables = {Resource.class})
public class MarkdownModel {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkdownModel.class);
    private static final String NOT_FOUND_MESSAGE = "Could not find content fragment under path = [%s]";
    private static final String MASTER = "master";
    private static final String MAIN_ELEMENT = "main";

    @ValueMapValue
    @Optional
    private String fileReference;

    @ValueMapValue
    @Optional
    private String text;

    @ValueMapValue
    private String type;

    @ValueMapValue
    @Optional
    private String cmpPath;

    @ValueMapValue
    @Default(values = MAIN_ELEMENT)
    private String element;

    @ValueMapValue
    @Default(values = MASTER)
    private String variation;

    @OSGiService
    private ReadmeService readmeService;

    @SlingObject
    private ResourceResolver resourceResolver;

    private String markup;

    private String styles;

    @PostConstruct
    public void init() {
        LOGGER.trace("{} init started", MarkdownModel.class.getCanonicalName());

        final MarkdownSource markdownSource = MarkdownSource.valueOf(type.toUpperCase());

        switch (markdownSource) {
            case CFM:
                markup = getMarkupFromCfm();
                break;
            case TEXT:
                markup = MarkDownUtils.markdownToHtml(text);
                break;
            case README:
                markup = readmeService.getReadmeContent(cmpPath, resourceResolver);
                break;
            default:
                setErrorStyles();
                markup = "Unknown type. Allowed types = ['cfm','text']. Current type = " + type;
        }

        LOGGER.debug("Current state {}", this);
    }

    private void setErrorStyles() {
        this.styles = "border: 2px solid red; min-height: 100px; padding: 5px;";
    }

    private String getMarkupFromCfm() {
        if (fileReference == null) {
            setErrorStyles();
            return null;
        }

        final Resource resource = resourceResolver.getResource(fileReference);

        if (resource == null) {
            setErrorStyles();
            return String.format(NOT_FOUND_MESSAGE, fileReference);
        }

        final ContentFragment contentFragment = resource.adaptTo(ContentFragment.class);

        if (contentFragment == null) {
            setErrorStyles();
            LOGGER.warn("Seems that resource is not a content fragment. Path = [{}]", this.fileReference);
            return "Seems that reference points not to a content fragment";
        }

        final ContentElement contentElement = contentFragment.getElement(this.element);

        final String content;

        if (MASTER.equals(this.variation)) {
            content = contentElement.getContent();
        } else {

            final ContentVariation contentVariation = contentElement.getVariation(this.variation);

            if (contentVariation == null) {
                setErrorStyles();
                content = "Could not find requested variation '" + this.variation + "'";
            } else {
                content = contentVariation.getContent();
            }
        }

        return MarkDownUtils.markdownToHtml(content);
    }


    public String getMarkup() {
        return markup;
    }

    public String getStyles() {
        return styles;
    }

    @Override
    public String toString() {
        return "MarkdownModel{" +
                "fileReference='" + fileReference + '\'' +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", cmpPath='" + cmpPath + '\'' +
                ", element='" + element + '\'' +
                ", variation='" + variation + '\'' +
                '}';
    }

    private enum MarkdownSource {
        CFM, TEXT, README
    }
}
