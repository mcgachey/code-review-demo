package edu.harvard.dropbox.models;

public class DownloadSpecModel {
  private String url;
  private String title;
  private String localFile;

  public DownloadSpecModel(String url, String title) {
    this.url = url;
    this.title = title;
  }
  
  public DownloadSpecModel() {
  }
  
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getLocalFile() {
    return localFile;
  }
  public void setLocalFile(String localFile) {
    this.localFile = localFile;
  }  
  
}
