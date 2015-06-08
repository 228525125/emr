package com.mhsoft.emr.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

public class PdfUtil {
	
	public static final String FILETYPE_JPG = "jpg";
	public static final String SUFF_IMAGE = "." + FILETYPE_JPG;

	/**
	 * 将指定pdf文件的首页转换为指定路径的缩略图
	 *@param filepath 原文件路径，例如d:/test.pdf
	 *@param imagepath 图片生成路径，例如 d:/test-1.jpg
	 *@param zoom     缩略图显示倍数，1表示不缩放，0.3则缩小到30%
	 */
	public static void pdfConvertJPEG(String filepath, String imagepath, float zoom)
			throws PDFException, PDFSecurityException, IOException {
		// ICEpdf document class
		Document document = null;

		float rotation = 0f;

		document = new Document();
		document.setFile(filepath);
		// maxPages = document.getPageTree().getNumberOfPages();

		BufferedImage img = (BufferedImage) document.getPageImage(0,
				GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation,
				zoom);

		Iterator iter = ImageIO.getImageWritersBySuffix(FILETYPE_JPG);
		ImageWriter writer = (ImageWriter) iter.next();
		File outFile = new File(imagepath);
		if(!outFile.exists())
			outFile.createNewFile();
		FileOutputStream out = new FileOutputStream(outFile);
		ImageOutputStream outImage = ImageIO.createImageOutputStream(out);
		writer.setOutput(outImage);
		writer.write(new IIOImage(img, null, null));
	}

	public static void main(final String[] args) {
		try {
			pdfConvertJPEG("c:/temp/java.pdf","c:/temp/java-1.jpg",1);
		} catch (PDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PDFSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
