package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.ByLine;
import com.adobe.cq.wcm.core.components.models.Image;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ByLineImplTest {

    private final AemContext context = new AemContext();

    @Mock
    private Image image;

    @Mock
    private ModelFactory modelFactory;

    @BeforeEach
    void setUp() throws Exception {
        context.addModelsForClasses(ByLineImpl.class);
        context.load().json("/com/adobe/aem/guides/wknd/core/models/impl/BylineImplTest.json", "/content");
        lenient().when(modelFactory.getModelFromWrappedRequest(eq(context.request()), any(Resource.class), eq(Image.class)))
                .thenReturn(image);

        context.registerService(ModelFactory.class, modelFactory, org.osgi.framework.Constants.SERVICE_RANKING,
                Integer.MAX_VALUE);
    }

    @Test
    void testGetName() {
        final String expected = "Jane Doe";

        context.currentResource("/content/byline");
        ByLine byLine = context.request().adaptTo(ByLine.class);
        String actual = byLine.getName();
        assertEquals(expected, actual);
    }

    @Test
    void testGetOccupations() {
        List<String> expected = Arrays.asList("Blogger", "Photographer", "Youtuber");
        context.currentResource("/content/byline");
        ByLine byLine = context.request().adaptTo(ByLine.class);
        List<String> actual = byLine.getOccupations();
        assertEquals(expected, actual);
    }

    @Test
    void testIsEmpty() {
        context.currentResource("/content/empty");
        ByLine byLine = context.request().adaptTo(ByLine.class);
        assertTrue(byLine.isEmpty());
    }

    @Test
    void testIsEmpty_WithoutName() {
        context.currentResource("/content/without-name");
        ByLine byLine = context.request().adaptTo(ByLine.class);
        assertTrue(byLine.isEmpty());
    }

    @Test
    void testIsEmpty_WithoutOccupations() {
        context.currentResource("/content/without-occupations");
        ByLine byLine = context.request().adaptTo(ByLine.class);
        assertTrue(byLine.isEmpty());
    }

    @Test
    void testIsEmpty_WithoutImage() {
        context.currentResource("/content/byline");
        lenient().when(modelFactory.getModelFromWrappedRequest(
                eq(context.request()),
                any(Resource.class),
                eq(Image.class)))
                .thenReturn(null);
        ByLine byLine = context.request().adaptTo(ByLine.class);
        assertTrue(byLine.isEmpty());
    }

    @Test
    void testIsEmpty_WithoutImageSrc() {
        context.currentResource("/content/byline");
        when(image.getSrc()).thenReturn("");
        ByLine byLine = context.request().adaptTo(ByLine.class);
        assertTrue(byLine.isEmpty());
    }

    @Test
    void testIsNotEmpty() {
        context.currentResource("/content/byline");
        when(image.getSrc()).thenReturn("/content/bio.png");
        ByLine byLine = context.request().adaptTo(ByLine.class);
        assertFalse(byLine.isEmpty());
    }

}