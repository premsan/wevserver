package com.wevserver.application.feature;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Feature {

    private String module;

    private String addPath;

    private String path;

    private Integer counter;

    private Class<?> entity;

    private String messageCode;
}
