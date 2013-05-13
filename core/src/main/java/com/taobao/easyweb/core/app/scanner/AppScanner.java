package com.taobao.easyweb.core.app.scanner;

import com.taobao.easyweb.core.app.App;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-25 …œŒÁ11:41
 * <p/>
 * appµƒ“≥√Ê…®√Ë
 */
public class AppScanner {

    static Map<String, Long> modified = new ConcurrentHashMap<String, Long>();
    private static AppScanner instance;
    FileFilter fileFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return (pathname.isDirectory() && !pathname.getName().startsWith(".") && !pathname.getName().equals("target")) || pathname.isFile();
        }
    };
    private Map<String, Long> appLastScan = new HashMap<String, Long>();

    private AppScanner() {
    }

    public static AppScanner getInstance() {
        if (instance == null) {
            instance = new AppScanner();
        }
        return instance;
    }

    public ScanResult scan(App app) {
        ScanResult result = new ScanResult(app);
        File appFile = new File(app.getRootPath());
        list(appFile, result, app, false);
        result.setAppFile(appFile);
        result.end();
        return result;
    }

    private void list(File file, ScanResult result, App app, boolean web) {
        if (file.isDirectory()) {
            if (!web) {
                for (String webPath : app.getWebPaths()) {
                    if (StringUtils.isBlank(webPath)) {
                        continue;
                    }
                    if (file.getAbsolutePath().endsWith(webPath)) {
                        web = true;
                        break;
                    }
                }
            }
            for (File c : file.listFiles(fileFilter)) {
                list(c, result, app, web);
            }
        } else {
            if (file.getName().endsWith(".groovy")) {
                boolean modified = isModified(file);
                String path = file.getAbsolutePath();
                if (web) {
                    result.addWebGroovyFile(path);
                    if (modified) {
                        result.addModifiedWebGroovy(path);
                    }

                } else {
                    result.addBizGroovyFile(path);
                    if (modified) {
                        result.addModifiedBizGroovy(path);
                    }
                }
            } else {
                result.addSuffixFile(file);
            }
        }
    }

    private boolean isModified(File file) {
        String name = file.getAbsolutePath();
        long last = file.lastModified();
        Long l = modified.get(name);
        if (l == null || last != l.longValue()) {
            modified.put(name, last);
            return true;
        }
        return false;
    }


}
