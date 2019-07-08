package dev.andreishilov.ade.core.utils;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkDownUtils {

    private static final Parser PARSER = Parser.builder().build();
    private static final HtmlRenderer HTML_RENDERER = HtmlRenderer.builder().build();

    private MarkDownUtils() {
        throw new IllegalStateException();
    }


    public static String markdownToHtml(final String markdown) {
        Node node = PARSER.parse(markdown);

        return HTML_RENDERER.render(node);
    }
}
