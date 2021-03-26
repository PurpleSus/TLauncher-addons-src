package org.tlauncher.util;

public final class Utils {
  private Utils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
  
  public static <T> T tryCatchSilently(ThrSupplier<T> supplier, ThrSupplier<T> supplierOnException) {
    try {
      return supplier.get();
    } catch (Exception e) {
      try {
        return supplierOnException.get();
      } catch (Exception exception) {
        exception.printStackTrace();
        return null;
      } 
    } 
  }
  
  public static interface ThrSupplier<T> {
    T get() throws Exception;
  }
}
