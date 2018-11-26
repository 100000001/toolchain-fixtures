package nl.praegus.fitnesse.junit.testsystemlisteners;

import fitnesse.testsystems.*;
import nl.praegus.fitnesse.junit.testsystemlisteners.util.OutputChunkParser;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlainHtmlListener implements TestSystemListener, Closeable {
    public static StringBuilder output = new StringBuilder();
    private OutputChunkParser parser = new OutputChunkParser();

    @Override
    public void testSystemStarted(TestSystem testSystem) {

    }

    @Override
    public void testOutputChunk(String chunk) {
        chunk = parser.filterCollapsedSections(chunk);
        if(!chunk.isEmpty()) {
            chunk = parser.embedImages(chunk);
        }
        output.append(chunk);
    }

    @Override
    public void testStarted(TestPage testPage) {

        String css = new String(getBytesForResource("plaincss.css"));
        String js = new String(getBytesForResource("javascript.js"));
        output = new StringBuilder();
        output.append("<html>").append("<head>")
                .append("<style>" + css + "</style>")
                .append("</head><body>")
                .append("<script>\r\n" + js + "\r\n</script>")
                .append("<h1>" + testPage.getFullPath() + "</h1>");
    }

    private byte[] getBytesForResource(String resource) {
        byte[] result;
        try {
            Path path = Paths.get(getClass().getClassLoader()
                    .getResource(resource).toURI());
            result = Files.readAllBytes(path);
        } catch (Exception e) {
            e.printStackTrace();
            result = "".getBytes();
        }
        return result;
    }

    @Override
    public void testComplete(TestPage testPage, TestSummary testSummary) {
        output.append("</body></html>");
    }

    @Override
    public void testSystemStopped(TestSystem testSystem, Throwable cause) {

    }

    @Override
    public void testAssertionVerified(Assertion assertion, TestResult testResult) {

    }

    @Override
    public void testExceptionOccurred(Assertion assertion, ExceptionResult exceptionResult) {

    }

    @Override
    public void close() throws IOException {

    }
}
