package com.wevserver.application.feature;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Feature {

    private String module;

    private String path;

    private Integer counter;

    private FeatureType type;

    private Class<?> entity;

    private String messageCode;
}
