package com.projects.model.util;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class EncryptorTest {

    private Object[][] sha256Params() {
        return new Object[][]{{"simplePass", "6369627c677030fe03401feb39da39414cf5c7ee344b94ae30dd303fed8cb7c6"},
                {"testPass", "af0426e71dd57c0fdf93f23f6f191a4aa0578ad7d71897e936746028b8ffd31d"},
                {"examplePass", "caf900ec901a14f3f8bc734f7b1b257c33dcae8aaec7b50b12a77f06c6078985"}};
    }

    private Object[][] md5Params() {
        return new Object[][]{{"somePass", "c78036d23335477b6b339e0b938d74a6"},
                {"easyPass", "43b4a16b3a19d914a83d081f5eb49012"},
                {"strongPass", "50eacbdd8a9604709e7831c99302636d"}};
    }

    private Object[][] sha1Params() {
        return new Object[][]{{"sha1Pass", "4cf5a807eb58f69a4dea6f18e89481d149a23aaa"},
                {"shortPass", "2f133409ddd4a9a3a072cba0cc7b6889cdbfdfce"},
                {"longPass", "84c2d6067fcd127b06f526fd2ddd0d451ba42c41"}};
    }

    @Test
    @Parameters(method = "sha256Params")
    public void encryptSHA256(String text, String expected) {
        assertEquals(expected, Encryptor.encrypt(text, Encryptor.SHA256));
    }

    @Test
    @Parameters(method = "md5Params")
    public void encryptMD5(String text, String expected) {
        assertEquals(expected, Encryptor.encrypt(text, Encryptor.MD5));
    }

    @Test
    @Parameters(method = "sha1Params")
    public void encryptSHA1(String text, String expected) {
        assertEquals(expected, Encryptor.encrypt(text, Encryptor.SHA1));
    }

    @Test
    public void encryptNull() {
        assertNull(Encryptor.encrypt(null, Encryptor.SHA256));
        assertNull(Encryptor.encrypt(null, Encryptor.MD5));
        assertNull(Encryptor.encrypt(null, Encryptor.SHA1));
    }

    @Test
    public void encryptEmptyText() {
        assertNull(Encryptor.encrypt("", Encryptor.SHA256));
        assertNull(Encryptor.encrypt("", Encryptor.MD5));
        assertNull(Encryptor.encrypt("", Encryptor.SHA1));
    }
}
