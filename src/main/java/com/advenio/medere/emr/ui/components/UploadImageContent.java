package com.advenio.medere.emr.ui.components;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Scope;

import com.advenio.medere.server.session.ISessionManager;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

@SpringComponent("UploadImageContent")
@Scope("prototype")
public class UploadImageContent extends Div {
	private static final long serialVersionUID = -8154331677035569405L;

	private ISessionManager sessionManager;
	private byte[] bytes;
	private Upload upload;
	private Div output;
	private String maxWidthImage;
	private String maxHeightImage;
	private String name;

	public UploadImageContent(String name,byte[] loadBytes,ISessionManager sessionManager) {
		super();
		this.sessionManager=sessionManager;
		bytes = loadBytes;
		this.name = name;
		createUpdate();
	}

	
	public UploadImageContent(String name,byte[] loadBytes,ISessionManager sessionManager,String maxWidthImage, String maxHeightImage) {
		super();
		this.sessionManager=sessionManager;
		bytes = loadBytes;
		this.maxWidthImage = maxWidthImage;
		this.maxHeightImage = maxHeightImage;
		this.name = name;
		createUpdate();
	}

	private void createUpdate() {
		output = new Div();
		output.setId("output"+name);
		upload= createUpload(output);

		// Si ya cargo anteriormente la imagen muestra
		loadImage();

		getStyle().set("margin-right","var(--lumo-space-s)");
		add(upload, output);
		H4 title = new H4(name);
		title.getStyle().set("margin-top","0.5em");
		addComponentAsFirst(title);
	}

	public void setEnabledUpload(boolean enabled) {
		upload.setMaxFiles(enabled?1:0);
		clearUpload();
	}

	private Upload createUpload(Div output) {
		MemoryBuffer buffer = new MemoryBuffer();

		Upload upload = new Upload(buffer);
		upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif", "image/bmp");
		upload.getElement().setAttribute("capture", "environment");
		upload.setI18n(createI18n());
		upload.setMaxFileSize(31457280);

		upload.addSucceededListener(event -> {
			Component component = createComponent(event.getMIMEType(),
					event.getFileName(), buffer.getInputStream());
			showOutput("", component, output);
			try {
				bytes=IOUtils.toByteArray(buffer.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		upload.addFileRejectedListener(event -> {
			Paragraph component = new Paragraph();
			showOutput(event.getErrorMessage(), component, output);
		});

		upload.getElement().addEventListener("file-remove", event -> {
			clearUpload();
		}).addEventData("event.detail.file.name");

		return upload;
	}
	
	private UploadI18N createI18n() {
		UploadI18N i18n = new UploadI18N();
		i18n.setDropFiles(new UploadI18N.DropFiles()
				.setOne(sessionManager.getI18nMessage("DropImageHere"))
				.setMany(sessionManager.getI18nMessage("DropImageHere")))
		.setAddFiles(new UploadI18N.AddFiles()
				.setOne(sessionManager.getI18nMessage("UploadImage"))
				.setMany(sessionManager.getI18nMessage("UploadImage")))
		.setError(new UploadI18N.Error()
				.setFileIsTooBig(sessionManager.getI18nMessage("TheFileIsTooBig"))
				.setIncorrectFileType(sessionManager.getI18nMessage("InvalidFileType")))
		.setUploading(new UploadI18N.Uploading()
				.setStatus(new UploadI18N.Uploading.Status()
						.setConnecting(sessionManager.getI18nMessage("Connecting"))
						.setStalled(sessionManager.getI18nMessage("DownloadStalled"))
						.setProcessing(sessionManager.getI18nMessage("Processing")))
				.setRemainingTime(new UploadI18N.Uploading.RemainingTime()
						.setPrefix(sessionManager.getI18nMessage("TimeRemaining"))
						.setUnknown(sessionManager.getI18nMessage("RemainingTimeUnknown")))
				.setError(new UploadI18N.Uploading.Error()
						.setServerUnavailable(sessionManager.getI18nMessage("ServerNotAvailable"))
						.setUnexpectedServerError(sessionManager.getI18nMessage("Error"))
						.setForbidden(sessionManager.getI18nMessage("DownloadForbidden"))));
		i18n.setUnits(new UploadI18N.Units()
				.setSize(Arrays.asList("B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")));
		return i18n;
	}

	private Component createComponent(String mimeType, String fileName, InputStream stream) {
		if (mimeType.startsWith("image")) {
			Image image = new Image();
			try {
				byte[] bytes = IOUtils.toByteArray(stream);
				image.getElement().setAttribute("src", new StreamResource(
						fileName, () -> new ByteArrayInputStream(bytes)));
				try (ImageInputStream in = ImageIO.createImageInputStream(
						new ByteArrayInputStream(bytes))) {
					final Iterator<ImageReader> readers = ImageIO
							.getImageReaders(in);
					if (readers.hasNext()) {
						ImageReader reader = readers.next();
						try {
							reader.setInput(in);
							image.setWidth(reader.getWidth(0) + "px");
							image.setHeight(reader.getHeight(0) + "px");
						} finally {
							reader.dispose();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			image.setMaxHeight(maxHeightImage!=null?maxHeightImage:"100px");
			image.setMaxWidth(maxWidthImage!=null?maxWidthImage:"150px");
			return image;
		}
		Div content = new Div();
		String text = String.format("Mime type: '%s'\nSHA-256 hash: '%s'",
				mimeType, MessageDigestUtil.sha256(stream.toString()));
		content.setText(text);
		return content;
	}

	private void showOutput(String text, Component content,
			HasComponents outputContainer) {
		outputContainer.removeAll();
		HtmlComponent p = new HtmlComponent(Tag.P);
		p.getElement().setText(text);
		outputContainer.add(p);
		outputContainer.add(content);
	}

	private void showSavedImage(byte[] bytes, String name, Div output, Upload upload) {
		Image image = new Image();
		image.getElement().setAttribute("src", new StreamResource(
				name, () -> new ByteArrayInputStream(bytes)));
		image.setMaxHeight(maxHeightImage!=null?maxHeightImage:"100px");
		image.setMaxWidth(maxWidthImage!=null?maxWidthImage:"150px");

		Div content = new Div();
		content.add(image);

		JsonArray jsonArray = Json.createArray();
		JsonObject jsonObject = Json.createObject();
		jsonObject.put("name", name);
		jsonObject.put("progress", 100);
		jsonObject.put("complete", true);
		jsonArray.set(0, jsonObject);
		upload.getElement().setPropertyJson("files", jsonArray);
		showOutput("",content, output);
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public void clearUpload() {
		output.removeAll();
		upload.getElement().setPropertyJson("files", Json.createArray());
		bytes=null;
	}

	public boolean isValid() {
		if(bytes==null ||bytes.length==0 ) {
			return false;
		}
		return true;
	}

	public void loadImage() {
		if(bytes!=null) {
			showSavedImage(bytes,sessionManager.getI18nMessage("Image")+name,output,upload);
		}
	}
}