

package test;

import java.util.Date;

//1. Message - Immutable class
public class Message {
 public final byte[] data;
 public final String asText;
 public final double asDouble;
 public final Date date;

 public Message(byte[] data) {
     this.data = data;
     this.asText = new String(data);
     this.asDouble = toDouble(asText);
     this.date = new Date();
 }

 public Message(String text) {
     this(text.getBytes());
 }

 public Message(double value) {
     this(Double.toString(value));
 }

 private double toDouble(String s) {
     try {
         return Double.parseDouble(s);
     } catch (NumberFormatException e) {
         return Double.NaN;
     }
 }
}
