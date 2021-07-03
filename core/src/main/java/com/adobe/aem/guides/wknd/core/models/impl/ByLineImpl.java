package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.ByLine;
import com.adobe.cq.wcm.core.components.models.Image;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {ByLine.class},
        resourceType = {ByLineImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ByLineImpl implements ByLine {
    protected static final String RESOURCE_TYPE = "wknd/components/content/byline";

    @ValueMapValue
    private String name;

    @ValueMapValue
    private List<String> occupations;

    @OSGiService
    private ModelFactory modelFactory;

    @Self
    private SlingHttpServletRequest request;

    private Image image;

    @PostConstruct
    private void init() {
        image = modelFactory.getModelFromWrappedRequest(request, request.getResource(), Image.class);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getOccupations() {
        if (occupations != null) {
            Collections.sort(occupations);
            return new ArrayList<>(occupations);
        } else {
            return Collections.emptyList();
        }
    }

    private Image getImage() {
        return image;
    }

    @Override
    public boolean isEmpty() {
        final Image componentImage = getImage();
        if (StringUtils.isEmpty(name)) {
            return true;
        } else if (occupations == null || occupations.isEmpty()) {
            return true;
        } else if (componentImage == null || StringUtils.isEmpty(componentImage.getSrc())) {
            return true;
        } else {
            /* Everything is populated, so this component is not considered empty */
            return false;
        }
    }
}
