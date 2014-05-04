package edu.harvard.dropbox.controllers;

import org.easymock.Capture;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import edu.harvard.dropbox.models.DownloadSpecModel;
import edu.harvard.dropbox.utils.HttpUtils;

public class DownloadControllerTests {

  @Test
  // Download form method should return download spec object
  public void downloadFormSpec() {
    HttpUtils utilsMock = createMock(HttpUtils.class);
    DownloadController controller = new DownloadController(utilsMock);
    ModelAndView downloadForm = controller.downloadForm();
    Object model = downloadForm.getModel().get("command");
    assertNotNull("Download form did not return command model", model);
    assertTrue("Command model was not a DownloadSpecModel", model instanceof DownloadSpecModel);
  }
  
  @Test
  // Download form method should render the appropriate page
  public void downloadFormPage() {
    HttpUtils utilsMock = createMock(HttpUtils.class);
    DownloadController controller = new DownloadController(utilsMock);
    ModelAndView downloadForm = controller.downloadForm();
    String view = downloadForm.getViewName();
    assertNotNull("Download form did not return a view", view);
    assertEquals("Download form returned incorrect view: " + view, "download-form", view);
  }

  @Test
  // Download execution controller should delegate to utility function
  public void delegateToUtils() {
    HttpUtils utilsMock = createMock(HttpUtils.class);
    utilsMock.downloadFile(anyObject(String.class), anyObject(String.class));
    replay(utilsMock);
    DownloadController controller = new DownloadController(utilsMock);
    DownloadSpecModel spec = new DownloadSpecModel("url.jpg", "title");
    controller.executeDownload(spec, new ModelMap());
    verify(utilsMock);
  }

  private String captureTargetFile() {
    HttpUtils utilsMock = createMock(HttpUtils.class);
    Capture<String> captureFile = new Capture<String>();
    utilsMock.downloadFile(anyObject(String.class), capture(captureFile));
    replay(utilsMock);
    DownloadController controller = new DownloadController(utilsMock);
    DownloadSpecModel spec = new DownloadSpecModel("url.abc", "title");
    controller.executeDownload(spec, new ModelMap());
    verify(utilsMock);
    return captureFile.getValue();
  }
  
  @Test
  // Target file should have the correct extension
  public void targetFileExtension() {
    String file = captureTargetFile();
    assertNotNull("Target file was null", file);
    assertTrue("Target file ended with wrong suffix", file.endsWith(".abc"));
  }

  @Test
  // Target file should be in the correct directory
  public void targetFileDirectory() {
    String file = captureTargetFile();
    assertNotNull("Target file was null", file);
    System.out.println("File: " + file);
    assertTrue("Target file in the wrong directory", file.startsWith(DownloadController.FILE_STORE_BASE));
  }

  @Test
  // Download execution controller should download the correct file
  public void sourceUrl() {
    HttpUtils utilsMock = createMock(HttpUtils.class);
    Capture<String> captureUrl = new Capture<String>();
    utilsMock.downloadFile(capture(captureUrl), anyObject(String.class));
    replay(utilsMock);
    DownloadController controller = new DownloadController(utilsMock);
    DownloadSpecModel spec = new DownloadSpecModel("url.abc", "title");
    controller.executeDownload(spec, new ModelMap());
    verify(utilsMock);
    String url = captureUrl.getValue();
    assertNotNull("Source URL was null", url);
    assertTrue("Source URL didn't match that provided", url == "url.abc");
  }

  @Test
  // Download controller should pass the correct title to the view
  public void downloadCompleteTitle() {
    HttpUtils utilsMock = createMock(HttpUtils.class);
    utilsMock.downloadFile(anyObject(String.class), anyObject(String.class));
    DownloadController controller = new DownloadController(utilsMock);
    ModelMap map = new ModelMap();
    controller.executeDownload(new DownloadSpecModel("url.abc", "the title"), map);
    String title = (String) map.get("title");
    assertNotNull("executeDownload didn't return a title", title);
    assertEquals("executeDownload returned the wrong title", "the title", title);
  }

  @Test
  // Download controller should render the correct view
  public void downloadCompleteView() {
    HttpUtils utilsMock = createMock(HttpUtils.class);
    utilsMock.downloadFile(anyObject(String.class), anyObject(String.class));
    DownloadController controller = new DownloadController(utilsMock);
    String view = controller.executeDownload(new DownloadSpecModel("url.abc", "title"), new ModelMap());
    assertEquals("executeDownload returned the wrong view", "confirm-download", view);
  }

}
