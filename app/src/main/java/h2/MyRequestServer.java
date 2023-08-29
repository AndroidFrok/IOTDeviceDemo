package h2;

import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.BodyType;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2020/10/02
 * desc   : 服务器配置
 */
public class MyRequestServer implements IRequestServer {

    @Override
    public String getHost() {
        return "https://api.zxwlzngx.com/";
    }

    @NonNull
    @Override
    public BodyType getBodyType() {
        return BodyType.FORM;
    }
}