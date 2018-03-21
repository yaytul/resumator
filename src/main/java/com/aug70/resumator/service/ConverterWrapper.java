package com.aug70.resumator.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vladsch.flexmark.IRender;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.docx.converter.internal.DocxRenderer;
import com.vladsch.flexmark.ext.definition.DefinitionExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughSubscriptExtension;
import com.vladsch.flexmark.ext.ins.InsExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.ext.wikilink.WikiLinkExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.pdf.converter.PdfConverterExtension;
import com.vladsch.flexmark.profiles.pegdown.Extensions;
import com.vladsch.flexmark.profiles.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.superscript.SuperscriptExtension;
import com.vladsch.flexmark.util.options.DataHolder;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;

final class ConverterWrapper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConverterWrapper.class);
	
	private ConverterWrapper() {}
	
	private static final DataHolder OPTIONS = new MutableDataSet()
            .set(Parser.EXTENSIONS, Arrays.asList(
                    DefinitionExtension.create(),
                    EmojiExtension.create(),
                    FootnoteExtension.create(),
                    StrikethroughSubscriptExtension.create(),
                    InsExtension.create(),
                    SuperscriptExtension.create(),
                    TablesExtension.create(),
                    TocExtension.create(),
                    SimTocExtension.create(),
                    WikiLinkExtension.create()
            ))
            .set(DocxRenderer.SUPPRESS_HTML, true);
	
	private static final MutableDataHolder PDF_OPTIONS = PegdownOptionsAdapter.flexmarkOptions(
            Extensions.ALL & ~(Extensions.ANCHORLINKS | Extensions.EXTANCHORLINKS_WRAP)
    ).toMutable();

	private static Parser parser() {
		return Parser.builder(OPTIONS).build();
	}
	
	private static DocxRenderer docxRenderer() {
		return DocxRenderer.builder(OPTIONS).build();
	}
	
	private static HtmlRenderer htmlRenderer() {
		return HtmlRenderer.builder().escapeHtml(false).build();
	}

	/**
	 * https://github.com/vsch/flexmark-java/blob/master/flexmark-java-samples/src/com/vladsch/flexmark/samples/DocxConverterCommonMark.java
	 */
	static ByteArrayOutputStream toDocx(Reader reader) {
		
		try {
			Node document = parser().parseReader(reader);
			final WordprocessingMLPackage template = DocxRenderer.getDefaultTemplate();
			docxRenderer().render(document, template);
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
			template.save(os);
			return os;
		} catch (IOException | Docx4JException ex) {
			LOGGER.error("Error while converting to docx", ex);
			throw new RuntimeException(ex);
		}
	}

	static String toXml(Reader reader) {
		try {
			IRender renderer = docxRenderer();  
			Node document = parser().parseReader(reader);
			return renderer.render(document);
		} catch (IOException ex) {
			LOGGER.error("Error while converting to xml", ex);
			throw new RuntimeException(ex);
		}
	}

	static String toHtml(Reader reader) {
		try {
			Node document = parser().parseReader(reader);
			return htmlRenderer().render(document);
		} catch (IOException ex) {
			LOGGER.error("Error while converting to html", ex);
			throw new RuntimeException(ex);
		}
	}

	static ByteArrayOutputStream toPdf(String html) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PdfConverterExtension.exportToPdf(os, html,"", PDF_OPTIONS);
		return os;
	}

}
