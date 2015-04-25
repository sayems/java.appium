package com.sayem.appium.pagefactory.framework.browser;

import com.google.common.base.Throwables;
import com.sayem.appium.pagefactory.framework.browser.web.RemoteBrowser;
import com.thoughtworks.selenium.Wait;
import com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class BrowserUtil {
    public static final int DEFAULT_TIMEOUT_SECONDS = 30;
    private static final Logger logger = LoggerFactory.getLogger(BrowserUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Helper to wait until the length of an HTML page (as a String) is stable for 1 second.
     * Useful to wait for javascript actions that modify the DOM of the page to complete.
     *
     * @param browser - this will probably only be useful for a {@link com.sayem.appium.pagefactory.framework.browser.web.WebBrowser}
     */
    public static void waitForPageHtmlToBeStable(Browser browser, int timeoutSeconds) {
        final long TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(timeoutSeconds);
        final long START = System.currentTimeMillis();
        WaitForPageToLoad waitForPageToLoad = new WaitForPageToLoad();
        Wait lengthWait = waitForPageToLoad.getLengthCheckingWait(browser.getWebDriver());
        lengthWait.wait("Timeout waiting for the page HTML to be stable with !", TIMEOUT_MILLIS);
        final long END = System.currentTimeMillis();
        logger.info("Success - waited for the page HTML to be stable! Took {} ms", END - START);
    }

    public static void waitForPageHtmlToBeStable(Browser browser) {
        waitForPageHtmlToBeStable(browser, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Helper to determine the Selenium Node we're running on via the Selenium API
     *
     * @param browser - a remote browser that is running in a Selenium Grid
     * @return - the URL for the Node that the test is running on.
     */
    public static Optional<String> getSeleniumNodeUrl(RemoteBrowser browser) {
        RemoteWebDriver remoteWebDriver = (RemoteWebDriver) browser.getWebDriver();
        SessionId sessionId = remoteWebDriver.getSessionId();
        CommandExecutor commandExecutor = remoteWebDriver.getCommandExecutor();
        if (commandExecutor instanceof HttpCommandExecutor) {
            HttpCommandExecutor httpCommandExecutor = (HttpCommandExecutor) commandExecutor;
            URL remoteServer = httpCommandExecutor.getAddressOfRemoteServer();
            return getSeleniumNodeUrl(remoteServer, sessionId.toString());
        }
        return Optional.empty();
    }

    // Helper for above method.
    private static Optional<String> getSeleniumNodeUrl(URL remoteServer, String sessionId) {
        try {
            URI gridApiURI = new URI(remoteServer.getProtocol(), null, remoteServer.getHost(), remoteServer.getPort(),
                    "/grid/api/testsession", "session=" + sessionId, null);
            DefaultHttpClient client = new DefaultHttpClient();
            BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", gridApiURI.toString());
            HttpHost host = new HttpHost(remoteServer.getHost(), remoteServer.getPort());
            HttpResponse response = client.execute(host, request);
            InputStream inputStream = response.getEntity().getContent();
            JsonNode obj = objectMapper.readTree(inputStream);
            String nodeHost = obj.get("proxyId").asText();
            return Optional.ofNullable(nodeHost);
        } catch (Exception e) {
            logger.warn("Error determining Selenium Node URL: {}", e.getMessage());
            logger.debug(Throwables.getStackTraceAsString(e));
            return Optional.empty();
        }
    }
}
