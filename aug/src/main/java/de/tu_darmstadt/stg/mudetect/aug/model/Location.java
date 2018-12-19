package de.tu_darmstadt.stg.mudetect.aug.model;

import java.io.Serializable;

public class Location implements Serializable {
    private final String projectName;
    private final String filePath;
    private final String methodSignature;

    public Location(String projectName, String filePath, String methodSignature) {
        this.projectName = projectName;
        this.filePath = filePath;
        this.methodSignature = methodSignature;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getMethodSignature() {
        return methodSignature;
    }
}
