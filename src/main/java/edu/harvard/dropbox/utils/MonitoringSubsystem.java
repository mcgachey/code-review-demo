package edu.harvard.dropbox.utils;

public interface MonitoringSubsystem {

  void raiseWarning(String string, Exception e);
  void raiseError(String string, Exception e);

}
