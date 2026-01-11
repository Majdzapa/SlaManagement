package com.company.sla.service;

import com.company.sla.annotation.SlaConfig;
import com.company.sla.annotation.SlaResult;
import com.company.sla.dto.ContextDto.ContextClassInfo;
import com.company.sla.dto.ContextDto.ContextFieldInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class SlaContextService {
    
    private static final Logger log = LoggerFactory.getLogger(SlaContextService.class);

    @PersistenceContext
    private EntityManager entityManager;

    public List<ContextClassInfo> getAvailableContexts() {
        List<ContextClassInfo> contexts = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(SlaConfig.class));

        // Scan the base package
        String basePackage = "com.company.sla.model"; 

        for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
            try {
                Class<?> clazz = Class.forName(bd.getBeanClassName());
                contexts.add(extractContextInfo(clazz));
            } catch (ClassNotFoundException e) {
                log.error("Failed to load class: {}", bd.getBeanClassName(), e);
            }
        }
        return contexts;
    }

    public List<ContextClassInfo> getAvailableResultTypes() {
        List<ContextClassInfo> results = new ArrayList<>();
        
        // Add default primitive types
        results.add(ContextClassInfo.builder().className("BOOLEAN").displayName("Boolean").fields(new ArrayList<>()).build());
        results.add(ContextClassInfo.builder().className("STRING").displayName("String").fields(new ArrayList<>()).build());
        results.add(ContextClassInfo.builder().className("INTEGER").displayName("Integer").fields(new ArrayList<>()).build());

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(SlaResult.class));

        // Scan the base package
        String basePackage = "com.company.sla.model"; 

        for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
            try {
                Class<?> clazz = Class.forName(bd.getBeanClassName());
                results.add(extractResultInfo(clazz));
            } catch (ClassNotFoundException e) {
                log.error("Failed to load result class: {}", bd.getBeanClassName(), e);
            }
        }
        return results;
    }

    public List<Object> getResultInstances(String className) {
        // Basic security check: ensure className is a known SlaResult or safe
        // For prototype, we'll try to load the class and check annotation
        try {
            // We need full package name. This is a limitation. 
            // We can search in known packages or assume we store full class name in UI but previous steps used SimpleName.
            // Let's scan to find the full class name first or loop through available types.
             String fullClassName = findFullClassName(className);
             
             if (fullClassName == null) return new ArrayList<>();

             // Simply query all
             return entityManager.createQuery("SELECT e FROM " + className + " e").getResultList();
        } catch (Exception e) {
            log.error("Error fetching instances for {}", className, e);
            return new ArrayList<>();
        }
    }

    private String findFullClassName(String simpleName) {
         ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
         scanner.addIncludeFilter(new AnnotationTypeFilter(SlaResult.class));
         for (BeanDefinition bd : scanner.findCandidateComponents("com.company.sla.model")) {
             if (bd.getBeanClassName().endsWith("." + simpleName)) {
                 return bd.getBeanClassName();
             }
         }
         return null;
    }

    private ContextClassInfo extractContextInfo(Class<?> clazz) {
        SlaConfig annotation = clazz.getAnnotation(SlaConfig.class);
        String displayName = (annotation != null && !annotation.displayName().isEmpty()) 
                ? annotation.displayName() 
                : clazz.getSimpleName();

        List<ContextFieldInfo> fields = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
             if(!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                 // Weight calculation: (index + 1) * 0.1, formatted roughly
                 double weight = (i + 1) * 0.1; 
                 fields.add(ContextFieldInfo.builder()
                         .fieldName(field.getName())
                         .fieldType(field.getType().getSimpleName())
                         .metricWeight(weight)
                         .build());
             }
        }

        return ContextClassInfo.builder()
                .className(clazz.getSimpleName()) // Or full name if needed. Using simple name for readability in UI.
                .displayName(displayName)
                .fields(fields)
                .build();
    }
    
    private ContextClassInfo extractResultInfo(Class<?> clazz) {
        SlaResult annotation = clazz.getAnnotation(SlaResult.class);
        String displayName = (annotation != null && !annotation.displayName().isEmpty()) 
                ? annotation.displayName() 
                : clazz.getSimpleName();

        List<ContextFieldInfo> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
             if(!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                 fields.add(ContextFieldInfo.builder()
                         .fieldName(field.getName())
                         .fieldType(field.getType().getSimpleName())
                         .build());
             }
        }

        return ContextClassInfo.builder()
                .className(clazz.getSimpleName())
                .displayName(displayName)
                .fields(fields)
                .build();
    }
}
