package org.ci4j.web.upload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.ci4j.util.LogUtils;

import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class MultipartRequest extends HttpServletRequestWrapper {
	private static final Logger logger = LogUtils
			.getLogger(MultipartRequest.class.getName());

	private static String saveDirectory;
	private static int maxPostSize;
	private static String encoding;

	private static final DefaultFileRenamePolicy fileRenamePolicy = new DefaultFileRenamePolicy();
	private com.oreilly.servlet.MultipartRequest multipartRequest;

	private List<UploadFile> uploadFiles;

	public static void init(String saveDirectory, int maxPostSize, String encoding) {
		MultipartRequest.saveDirectory = saveDirectory;
		MultipartRequest.maxPostSize = maxPostSize;
		MultipartRequest.encoding = encoding;
	}

	public MultipartRequest(HttpServletRequest request, String saveDirectory,
			int maxPostSize, String encoding) {
		super(request);
		wrapMultipartRequest(request, saveDirectory, maxPostSize, encoding);
	}

	public MultipartRequest(HttpServletRequest request, String saveDirectory,
			int maxPostSize) {
		super(request);
		wrapMultipartRequest(request, saveDirectory, maxPostSize, encoding);
	}

	public MultipartRequest(HttpServletRequest request, String saveDirectory) {
		super(request);
		wrapMultipartRequest(request, saveDirectory, maxPostSize, encoding);
	}

	public MultipartRequest(HttpServletRequest request) {
		super(request);
		wrapMultipartRequest(request, saveDirectory, maxPostSize, encoding);
	}

	/**
	 * 添加对相对路径的支持 1: 以 "/" 开头或者以 "x:开头的目录被认为是绝对路径 2: 其它路径被认为是相对路径, 需要
	 * JFinalConfig.uploadedFileSaveDirectory 结合
	 */
	private String handleSaveDirectory(String saveDirectory) {
		if (saveDirectory.startsWith("/") || saveDirectory.indexOf(":") == 1)
			return saveDirectory;
		else
			return MultipartRequest.saveDirectory + saveDirectory;
	}

	private void wrapMultipartRequest(HttpServletRequest request,
			String saveDirectory, int maxPostSize, String encoding) {

		saveDirectory = handleSaveDirectory(saveDirectory);

		File dir = new File(saveDirectory);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new RuntimeException("Directory " + saveDirectory
						+ " not exists and can not create directory.");
			}
		}

		uploadFiles = new ArrayList<UploadFile>();

		try {
			multipartRequest = new com.oreilly.servlet.MultipartRequest(
					request, saveDirectory, maxPostSize, encoding,
					fileRenamePolicy);
			Enumeration files = multipartRequest.getFileNames();
			while (files.hasMoreElements()) {
				String name = (String) files.nextElement();
				String filesystemName = multipartRequest
						.getFilesystemName(name);
				LogUtils.info(logger, filesystemName);

				// 文件没有上传则不生成 UploadFile, 这与 cos的解决方案不一样
				if (filesystemName != null) {
					String originalFileName = multipartRequest
							.getOriginalFileName(name);
					String contentType = multipartRequest.getContentType(name);
					UploadFile uploadFile = new UploadFile(name, saveDirectory,
							filesystemName, originalFileName, contentType);
					if (isSafeFile(uploadFile))
						uploadFiles.add(uploadFile);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isSafeFile(UploadFile uploadFile) {
		if (uploadFile.getFileName().toLowerCase().endsWith(".jsp")) {
			uploadFile.getFile().delete();
			return false;
		}
		return true;
	}

	public List<UploadFile> getFiles() {
		return uploadFiles;
	}
}
