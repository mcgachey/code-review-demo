package edu.harvard.dropbox.controllers;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.harvard.dropbox.models.DownloadSpecModel;
import edu.harvard.dropbox.utils.HttpUtils;

@Controller
@RequestMapping("/")
public class DownloadController {
  
  static final String FILE_STORE_BASE = "/tmp/";
  private HttpUtils http;

  public DownloadController(HttpUtils http) {
    this.http = http;
  }
  
  @RequestMapping(value = "/download-form", method = RequestMethod.GET)
  public ModelAndView downloadForm() {
    return new ModelAndView("download-form", "command", new DownloadSpecModel());
  }

  @RequestMapping(value = "/execute-download", method = RequestMethod.POST)
  public String executeDownload(@ModelAttribute("SpringWeb") DownloadSpecModel spec,
      ModelMap model) {
    
    // Get the file type from the URL
    String extension = spec.getUrl().substring(spec.getUrl().lastIndexOf("."));

    // Download to a uniquely-named file
    String fileId = UUID.randomUUID().toString() + extension;
    http.downloadFile(spec.getUrl(), FILE_STORE_BASE + fileId);
    
    model.addAttribute("title", spec.getTitle());
    return "confirm-download";
  }

}
