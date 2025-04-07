package com.wevserver.ui;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
public class Pagination {

    private final HttpServletRequest httpServletRequest;
    private final Page<?> page;

    public Map<String, Object> getModel() {

        final Map<String, Object> model = new HashMap<>();

        if (page.getTotalPages() == 0) {

            return model;
        }

        model.put(
                "firstPage",
                new PageItem(
                        ServletUriComponentsBuilder.fromRequest(httpServletRequest)
                                .replaceQueryParam("pageNumber", 0)
                                .build()
                                .toUriString(),
                        page.getNumber() == 0,
                        0));

        if (page.getTotalPages() == 1) {

            return model;
        }

        if (page.getNumber() != 0 && page.getNumber() != page.getTotalPages() - 1) {

            model.put(
                    "currentPage",
                    new PageItem(
                            ServletUriComponentsBuilder.fromRequest(httpServletRequest)
                                    .replaceQueryParam("pageNumber", page.getNumber())
                                    .build()
                                    .toUriString(),
                            true,
                            page.getNumber()));
        }

        model.put(
                "lastPage",
                new PageItem(
                        ServletUriComponentsBuilder.fromRequest(httpServletRequest)
                                .replaceQueryParam("pageNumber", page.getTotalPages() - 1)
                                .build()
                                .toUriString(),
                        page.getNumber() == page.getTotalPages() - 1,
                        page.getTotalPages() - 1));

        if (page.getNumber() > 0) {

            model.put(
                    "previousPage",
                    ServletUriComponentsBuilder.fromRequest(httpServletRequest)
                            .replaceQueryParam("pageNumber", page.getNumber() - 1)
                            .build()
                            .toUriString());
        }

        if (page.getNumber() < page.getTotalPages() - 1) {

            model.put(
                    "nextPage",
                    ServletUriComponentsBuilder.fromRequest(httpServletRequest)
                            .replaceQueryParam("pageNumber", page.getNumber() + 1)
                            .build()
                            .toUriString());
        }

        return model;
    }

    private record PageItem(String link, Boolean current, Integer number) {}
}
