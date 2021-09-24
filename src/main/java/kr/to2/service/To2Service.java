package kr.to2.service;

import java.util.ArrayList;

// for 'Service Stub'
public interface To2Service {

  static final int CODE_MIN_LENGTH = 3;

  static final String CODE_REGEX = "[abcdefghjkmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789]{" + CODE_MIN_LENGTH + ",}";

  static final char[] CODES = new char[] {
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n',
    'o','p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L','M',
    'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
    '1', '2', '3', '4', '5', '6', '7', '8', '9'
  };

  default String convertLongToCode(long value) {
    if (value <= 0) {
      throw new IllegalArgumentException("value는 반드시 0보다 커야 합니다.");
    }

    ArrayList<Integer> indices = new ArrayList<>();
    while (value > 0) {
      int index = (int) (value % CODES.length);
      if (index > 0) {
        index--;
      }
      indices.add(0, index);
      value /= CODES.length;
    }
    if (indices.size() < CODE_MIN_LENGTH) {
      for (int i = CODE_MIN_LENGTH - indices.size(); i > 0; i--) {
        indices.add(0, 0);
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int index : indices) {
      sb.append(CODES[index]);
    }
    return sb.toString();
  }

  String shorten(String url);

  String findUrlByCode(String code);

}
