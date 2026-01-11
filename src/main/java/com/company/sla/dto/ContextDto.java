package com.company.sla.dto;

import java.util.List;

public class ContextDto {

    public static class ContextClassInfo {
        private String className;
        private String displayName;
        private List<ContextFieldInfo> fields;
        
        public ContextClassInfo() {}
        public ContextClassInfo(String className, String displayName, List<ContextFieldInfo> fields) {
            this.className = className;
            this.displayName = displayName;
            this.fields = fields;
        }
        
        public String getClassName() { return className; }
        public void setClassName(String className) { this.className = className; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public List<ContextFieldInfo> getFields() { return fields; }
        public void setFields(List<ContextFieldInfo> fields) { this.fields = fields; }
        
        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private String className;
            private String displayName;
            private List<ContextFieldInfo> fields;
            public Builder className(String className) { this.className = className; return this; }
            public Builder displayName(String displayName) { this.displayName = displayName; return this; }
            public Builder fields(List<ContextFieldInfo> fields) { this.fields = fields; return this; }
            public ContextClassInfo build() { return new ContextClassInfo(className, displayName, fields); }
        }
    }

    public static class ContextFieldInfo {
        private String fieldName;
        private String fieldType;
        private Double metricWeight;
        
        public ContextFieldInfo() {}
        public ContextFieldInfo(String fieldName, String fieldType, Double metricWeight) {
            this.fieldName = fieldName;
            this.fieldType = fieldType;
            this.metricWeight = metricWeight;
        }

        public String getFieldName() { return fieldName; }
        public void setFieldName(String fieldName) { this.fieldName = fieldName; }
        public String getFieldType() { return fieldType; }
        public void setFieldType(String fieldType) { this.fieldType = fieldType; }
        public Double getMetricWeight() { return metricWeight; }
        public void setMetricWeight(Double metricWeight) { this.metricWeight = metricWeight; }
        
        public static Builder builder() { return new Builder(); }
        
        public static class Builder {
            private String fieldName;
            private String fieldType;
            private Double metricWeight;

            public Builder fieldName(String fieldName) { this.fieldName = fieldName; return this; }
            public Builder fieldType(String fieldType) { this.fieldType = fieldType; return this; }
            public Builder metricWeight(Double metricWeight) { this.metricWeight = metricWeight; return this; }
            
            public ContextFieldInfo build() {
                ContextFieldInfo info = new ContextFieldInfo();
                info.setFieldName(fieldName);
                info.setFieldType(fieldType);
                info.setMetricWeight(metricWeight);
                return info;
            }
        }
    }
}
