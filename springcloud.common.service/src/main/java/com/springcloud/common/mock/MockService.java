package com.springcloud.common.mock;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class MockService {

    public void dynamicMock(HttpServletRequest request, HttpServletResponse response) {
        String requestPath = request.getServletPath().replace("/mock", "");
        // 下面这一行，替换为自己的业务代码，拿到想要的报文，响应代码，响应头等
       /* MockDO mockDO = new LambdaQueryChainWrapper<>(mockMapper).eq(MockDO::getPath, requestPath).one();
        // 设置HttpServletResponse结果，这里只以“报文”和“响应代码”举例
        try (OutputStream outputStream = response.getOutputStream()) {
            // 设置报文
            byte[] dataByteArr = mockDO.getResponseBody().getBytes(StandardCharsets.UTF_8);
            outputStream.write(dataByteArr);
            // 设置响应代码
            response.setStatus(mockDO.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

}
