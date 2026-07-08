package com.smartcampus.reportinganalytics.model;

import java.util.Map;

public class LibraryTrends {
    private Map<String, Integer> flowTrends; 
    private String recommendedQuietTime;

    public LibraryTrends() {}

    public LibraryTrends(Map<String, Integer> flowTrends, String recommendedQuietTime) {
        this.flowTrends = flowTrends;
        this.recommendedQuietTime = recommendedQuietTime;
    }

    // Getters and Setters
    public Map<String, Integer> getFlowTrends() { return flowTrends; }
    public void setFlowTrends(Map<String, Integer> flowTrends) { this.flowTrends = flowTrends; }

    public String getRecommendedQuietTime() { return recommendedQuietTime; }
    public void setRecommendedQuietTime(String recommendedQuietTime) { this.recommendedQuietTime = recommendedQuietTime; }
}
