package cn.roy.logcanary;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

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
        int length = -1;
        byte[] tempBuf = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(""));
            while ((length = fileInputStream.read(buf)) != -1) {
                // 解析数据长度
                byte[] merge;
                if (tempBuf == null) {
                    merge = merge(tempBuf, buf, length);
                } else {
                    merge = new byte[length];
                    System.arraycopy(buf, 0, merge, 0, length);
                }
                if (merge.length < 4) {
                    tempBuf = merge;
                    continue;
                }
                process(merge, tempBuf);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process(byte[] buffer, byte[] temp) {
        if (buffer.length < 4) {
            temp = buffer;
            return;
        }
        byte[] sizeBuf = new byte[4];
        System.arraycopy(buffer, 0, sizeBuf, 0, 4);
        int dataLength = bytesToInt2(sizeBuf);
        if (buffer.length > dataLength) {
            String str = new String(buffer, 0, dataLength, Charset.forName("UTF-8"));
            // TODO: 2022/8/1 分发数据
            byte[] data = new byte[buffer.length - dataLength];
            System.arraycopy(buffer, dataLength, data, 0, data.length);
            process(data, temp);
        } else {
            System.arraycopy(buffer, 0, temp, 0, buffer.length);
        }
    }

    private static byte[] merge(byte[] temp, byte[] buf, int bufLength) {
        byte[] result = new byte[temp.length + buf.length];
        System.arraycopy(temp, 0, result, 0, temp.length);
        System.arraycopy(buf, 0, result, temp.length, bufLength);
        return result;
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

    @Test
    public void testArray(){
        List<String> list = new ArrayList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        new Thread(new Runnable() {
            @Override
            public void run() {
                list.subList(0,3).clear();
                System.out.printf("剩余："+list);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                list.clear();
            }
        }).start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("测试结束");
    }

}