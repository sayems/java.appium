package org.sayem.appium.webservice;

public class EndpointBuilder {
    public static String uri(String host, String root, String relativePath) {
        String absolutePath = buildAbsolutePath(root, relativePath);
        return buildUri(host, absolutePath);
    }

    private static String buildUri(String host, String absolutePath) {
        while (host.endsWith("/") && host.length() > 1) {
            host = host.substring(0, host.length() - 1);
        }
        return host + absolutePath;
    }

    private static String buildAbsolutePath(String root, String relativePath) {
        String separator = root.endsWith("/") ? "" : "/";
        return root + separator + relativePath;
    }

}
