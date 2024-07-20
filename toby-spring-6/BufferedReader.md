### BufferedReader 간단 구현

- 버퍼(Buffer): 데이터를 임시로 저장하는 메모리 공간
- 데이터를 한 글자씩 읽는 대신, 일정 크기의 버퍼에 데이터를 한 번에 읽어와서 처리 속도 높힘
- 동작 방식
    - 버퍼에서 데이터 읽어옴
    - 한글자씩 처리
    - 문자열로 결합

```java
import java.io.*;

public class SimpleBufferedReader {

    private InputStreamReader inputStreamReader;
    private char[] buffer;
    private int bufferSize;
    private int bufferPosition;
    private int bufferCount;

    public SimpleBufferedReader(int bufferSize, InputStreamReader inputStreamReader) {
        this.bufferSize = bufferSize;
        this.inputStreamReader = inputStreamReader;
        this.buffer = new char[bufferSize];
        this.bufferPosition = 0;
        this.bufferCount = 0;
    }

    private void fillBuffer() throws IOException {
        bufferCount = inputStreamReader.read(buffer, 0, bufferSize);
        bufferPosition = 0;
    }

    public String readLine() throws IOException {
        StringBuilder line = new StringBuilder();
        boolean endLine = false;

        while (!endLine) {
            if (bufferPosition >= bufferCount) {
                fillBuffer();
                if (bufferCount == -1) {
                    return line.length() > 0 ? line.toString() : null;
                }
            }

            for(; bufferPosition < bufferCount; bufferPosition++) {
                char ch = buffer[bufferPosition];
                if (ch == '\n') {
                    endLine = true;
                    bufferPosition++;
                    break;
                } else if (ch == '\r') {
                    endLine = true;
                    if (bufferPosition + 1 < bufferCount && buffer[bufferPosition + 1] == '\n') {
                        bufferPosition++;
                    }
                    bufferPosition++;
                    break;
                } else {
                    line.append(ch);
                }
            }
        }
        return line.toString();
    }

    public void close() throws IOException {
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
    }
}

```
