package cn.roy.logcanary;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    public void send() {
        String x = "xsdfafdafas";
        byte[] bytes = x.getBytes(StandardCharsets.UTF_8);
        int length = bytes.length;
        byte[] headBytes = intToBytes2(length);
        // TODO: 2022/7/28 发送

    }

    public void read() {
        byte[] buf = new byte[1024];
        byte[] data = null;
        int targetLength = -1;
        int currentIndex = 0;
        int length = -1;
        byte[] temp = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(""));
            while ((length = fileInputStream.read(buf)) != -1) {
                // 解析数据长度
                if (targetLength == -1) {
                    if (temp != null) {

                    }
                    if (length >= 4) {
                        byte[] h = new byte[4];
                        System.arraycopy(buf, 0, h, 0, 4);
                        targetLength = bytesToInt2(h);
                    } else {
                        temp = buf;
                        continue;
                    }
                }

                // 读取数据
                data = new byte[targetLength];
                if (length > targetLength) {
                    System.arraycopy(buf, 0, data, currentIndex, targetLength);
                } else {
                    System.arraycopy(buf, 0, data, currentIndex, length);
                    currentIndex = currentIndex + length;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    public static int bytesToInt2(byte[] src) {
        int value = ((src[0] & 0xFF) << 24)
                | ((src[1] & 0xFF) << 16)
                | ((src[2] & 0xFF) << 8)
                | (src[3] & 0xFF);
        return value;
    }

}