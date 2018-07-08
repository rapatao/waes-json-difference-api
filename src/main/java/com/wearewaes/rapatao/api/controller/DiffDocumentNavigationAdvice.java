package com.wearewaes.rapatao.api.controller;

import com.wearewaes.rapatao.api.domain.DiffDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * This Spring Controller Advice adds to the response header the navigation links.
 * The implementation is inspired on the Github navigation links implementation and is proposed by the RFC 5988.
 *
 * @see <a href="https://tools.ietf.org/html/rfc5988#page-6">RFC 5988, The Link Header Field</a>
 */
@ControllerAdvice
public class DiffDocumentNavigationAdvice implements ResponseBodyAdvice<DiffDocument> {

    private final Logger logger = LoggerFactory.getLogger(DiffDocumentNavigationAdvice.class);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return ((ParameterizedType) returnType.getGenericParameterType()).getActualTypeArguments()[0] == DiffDocument.class;
    }

    @Override
    public DiffDocument beforeBodyWrite(DiffDocument body, MethodParameter returnType, MediaType selectedContentType,
                                        Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                        ServerHttpRequest request, ServerHttpResponse response) {

        try {
            final List<String> links = new ArrayList<>();
            links.add("<" + linkTo(methodOn(DiffController.class).addLeft(body.getId(), null)).toString() + ">; rel=\"left\"");
            links.add("<" + linkTo(methodOn(DiffController.class).addRight(body.getId(), null)).toString() + ">; rel=\"right\"");
            if (body.getLeft() != null && body.getRight() != null) {
                links.add("<" + linkTo(methodOn(DiffController.class).difference(body.getId())).toString() + ">; rel=\"diff\"");
            }

            response.getHeaders().add("Link", String.join(", ", links));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return body;
    }

}
