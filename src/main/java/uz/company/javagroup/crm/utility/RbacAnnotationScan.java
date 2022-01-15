package uz.isd.javagroup.grandcrm.utility;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.springframework.security.access.prepost.PreAuthorize;
import uz.isd.javagroup.grandcrm.entity.directories.Permission;
import uz.isd.javagroup.grandcrm.services.directories.PermissionService;

import java.lang.reflect.Method;
import java.util.Set;

@Slf4j
public class RbacAnnotationScan {
    public void start(PermissionService permissionService) {
        log.info("---------------------------------------");
        log.info("----Scan for Permission annotations----");
        log.info("---------------------------------------");
        Reflections reflections = new Reflections("uz.isd.javagroup.grandcrm.controller", new MethodAnnotationsScanner());
        Set<Method> methods = reflections.getMethodsAnnotatedWith(PreAuthorize.class);
        methods.forEach(method -> {
            PreAuthorize perm = method.getAnnotation(PreAuthorize.class);
            String name = perm.value();
            if (name.contains("hasAuthority")) {
                String code = name.replace("hasAuthority('", "").replace("')", "");
                try {
                    String methodType = method.getDeclaringClass().getSimpleName();
                    String type = null;
                    if (methodType.contains("Controller"))
                        type = methodType.replace("Controller", "") + " management";
                    if (type != null) {
                        Permission permissionOpt = permissionService.getPermissionByNameAndType(code, type);
                        if (permissionOpt == null) {
                            Permission permission = new Permission();
                            permission.setName(code);
                            permission.setType(type);
                            permissionService.savePermission(permission);
                            log.info("Found a permission '" + code + "'");
                        }
                    }
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    public void startView(PermissionService permissionService) {
        log.info("---------------------------------------");
        log.info("-------Scan for MENU PERMISSIONS-------");
        log.info("---------------------------------------");
        Reflections reflections = new Reflections("uz.isd.javagroup.grandcrm.viewController", new MethodAnnotationsScanner());
        Set<Method> methods = reflections.getMethodsAnnotatedWith(PreAuthorize.class);
        methods.forEach(method -> {
            PreAuthorize perm = method.getAnnotation(PreAuthorize.class);
            String name = perm.value();
            if (name.contains("hasAuthority")) {
                String code = name.replace("hasAuthority('", "").replace("')", "");
                String[] codes = code.split(",");
                try {
                    String methodType = method.getDeclaringClass().getSimpleName();
                    String type = null;
                    if (methodType.contains("Controller"))
                        type = methodType.replace("Controller", "") + " management";
                    if (type != null) {
                        Permission parentDB = permissionService.getPermissionByNameAndType(codes[0].trim(), type);
                        Permission parent = null;
                        if (parentDB == null) {
                            parent = new Permission();
                            parent.setName(codes[0].trim());
                            parent.setType(type);
                            permissionService.savePermission(parent);
                            log.info("Found a permission '" + codes[0].trim() + "'");
                        }
                        for (int i = 1; i < codes.length; i++) {
                            Permission permissionOpt = permissionService.getPermissionByNameAndType(codes[i].trim(), type);
                            if (permissionOpt == null) {
                                Permission permission = new Permission();
                                permission.setName(codes[i].trim());
                                permission.setType(type);
                                if (parent != null) permission.setParentId(parent.getId());
                                permissionService.savePermission(permission);
                                log.info("Found a permission '" + codes[i].trim() + "'");
                            }
                        }
                    }
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }
}
