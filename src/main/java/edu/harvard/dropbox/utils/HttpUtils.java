package edu.harvard.dropbox.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;

public class HttpUtils {

  private MonitoringSubsystem monitor;

  public HttpUtils(MonitoringSubsystem monitor) {
    this.monitor = monitor;
  }
  
  public void downloadFile(final String url, final String file) {
    System.out.println("Downloading " + url + " to " + file);

    DataInputStream in = null; // Data coming in from the network.
    DataOutputStream out = null; // Data going out on the network.
    FileOutputStream fOut = null; // Data being written to a file.
    try {
      // Parse the URL, and use the default HTTP port if none is specified
      URL u = new URL(url);
      int port = u.getPort() == -1 ? 80 : u.getPort();
  
      // Connect to the server, and get the input and output streams
      Socket s = new Socket(u.getHost(), port);
      out = new DataOutputStream(s.getOutputStream());
      in = new DataInputStream(s.getInputStream());
  
      // Open the file where the output will be saved
      File f = new File(file);
      fOut = new FileOutputStream(f);
  
      // Make the GET request
      out.write(("GET " + url + " HTTP/1.0\n\n").getBytes());
  
      // Strip off the HTTP header block by reading until the first empty line (see the HTTP protocol RFC for details of the format)
      String header = in.readLine();
      while (header.trim().length() > 0) {
        // TODO: Parse headers to find the length of the content
        // TODO: Check for HTTP error return codes
        header = in.readLine();
      }
      
      // Read the response and send it to the output file
      boolean data = true;
      while (data) {
        byte[] buffer = new byte[50];
        int length = in.read(buffer);
        if (length < 1) {
          data = false;
        }
        fOut.write(buffer);
      }
  
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // Close all the streams regardless of whether there was an exception
      try {
        out.close();
        in.close();
        fOut.close();      
      } catch(IOException e2) {
        // Something really bad's happened here; let's page the sysadmin.
        monitor.raiseError("Failed to download URL " + url + " to file " + file, e2);
      }
    }
  }
}
