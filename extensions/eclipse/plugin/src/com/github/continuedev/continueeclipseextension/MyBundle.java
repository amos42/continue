package com.github.continuedev.continueeclipseextension;

import org.eclipse.osgi.util.NLS;

public class MyBundle extends NLS {
    private static final String BUNDLE_NAME = "messages.MyBundle"; // messages.properties 파일 이름
    public static String someMessageKey; // 메시지 키를 정적 필드로 정의 해야 합니다.

    static {
        // 클래스 로딩 시점에 초기화
        NLS.initializeMessages(BUNDLE_NAME, MyBundle.class);
    }

    private MyBundle() {
        // 생성자를 private으로 정의하여 인스턴스화 방지
    }

    public static String message(String key, Object... params) {
        // 메시지와 파라미터를 포맷팅합니다.
        return String.format(NLS.bind(getMessage(key), params));
    }
}
