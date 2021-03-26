package org.tlauncher.util;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import javax.annotation.Nullable;

public final class JSONUtils {
  private JSONUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
  
  @Nullable
  public static <T> T fromJson(Gson gsonIn, String json, Class<T> adapter) {
    return fromJson(gsonIn, json, adapter, false);
  }
  
  @Nullable
  public static <T> T fromJson(Gson gsonIn, String json, Class<T> adapter, boolean lenient) {
    return fromJson(gsonIn, new StringReader(json), adapter, lenient);
  }
  
  @Nullable
  public static <T> T fromJson(Gson gsonIn, Reader readerIn, Class<T> adapter, boolean lenient) {
    try {
      JsonReader jsonreader = new JsonReader(readerIn);
      jsonreader.setLenient(lenient);
      return (T)gsonIn.getAdapter(adapter).read(jsonreader);
    } catch (IOException ioexception) {
      throw new JsonParseException(ioexception);
    } 
  }
}
