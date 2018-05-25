package com.github.andreishilov.ade.core.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface ReadmeService {

    String getReadmeContent(String componentPath, ResourceResolver resourceResolver);
}
