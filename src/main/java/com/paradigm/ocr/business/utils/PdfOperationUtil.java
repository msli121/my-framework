package com.paradigm.ocr.business.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PdfOperationUtil {
    /**
     * 从文件中加载 pdf
     *
     * @param file 文件
     * @return
     * @throws IOException
     */
    public static PDDocument load(File file) throws IOException {
        if (!file.exists() || file.isDirectory()) {
            return null;
        }
        return PDDocument.load(file);
    }

    /**
     * 从文件流中加载 pdf
     *
     * @param inputStream 文件输入流
     * @return
     * @throws IOException
     */
    public static PDDocument load(InputStream inputStream) throws IOException {
        if (inputStream == null || inputStream.available() == 0) {
            return null;
        }
        return PDDocument.load(inputStream);
    }

    /**
     * 获取 pdf 总页数
     *
     * @param pdf
     * @return
     */
    public static int pageCount(PDDocument pdf) {
        return pdf.getNumberOfPages();
    }

    /**
     * 获取 pdf 文档的所有分页对象
     *
     * @param pdf
     * @return 返回的 list 集合
     */
    public static List<PDPage> getPageList(PDDocument pdf) {
        int count = pageCount(pdf);
        List<PDPage> pages = new ArrayList<>(64);
        PDPageTree pdPages = pdf.getPages();
        for (int i = 0; i < count; i++) {
            PDPage pdPage = pdPages.get(i);
            pages.add(pdPage);
        }
        return pages;
    }

    /**
     * 给整个PDF文件分页，形成多个 pdf 单页文件
     *
     * @param inputStream  pdf文件流
     * @param outputParent 输出文件的父目录
     * @throws IOException
     */
    public static Integer pageSpilt(InputStream inputStream, File outputParent) throws IOException {
        if (!outputParent.exists() || !outputParent.isDirectory()) {
            throw new RuntimeException("输出文件的父目录不存在");
        }

        PDDocument pdf = load(inputStream);
        try {
            int numberOfPages = pageCount(pdf);
            for (int i = 0; i < numberOfPages; i++) {
                PDDocument document = new PDDocument();
                document.addPage(pdf.getPage(i));
                document.save(new File(outputParent, i + 1 + ".pdf"));
                close(document);
            }
            return numberOfPages;
        } finally {
            close(pdf);
            close(inputStream);
        }
    }

    /**
     * 图片转PDF
     *
     * @param inputFile  图片路径
     * @param outputFile 生成pdf的文件路径
     * @throws IOException
     */
    public static void convertImgToPDF(String inputFile, String outputFile) throws IOException {
        if (!new File(inputFile).exists()) {
            throw new RuntimeException("输入文件不存在");
        }
        if (!outputFile.toLowerCase().endsWith(".pdf")) {
            throw new RuntimeException("只能转成pdf文件");
        }
        PDDocument document = new PDDocument();
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedImage bimg = ImageIO.read(inputStream);
        float width = bimg.getWidth();
        float height = bimg.getHeight();
        PDPage page = new PDPage(new PDRectangle(width, height));
        document.addPage(page);
        PDImageXObject img = PDImageXObject.createFromFile(inputFile, document);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.drawImage(img, 0, 0, width, height);
        contentStream.close();
        close(inputStream);
        document.save(outputFile);
        close(document);
    }

    public static void close(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(PDDocument pdf) {
        try {
            pdf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
